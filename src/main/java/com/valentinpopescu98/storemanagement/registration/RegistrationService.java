package com.valentinpopescu98.storemanagement.registration;

import com.valentinpopescu98.storemanagement.user.User;
import com.valentinpopescu98.storemanagement.user.UserService;
import com.valentinpopescu98.storemanagement.user.authorities.Authority;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.valentinpopescu98.storemanagement.user.authorities.Role.*;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;

    public String registerSubmit(RegistrationRequest request) {
        String authorityParam = request.getAuthority().toUpperCase();
        Authority authority;

        switch (authorityParam) {
            case "USER":
                authority = new Authority(USER);
                break;
            case "ADMIN":
                authority = new Authority(ADMIN);
                break;
            case "OWNER":
                authority = new Authority(OWNER);
                break;
            default:
                throw new IllegalArgumentException("Authority can be either 'USER', 'ADMIN' or 'OWNER'");
        }

        userService.signUpUser(
                new User(
                        request.getUsername(),
                        request.getPassword(),
                        false,
                        true,
                        authority
                )
        );

        return String.format("User '%s' registered successfully", request.getUsername());
    }

}
