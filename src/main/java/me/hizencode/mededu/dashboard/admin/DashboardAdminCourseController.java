package me.hizencode.mededu.dashboard.admin;

import me.hizencode.mededu.courses.CourseDescriptionEntity;
import me.hizencode.mededu.courses.CourseDetailEntity;
import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import me.hizencode.mededu.dashboard.admin.dto.AdminCourseDto;
import me.hizencode.mededu.dashboard.admin.dto.AdminSearchDataDto;
import me.hizencode.mededu.image.ImageEntity;
import me.hizencode.mededu.specialities.SpecialityEntity;
import me.hizencode.mededu.specialities.SpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardAdminCourseController {

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
    private String showManageCourses(@RequestParam(value = "text", required = false, defaultValue = "") String text,
                                 @RequestParam(value = "specialities", required = false, defaultValue = "") List<String> specialitiesIds,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") String pageNumberStr,
                                 Model model) {
        int pageNumber;

        try {
            pageNumber = Integer.parseInt(pageNumberStr);
            if(pageNumber < 1) {
                return "redirect:/user-dashboard/manage-courses";
            }
        } catch (NumberFormatException e) {
            return "redirect:/user-dashboard/manage-courses";
        }

        if(text.length() > 128 || specialitiesIds.size() > 128) {
            return "redirect:/user-dashboard/manage-courses";
        }

        //Get specialities from db and add to the model
        ArrayList<SpecialityEntity> specialities = specialityService.getAllSpecialities();
        model.addAttribute("specialities", specialities);

        //Specialities for filter option
        AdminSearchDataDto searchData = new AdminSearchDataDto();
        model.addAttribute("searchData", searchData);

        //Do the standard query or search
        if(text.isEmpty() && specialitiesIds.isEmpty()) {
            //Fetch 10 courses by editDate
            Page<CourseEntity> page = courseService.findAll(
                    PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Direction.DESC, "editDate")));

            model.addAttribute("coursesPage", page);

            return "user-dashboard/admin/courses/manage-courses";
        }

        if(!text.isEmpty() && specialitiesIds.isEmpty()) {
            Page<CourseEntity> page = courseService.findAllByNameIsLike(text,
                    PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Direction.DESC, "editDate")));

            AdminSearchDataDto searchDataDto = new AdminSearchDataDto();
            searchDataDto.setSearchText(text);

            model.addAttribute("searchData", searchDataDto);
            model.addAttribute("coursesPage", page);

            return "user-dashboard/admin/courses/manage-courses";
        }

        if(text.isEmpty()) {
            List<Integer> specialitiesIdsInt = new ArrayList<>();
            for (String stringId : specialitiesIds) {
                try {
                    specialitiesIdsInt.add(Integer.parseInt(stringId));
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }

            Page<CourseEntity> page = courseService.findAllBySpecialities(specialitiesIdsInt,
                    PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Direction.DESC, "editDate")));

            AdminSearchDataDto searchDataDto = new AdminSearchDataDto();
            searchDataDto.setChosenSpecialities(specialitiesIdsInt);

            model.addAttribute("searchData", searchDataDto);
            model.addAttribute("coursesPage", page);

            return "user-dashboard/admin/courses/manage-courses";
        }

        //If both, text and specialities are present
        List<Integer> specialitiesIdsInt = new ArrayList<>();
        for (String stringId : specialitiesIds) {
            try {
                specialitiesIdsInt.add(Integer.parseInt(stringId));
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }

        Page<CourseEntity> page = courseService.findAllByNameAndSpecialities(text, specialitiesIdsInt,
                PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Direction.DESC, "editDate")));

        AdminSearchDataDto searchDataDto = new AdminSearchDataDto();
        searchDataDto.setChosenSpecialities(specialitiesIdsInt);
        searchDataDto.setSearchText(text);

        model.addAttribute("searchData", searchDataDto);
        model.addAttribute("coursesPage", page);

        return "user-dashboard/admin/courses/manage-courses";
    }

    @GetMapping("/user-dashboard/manage-courses/create")
    private String createCourse(Model model) {
        //Get specialities from db and add to the model
        ArrayList<SpecialityEntity> specialities = specialityService.getAllSpecialities();
        model.addAttribute("specialities", specialities);

        AdminCourseDto course = new AdminCourseDto();
        model.addAttribute("course", course);

        return "user-dashboard/admin/courses/create-course";
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
            //TODO: Check type of the file
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

    @GetMapping("/user-dashboard/manage-courses/edit/course{courseId}")
    private String editCourse(@PathVariable("courseId") Integer courseId, Model model) {

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

        return "user-dashboard/admin/courses/edit-course";
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
