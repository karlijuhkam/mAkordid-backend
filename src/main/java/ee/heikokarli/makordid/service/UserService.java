package ee.heikokarli.makordid.service;

import ee.heikokarli.makordid.data.dto.request.auth.RegisterRequest;
import ee.heikokarli.makordid.data.dto.request.user.PatchUserRequest;
import ee.heikokarli.makordid.data.entity.user.Role;
import ee.heikokarli.makordid.data.entity.user.User;
import ee.heikokarli.makordid.data.repository.user.RoleRepository;
import ee.heikokarli.makordid.data.repository.user.UserRepository;
import ee.heikokarli.makordid.exception.BadRequestException;
import ee.heikokarli.makordid.exception.user.PasswordTooShortException;
import ee.heikokarli.makordid.exception.user.UserAlreadyExistsException;
import ee.heikokarli.makordid.exception.user.UserNotFoundException;
import ee.heikokarli.makordid.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private EmailService emailService;
    private SecureRandom random;
    private String randomAlphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder encoder,
                       EmailService emailService,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = encoder;
        this.emailService = emailService;
        this.random = new SecureRandom();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    public UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) authentication.getPrincipal();
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public User modifyUser(User user, PatchUserRequest request) {
        if (request.getPassword() != null) {
            if (request.getOldPassword() == null || !passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new BadRequestException("Vale parool!");
            }
            if (request.getPassword().length() < 8) {
                throw new PasswordTooShortException();
            }
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getAge() != null) {
            user.setAge(request.getAge());
        }

        return userRepository.save(user);
    }

    public void generateNewPassword(User user) throws MessagingException {
        String newPassword = generatePassword(8);
        user.setPassword(passwordEncoder.encode(newPassword));
        emailService.sendMail("mAkordid", user.getEmail(),
                "mAkordid.ee parooli taastamine",
                "<br><div style=\"text-align: center; font-size: 16px; font-weight: 600\">Saadame Sulle portaali mAkordid kasutamiseks uue parooli:</div>" +
                        "<br><br><div style=\"background-color: white; font-size: 26px; padding: 5px; text-align: center; " +
                        "width: 200px; background-color: #f7f7f7; margin: 0 auto; margin-top: 15px;\"><b>" + newPassword + "</b></div>" +
                        "<br><br><br><div style=\"font-weight: 600; text-align: center;\">Turvalisuse huvides soovitame Sul parooli uuendada.</div>");
        userRepository.save(user);
    }

    public User register(RegisterRequest request) throws MessagingException {

        User user = new User();

        if (request.getEmail() != null) {
            User userCheck = getUserByEmail(request.getEmail());
            if (userCheck != null) {
                throw new UserAlreadyExistsException("Sellise emailiga kasutaja on juba olemas!");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getUsername() != null) {
            User userCheck = getUserByUsername(request.getUsername());
            if (userCheck != null) {
                throw new UserAlreadyExistsException("See kasutajanimi on juba kasutusel!");
            }
            user.setEmail(request.getEmail());
        }

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("user"));
        user.setRoles(roles);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setStatus(User.UserStatus.active);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        emailService.sendMail("mAkordid", user.getEmail(),
                "Tere tulemast portaali mAkordid!",
                "<br><div style=\"text-align: center; font-size: 16px; font-weight: 600\">Tere tulemast, " + request.getFirstName() + " " + request.getLastName() + "!" +
                        "<br>Olete registreerinud end mAkordid kasutajaks.</div>");
        return userRepository.save(user);
    }


    public String generatePassword(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(randomAlphabet.charAt(random.nextInt(randomAlphabet.length())));
        }
        return sb.toString();
    }

}
