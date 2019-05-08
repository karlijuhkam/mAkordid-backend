package ee.heikokarli.makordid.service;

import ee.heikokarli.makordid.data.dto.request.auth.RegisterRequest;
import ee.heikokarli.makordid.data.dto.request.user.PatchUserRequest;
import ee.heikokarli.makordid.data.dto.request.user.UserListRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ee.heikokarli.makordid.data.specifications.UserSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.where;

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

    public Page<User> getAllUsers(UserListRequest request, Pageable pageable) {
        Specification<User> spec = where(null);

        if (request.getUsername() != null) spec = spec.and(username(request.getUsername()));
        if (request.getEmail() != null) spec = spec.and(email(request.getEmail()));
        if (request.getStatus() != null) spec = spec.and(status(request.getStatus()));
        if (request.getRoles() != null) spec = spec.and(hasRoles(request.getRoles()));

        return userRepository.findAll(spec, pageable);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
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
        return (UserDetails) authentication.getPrincipal();
    }

    public Page<User> getSpecifiedUsers(Pageable pageable) {
        Specification<User> spec = where(null);
        spec = spec.and(status(User.UserStatus.active));
        return userRepository.findAll(spec, pageable);
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public boolean isCurrentUserAnAdmin() {
        boolean result = false;
        for (Role role:getCurrentUser().getRoles()) {
            if (role.getName().equals("admin")) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean isCurrentUserAModerator() {
        boolean result = false;
        for (Role role:getCurrentUser().getRoles()) {
            if (role.getName().equals("moderator")) {
                result = true;
                break;
            }
        }
        return result;
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

        if (request.getRoles() != null) {
            user.setRoles(request.getRoles());
        }

        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
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

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("user"));
        user.setUsername(request.getUsername());
        user.setRoles(roles);
        user.setStatus(User.UserStatus.active);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
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
