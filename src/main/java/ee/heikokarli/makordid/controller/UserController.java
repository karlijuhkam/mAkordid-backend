package ee.heikokarli.makordid.controller;

import ee.heikokarli.makordid.data.dto.request.auth.ForgotPasswordRequest;
import ee.heikokarli.makordid.data.dto.request.auth.LoginRequest;
import ee.heikokarli.makordid.data.dto.request.auth.RegisterRequest;
import ee.heikokarli.makordid.data.dto.response.GenericMessageResponse;
import ee.heikokarli.makordid.data.dto.response.auth.LoginResponse;
import ee.heikokarli.makordid.data.dto.response.error.ErrorResponse;
import ee.heikokarli.makordid.data.entity.auth.AuthToken;
import ee.heikokarli.makordid.data.entity.user.User;
import ee.heikokarli.makordid.exception.BadRequestException;
import ee.heikokarli.makordid.exception.user.UserNotFoundException;
import ee.heikokarli.makordid.service.AuthenticationService;
import ee.heikokarli.makordid.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Api(tags = {"User"})
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

    @PostMapping("/login")
    @ApiOperation(
            value = "User login",
            tags = "User"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Invalid email/password", response = ErrorResponse.class)
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        User user = userService.getUserByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {

            AuthToken token = authenticationService.saveToken(user.getUsername());

            return new ResponseEntity<>(new LoginResponse(token, user), HttpStatus.OK);
        }
        throw new BadRequestException("Invalid email/password");
    }

    @PostMapping("/logout")
    public ResponseEntity logout() {
        authenticationService.removeToken(userService.getCurrentUserDetails().getToken());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/forgotpassword")
    @ApiOperation(
            value = "User password recovery",
            tags = "User"
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
            tags = "User"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "User does not exist", response = ErrorResponse.class)
    })
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) throws MessagingException {
        User user = userService.register(request);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}