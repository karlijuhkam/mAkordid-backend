package ee.heikokarli.makordid.controller;

import ee.heikokarli.makordid.data.dto.request.auth.ForgotPasswordRequest;
import ee.heikokarli.makordid.data.dto.request.auth.LoginRequest;
import ee.heikokarli.makordid.data.dto.request.auth.RegisterRequest;
import ee.heikokarli.makordid.data.dto.request.user.PatchUserRequest;
import ee.heikokarli.makordid.data.dto.request.user.UserListRequest;
import ee.heikokarli.makordid.data.dto.response.GenericMessageResponse;
import ee.heikokarli.makordid.data.dto.response.auth.LoginResponse;
import ee.heikokarli.makordid.data.dto.response.error.ErrorResponse;
import ee.heikokarli.makordid.data.dto.user.UserDto;
import ee.heikokarli.makordid.data.entity.auth.AuthToken;
import ee.heikokarli.makordid.data.entity.user.User;
import ee.heikokarli.makordid.exception.BadRequestException;
import ee.heikokarli.makordid.exception.user.UserInactiveException;
import ee.heikokarli.makordid.exception.user.UserNotFoundException;
import ee.heikokarli.makordid.service.AuthenticationService;
import ee.heikokarli.makordid.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.data.domain.PageRequest.of;

@RestController
@Api(tags = {"Users"})
public class UserController extends AbstractApiController {

    private AuthenticationService authenticationService;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(AuthenticationService authenticationService, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @ApiOperation(
            value = "Get user by Id",
            tags = "Users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "´User not found.", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @PreAuthorize("hasAnyAuthority('admin', 'moderator')")
    @RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
    public UserDto getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return new UserDto(user);
    }

    @ApiOperation(
            value = "Modify user by Id",
            tags = "Users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @PreAuthorize("hasAnyAuthority('admin', 'moderator')")
    @RequestMapping(path = "/users/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<UserDto> patchUser(@PathVariable Long id, @Valid @RequestBody PatchUserRequest request){
        User user = userService.modifyUser(userService.getUserById(id),request);
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Get current user profile",
            tags = "Users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getCurrentUser(){
        User user = userService.getCurrentUser();
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Get all users paginated/filtered/sorted",
            tags = "Users"
    )
    @PreAuthorize("hasAnyAuthority('moderator','admin')")
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public Page<UserDto> getSongList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String roles,
            @RequestParam(required = false) User.UserStatus status,
            @RequestParam(defaultValue = "username") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDir
    ) {

        UserListRequest request = new UserListRequest(username, email, roles, status);

        return userService.getAllUsers(request, of(page, size, sortDir, sort))
                .map(UserDto::new);
    }

    @PostMapping("/login")
    @ApiOperation(
            value = "User login",
            tags = "Users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Invalid email/password", response = ErrorResponse.class)
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        User user = userService.getUserByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {

            if (user.getStatus() == User.UserStatus.inactive) {
                throw new UserInactiveException();
            }

            AuthToken token = authenticationService.saveToken(user.getEmail());

            return new ResponseEntity<>(new LoginResponse(token, user), HttpStatus.OK);
        }
        throw new BadRequestException("Invalid email/password");
    }

    @PostMapping("/logout")
    @ApiOperation(
            value = "User logout",
            tags = "Users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    public ResponseEntity logout() {
        authenticationService.removeToken(userService.getCurrentUserDetails().getToken());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/forgotpassword")
    @ApiOperation(
            value = "User password recovery",
            tags = "Users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "User does not exist", response = ErrorResponse.class)
    })
    public ResponseEntity<GenericMessageResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) throws MessagingException {
        String email = request.getEmail();
        User user = userService.getUserByEmail(email);
        if (user != null) {
            userService.generateNewPassword(user);
            return new ResponseEntity<>(new GenericMessageResponse("New password sent to " + user.getEmail()), HttpStatus.OK);
        } else {
            throw new UserNotFoundException();
        }
    }

    @PostMapping("/register")
    @ApiOperation(
            value = "User registration",
            tags = "Users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "User does not exist", response = ErrorResponse.class)
    })
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) throws MessagingException {
        User user = userService.register(request);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Modify current user",
            tags = "Users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @RequestMapping(path = "/profile", method = RequestMethod.PATCH)
    public ResponseEntity<UserDto> patchCurrentUser(@Valid @RequestBody PatchUserRequest request){
        User user = userService.modifyUser(userService.getCurrentUser(), request);
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Get top 5 users by addedSongsCount",
            tags = "Users"
    )
    @RequestMapping(path = "/topusers", method = RequestMethod.GET)
    public Page<UserDto> getTopFiveByAddedSongsCount() {
        return userService.getSpecifiedUsers(of(0, 5, Sort.Direction.DESC, "addedSongsCount"))
                .map(UserDto::new);
    }

}
