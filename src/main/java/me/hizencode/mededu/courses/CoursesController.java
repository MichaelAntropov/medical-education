package me.hizencode.mededu.courses;

import me.hizencode.mededu.dashboard.admin.SearchCoursesDto;
import me.hizencode.mededu.specialities.SpecialityEntity;
import me.hizencode.mededu.specialities.SpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class CoursesController {

    /*Fields*/
    /*================================================================================================================*/
    private SpecialityService specialityService;

    private CourseService courseService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setSpecialityService(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    /*Methods*/
    /*================================================================================================================*/

    @GetMapping("/courses")
    public String showCourses(Model model) {
        //Get specialities from db and add to the model
        ArrayList<SpecialityEntity> specialities = specialityService.getAllSpecialities();
        model.addAttribute("specialities", specialities);

        //Specialities for filter option
        SearchCoursesDto searchCourses = new SearchCoursesDto();
        model.addAttribute("searchCourses", searchCourses);

        //Fetch 10 courses by name
        Page<CourseEntity> page = courseService.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "creationDate")));

        model.addAttribute("coursePage", page);

        return "courses/courses";
    }

}
