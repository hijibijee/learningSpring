package tacos.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.User;
import tacos.data.UserRepository;

@Controller
@RequestMapping("/register")
@Slf4j
public class RegistrationController {
    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(
        UserRepository userRepo,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        User user = form.toUser(passwordEncoder);
        log.info("Saving user: {}", user);
        userRepo.save(user);
        return "redirect:/login";
    }
}
