package ee.heikokarli.makordid.controller;

import ee.heikokarli.makordid.data.dto.request.auth.LoginRequest;
import ee.heikokarli.makordid.data.dto.response.auth.LoginResponse;
import ee.heikokarli.makordid.data.dto.response.error.ErrorResponse;
import ee.heikokarli.makordid.data.entity.auth.AuthToken;
import ee.heikokarli.makordid.data.entity.user.User;
import ee.heikokarli.makordid.exception.BadRequestException;
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

    @RequestMapping(path = "/clients", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('admin')")
    public void createClient() {
        System.out.println("Admin");
    }

    @GetMapping("/test")
    public void showText() {
        System.out.println("Tere kõik!");
    }



}