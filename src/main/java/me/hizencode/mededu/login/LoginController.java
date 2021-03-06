package me.hizencode.mededu.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "403";
    }

    @GetMapping("/not-found")
    public String showNotFound() {
        return "404";
    }
}
