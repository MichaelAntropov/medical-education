package me.hizencode.mededu.courses;

import me.hizencode.mededu.dashboard.admin.CourseDto;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Optional;

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

    @GetMapping("/courses/course{courseId}")
    private String editCourse(@PathVariable("courseId") Integer courseId, Model model) {

        Optional<CourseEntity> courseOptional = courseService.getCourseById(courseId);

        if (courseOptional.isEmpty()) {
            return "redirect:/courses";
        }
        //Get specialities from db and add to the model
        ArrayList<SpecialityEntity> specialities = specialityService.getAllSpecialities();
        model.addAttribute("specialities", specialities);

        CourseEntity courseEntity = courseOptional.get();
        CourseDto course = new CourseDto();

        course.setName(courseEntity.getName());
        course.setChosenSpecialities(courseEntity.getSpecialities());

        course.setVideoUrl(courseEntity.getCourseDetails().getVideoUrl());
        course.setStartCourse(courseEntity.getCourseDetails().getStartCourse());
        course.setEndCourse(courseEntity.getCourseDetails().getEndCourse());
        course.setAuthor(courseEntity.getCourseDetails().getAuthor());

        course.setDescription(courseEntity.getCourseDescription().getText());

        course.setCertificateAvailable(false);

        //Set image ID to create url later
        if (courseEntity.getCourseDetails().getImage() != null) {
            course.setImageId(courseEntity.getCourseDetails().getImage().getId());
        }

        //Add course id
        course.setCourseId(courseEntity.getId());
        //Add course to model. Note: Image is shown to user through ImageController
        model.addAttribute("course", course);

        return "courses/course-page";
    }

}
