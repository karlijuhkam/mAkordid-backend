package ee.heikokarli.makordid.service;

import ee.heikokarli.makordid.data.dto.request.auth.RegisterRequest;
import ee.heikokarli.makordid.data.entity.user.Role;
import ee.heikokarli.makordid.data.entity.user.User;
import ee.heikokarli.makordid.data.repository.user.RoleRepository;
import ee.heikokarli.makordid.data.repository.user.UserRepository;
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
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private SecureRandom random;
    private EmailService emailService;
    private RoleRepository roleRepository;
    private String randomAlphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Autowired
    public UserService(UserRepository userRepository,
                       EmailService emailService,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = encoder;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.random = new SecureRandom();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    public UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        System.out.println("***********************");
        System.out.println(authentication.getPrincipal());
        return (UserDetails) authentication.getPrincipal();
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
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("user"));
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRoles(roles);
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