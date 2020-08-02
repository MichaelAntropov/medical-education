package me.hizencode.doctormanagement.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @RequestMapping("/user")
    public String user(){
        return "user/user";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin/admin";
    }
}
