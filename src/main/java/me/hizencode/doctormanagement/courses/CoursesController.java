package me.hizencode.doctormanagement.courses;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CoursesController {

    @GetMapping("/courses")
    public String showCourses() {
        return "courses/courses";
    }

}
