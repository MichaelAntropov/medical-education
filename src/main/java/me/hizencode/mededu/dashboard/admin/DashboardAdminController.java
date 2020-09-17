package me.hizencode.mededu.dashboard.admin;

import me.hizencode.mededu.courses.CourseDescriptionEntity;
import me.hizencode.mededu.courses.CourseDetailEntity;
import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import me.hizencode.mededu.dashboard.admin.dto.AdminCourseDto;
import me.hizencode.mededu.dashboard.admin.dto.AdminSearchCoursesDto;
import me.hizencode.mededu.image.ImageEntity;
import me.hizencode.mededu.specialities.SpecialityEntity;
import me.hizencode.mededu.specialities.SpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class DashboardAdminController {

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

    @GetMapping("/user-dashboard/manage-courses")
    private String manageCourses(Model model) {
        //Get specialities from db and add to the model
        ArrayList<SpecialityEntity> specialities = specialityService.getAllSpecialities();
        model.addAttribute("specialities", specialities);

        //Specialities for filter option
        AdminSearchCoursesDto searchCourses = new AdminSearchCoursesDto();
        model.addAttribute("searchCourses", searchCourses);

        //Fetch 10 courses by name
        Page<CourseEntity> page = courseService.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "editDate")));

        model.addAttribute("coursePage", page);

        return "user-dashboard/admin/manage-courses";
    }

    @PostMapping("/user-dashboard/manage-courses/search")
    private String searchCourses(@ModelAttribute(name = "searchCourses") @Valid AdminSearchCoursesDto searchCoursesDto,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/user-dashboard/manage-courses";
        }

        //Do the search
        System.out.println("//================================================");
        searchCoursesDto.getChosenSpecialities().forEach(System.out::println);
        System.out.println(searchCoursesDto.getSearchText());
        System.out.println("//================================================");

        //Get specialities from db and add to the model
        ArrayList<SpecialityEntity> specialities = specialityService.getAllSpecialities();
        model.addAttribute("specialities", specialities);

        //Set values in search and filter for filter option
        AdminSearchCoursesDto searchCourses = new AdminSearchCoursesDto();
        searchCourses.setChosenSpecialities(searchCoursesDto.getChosenSpecialities());
        searchCourses.setSearchText(searchCoursesDto.getSearchText());

        model.addAttribute("searchCourses", searchCourses);

        //TODO:Add searched courses

        return "user-dashboard/admin/manage-courses";
    }

    @GetMapping("/user-dashboard/manage-courses/create")
    private String createCourse(Model model) {
        //Get specialities from db and add to the model
        ArrayList<SpecialityEntity> specialities = specialityService.getAllSpecialities();
        model.addAttribute("specialities", specialities);

        AdminCourseDto course = new AdminCourseDto();
        model.addAttribute("course", course);

        return "user-dashboard/admin/create-course";
    }

    @PostMapping("/user-dashboard/manage-courses/create/save")
    private String createSaveCourse(@ModelAttribute(name = "course") @Valid AdminCourseDto course,
                                    @RequestParam("image") MultipartFile image) {

        CourseDescriptionEntity courseDescriptionEntity = new CourseDescriptionEntity();
        CourseDetailEntity courseDetailEntity = new CourseDetailEntity();
        CourseEntity courseEntity = new CourseEntity();

        courseEntity.setName(course.getName());
        courseEntity.setSpecialities(course.getChosenSpecialities());

        courseDetailEntity.setVideoUrl(course.getVideoUrl());
        courseDetailEntity.setStartCourse(course.getStartCourse());
        courseDetailEntity.setEndCourse(course.getEndCourse());
        courseDetailEntity.setAuthor(course.getAuthor());

        courseDescriptionEntity.setText(course.getDescription());


        //Get image
        if (!image.isEmpty()) {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setName(image.getOriginalFilename());
            imageEntity.setType(image.getContentType());
            try {
                imageEntity.setData(image.getInputStream().readAllBytes());
                courseDetailEntity.setImage(imageEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Date creationDate = new Date();
        courseEntity.setCreationDate(creationDate);
        courseEntity.setEditDate(creationDate);

        courseService.saveNewCourse(courseEntity, courseDetailEntity, courseDescriptionEntity);

        return "redirect:/user-dashboard/manage-courses";
    }

    @PostMapping("/user-dashboard/manage-courses/edit")
    private String editCourse(@RequestParam("courseId") Integer courseId, Model model) {

        Optional<CourseEntity> courseOptional = courseService.getCourseById(courseId);

        if (courseOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }
        //Get specialities from db and add to the model
        ArrayList<SpecialityEntity> specialities = specialityService.getAllSpecialities();
        model.addAttribute("specialities", specialities);

        CourseEntity courseEntity = courseOptional.get();
        AdminCourseDto course = new AdminCourseDto();

        course.setName(courseEntity.getName());
        course.setChosenSpecialities(courseEntity.getSpecialities());

        course.setVideoUrl(courseEntity.getCourseDetails().getVideoUrl());
        course.setStartCourse(courseEntity.getCourseDetails().getStartCourse());
        course.setEndCourse(courseEntity.getCourseDetails().getEndCourse());
        course.setAuthor(courseEntity.getCourseDetails().getAuthor());

        course.setDescription(courseEntity.getCourseDescription().getText());

        //Set image ID to create url later
        if (courseEntity.getCourseDetails().getImage() != null) {
            course.setImageId(courseEntity.getCourseDetails().getImage().getId());
        }

        //Add course id
        course.setCourseId(courseEntity.getId());
        //Add course to model. Note: Image is shown to user through ImageController
        model.addAttribute("course", course);

        return "user-dashboard/admin/edit-course";
    }

    @PostMapping("/user-dashboard/manage-courses/edit/save")
    private String editSaveCourse(@ModelAttribute(name = "course") @Valid AdminCourseDto course,
                                  @RequestParam("image") MultipartFile image) {

        Optional<CourseEntity> courseOptional = courseService.getCourseById(course.getCourseId());

        if (courseOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }
        CourseEntity courseEntity = courseOptional.get();
        CourseDetailEntity courseDetailEntity = courseEntity.getCourseDetails();
        CourseDescriptionEntity courseDescriptionEntity = courseEntity.getCourseDescription();

        courseEntity.setName(course.getName());
        courseEntity.setSpecialities(course.getChosenSpecialities());

        courseDetailEntity.setVideoUrl(course.getVideoUrl());
        courseDetailEntity.setStartCourse(course.getStartCourse());
        courseDetailEntity.setEndCourse(course.getEndCourse());
        courseDetailEntity.setAuthor(course.getAuthor());

        courseDescriptionEntity.setText(course.getDescription());

        //Get image
        if (!image.isEmpty()) {
            ImageEntity imageEntity = courseDetailEntity.getImage();
            imageEntity.setName(image.getOriginalFilename());
            imageEntity.setType(image.getContentType());
            try {
                imageEntity.setData(image.getInputStream().readAllBytes());
                courseDetailEntity.setImage(imageEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Date editDate = new Date();
        courseEntity.setEditDate(editDate);

        courseService.saveCourse(courseEntity);

        return "redirect:/user-dashboard/manage-courses";
    }

    @PostMapping("/user-dashboard/manage-courses/delete")
    private String deleteCourse(@RequestParam("courseId") Integer courseId) {

        courseService.deleteCourseWhereId(courseId);

        return "redirect:/user-dashboard/manage-courses";
    }
}
