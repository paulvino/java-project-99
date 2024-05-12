package hexlet.code.component;

import hexlet.code.model.User;
import hexlet.code.service.CustomUserDetailsService;
//import hexlet.code.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final CustomUserDetailsService userService;
//
//    @Autowired
//    private final UserUtils userUtils;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var admin = new User();
        admin.setEmail(email);
        admin.setPasswordDigest("qwerty");
        userService.createUser(admin);
    }
}
