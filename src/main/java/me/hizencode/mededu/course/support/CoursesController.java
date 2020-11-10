package me.hizencode.mededu.course.support;

import me.hizencode.mededu.course.CourseEntity;
import me.hizencode.mededu.course.CourseService;
import me.hizencode.mededu.course.content.CourseContentItemType;
import me.hizencode.mededu.course.content.user.CourseUserEntity;
import me.hizencode.mededu.course.content.user.CourseUserService;
import me.hizencode.mededu.course.support.dto.CourseDto;
import me.hizencode.mededu.course.support.dto.SearchDataDto;
import me.hizencode.mededu.specialities.SpecialityEntity;
import me.hizencode.mededu.specialities.SpecialityService;
import me.hizencode.mededu.user.UserPrincipal;
import me.hizencode.mededu.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CoursesController {

    /*Fields*/
    /*================================================================================================================*/
    private SpecialityService specialityService;

    private CourseService courseService;

    private CourseUserService courseUserService;

    private UserService userService;

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

    @Autowired
    public void setCourseUserService(CourseUserService courseUserService) {
        this.courseUserService = courseUserService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /*Methods*/
    /*================================================================================================================*/

    @GetMapping("/courses")
    public String showCourses(@RequestParam(value = "text", required = false, defaultValue = "") String text,
                              @RequestParam(value = "specialities", required = false, defaultValue = "") List<String> specialitiesIds,
                              @RequestParam(value = "page", required = false, defaultValue = "1") String pageNumberStr,
                              Model model) {
        int pageNumber;

        try {
            pageNumber = Integer.parseInt(pageNumberStr);
            if (pageNumber < 1) {
                return "redirect:/user-dashboard/manage-courses";
            }
        } catch (NumberFormatException e) {
            return "redirect:/user-dashboard/manage-courses";
        }

        if (text.length() > 128 || specialitiesIds.size() > 128) {
            return "redirect:/user-dashboard/manage-courses";
        }

        //Get specialities from db and add to the model
        ArrayList<SpecialityEntity> specialities = specialityService.getAllSpecialities();
        model.addAttribute("specialities", specialities);

        //Specialities for filter option
        SearchDataDto searchData = new SearchDataDto();
        model.addAttribute("searchData", searchData);

        //Do the standard query or search
        if (text.isEmpty() && specialitiesIds.isEmpty()) {
            //Fetch 10 courses by editDate
            Page<CourseEntity> page = courseService.findAll(
                    PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Direction.DESC, "creationDate")));

            model.addAttribute("coursesPage", page);

            return "courses/courses";
        }

        if (!text.isEmpty() && specialitiesIds.isEmpty()) {
            Page<CourseEntity> page = courseService.findAllByNameIsLike(text,
                    PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Direction.DESC, "creationDate")));

            SearchDataDto searchDataDto = new SearchDataDto();
            searchDataDto.setSearchText(text);

            model.addAttribute("searchData", searchDataDto);
            model.addAttribute("coursesPage", page);

            return "courses/courses";
        }

        if (text.isEmpty()) {
            List<Integer> specialitiesIdsInt = new ArrayList<>();
            for (String stringId : specialitiesIds) {
                try {
                    specialitiesIdsInt.add(Integer.parseInt(stringId));
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }

            Page<CourseEntity> page = courseService.findAllBySpecialities(specialitiesIdsInt,
                    PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Direction.DESC, "creationDate")));

            SearchDataDto searchDataDto = new SearchDataDto();
            searchDataDto.setChosenSpecialities(specialitiesIdsInt);

            model.addAttribute("searchData", searchDataDto);
            model.addAttribute("coursesPage", page);

            return "courses/courses";
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
                PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Direction.DESC, "creationDate")));

        SearchDataDto searchDataDto = new SearchDataDto();
        searchDataDto.setChosenSpecialities(specialitiesIdsInt);
        searchDataDto.setSearchText(text);

        model.addAttribute("searchData", searchDataDto);
        model.addAttribute("coursesPage", page);

        return "courses/courses";
    }

    @GetMapping("/courses/course{courseId}")
    private String editCourse(@PathVariable("courseId") Integer courseId,
                              Authentication authentication, Model model) {

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

        //Check if the user signed for course
        boolean isSigned = false;
        if (authentication != null) {
            isSigned = courseUserService
                    .isUserSignedOnCourse((UserPrincipal) authentication.getPrincipal(), courseEntity.getId());
        }
        model.addAttribute("isSigned", isSigned);

        return "courses/course-page";
    }

    @GetMapping("/courses/course{courseId}/continue")
    private String continueCourse(@PathVariable Integer courseId, Authentication authentication) {

        if (userService.userHasRole(authentication, "ADMIN")) {
            Pair<Integer, CourseContentItemType> pair = courseUserService.findFirstContentItem(courseId);
            return "redirect:/courses/course" + courseId + "/" + pair.getSecond().value + "" + pair.getFirst();
        }

        if (userService.userHasRole(authentication, "USER")) {

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Optional<CourseUserEntity> courseUserEntity =
                    courseUserService.findByUserIdAndCourseId(userPrincipal.getId(), courseId);

            if (courseUserEntity.isEmpty()) {
                return "redirect:/courses/course" + courseId;
            }

            Pair<Integer, CourseContentItemType> pair =
                    courseUserService.findContentItemByCourseIdAndOrderNumber(courseId, courseUserEntity.get().getLastOrderNumber());

            return "redirect:/courses/course" + courseId + "/" +
                    pair.getSecond().value + "" +
                    pair.getFirst();
        }

        return "redirect:/courses/course" + courseId;
    }

    @PostMapping("/courses/course/signup")
    private String takeCourse(@RequestParam Integer courseId, Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        //Check if the user is already signed for course
        if (courseUserService.isUserSignedOnCourse(userPrincipal, courseId)) {
            return "redirect:/courses/course" + courseId;
        }

        Optional<CourseEntity> courseEntity = courseService.getCourseById(courseId);

        if (courseEntity.isEmpty()) {
            return "redirect:/courses";
        }

        courseUserService.signUserForCourse(userPrincipal.getId(), courseEntity.get());

        return "redirect:/courses/course" + courseId + "/continue";
    }
}
