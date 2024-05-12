package hexlet.code.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/welcome")
@RestController
public class WelcomeController {

    @GetMapping(path = "")
    public String welcome() {
        return "Welcome to Spring";
    }
}
