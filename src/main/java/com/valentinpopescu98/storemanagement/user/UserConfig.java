package com.valentinpopescu98.storemanagement.user;

import com.valentinpopescu98.storemanagement.user.authorities.Authority;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.valentinpopescu98.storemanagement.user.authorities.Role.*;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            User user = new User("user", passwordEncoder.encode("123456"),
                    false, true, new Authority(USER));

            User admin = new User("admin", passwordEncoder.encode("123456"),
                    false, true, new Authority(ADMIN));

            User owner = new User("owner", passwordEncoder.encode("123456"),
                    false, true, new Authority(OWNER));

            userRepository.saveAll(List.of(user, admin, owner));
        };
    }

}
