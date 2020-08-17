package me.hizencode.doctormanagement.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/user-dashboard")
    private String showDashboard() {
        return "user-dashboard/user-dashboard";
    }
}
