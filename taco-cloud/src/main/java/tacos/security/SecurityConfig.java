package tacos.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tacos.User;
import tacos.data.UserRepository;

@Configuration
@Slf4j
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            User user = userRepo.findByUsername(username);
            log.info("User: {}", user);
            if (user != null) return user;

            throw new UsernameNotFoundException("User: " + username + " not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeRequests()
                .antMatchers("/design", "/orders").access("hasRole('USER')")
                .antMatchers("/", "/**").access("permitAll()")

            .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")

            .and()
                .build();
    }
}
