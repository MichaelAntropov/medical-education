package me.hizencode.mededu.dashboard.admin;

import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import me.hizencode.mededu.dashboard.admin.dto.AdminLessonDto;
import me.hizencode.mededu.lessons.LessonEntity;
import me.hizencode.mededu.lessons.LessonIdTitleOrderNumberOnly;
import me.hizencode.mededu.lessons.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class DashboardAdminCourseLessonsController {

    /*Fields*/
    /*================================================================================================================*/
    private LessonService lessonService;

    private CourseService courseService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setLessonService(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    /*Methods*/
    /*================================================================================================================*/
    @GetMapping("/user-dashboard/manage-courses/course{courseId}/lessons")
    private String showCourseLessons(@PathVariable(name = "courseId") Integer courseId, Model model) {

        //Check if the course exists if no return to main page
        Optional<CourseEntity> courseEntity = courseService.getCourseById(courseId);

        if(courseEntity.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }

        //Get lessons
        List<LessonIdTitleOrderNumberOnly> lessons = lessonService.findAllByCourseIdOnlyIdAndTitleAndOrder(courseId);

        model.addAttribute("courseId", courseId);
        model.addAttribute("lessons", lessons);
        model.addAttribute("courseName", courseEntity.get().getName());
        model.addAttribute("lessonCount", courseEntity.get().getLessonCount());

        return "user-dashboard/admin/courses/lessons/course-lessons";
    }

    @GetMapping("/user-dashboard/manage-courses/course{courseId}/lessons/create")
    private String createLesson(@PathVariable(name = "courseId") Integer courseId, Model model) {

        AdminLessonDto adminLessonDto = new AdminLessonDto();

        adminLessonDto.setCourseId(courseId);

        model.addAttribute("lesson", adminLessonDto);

        return "user-dashboard/admin/courses/lessons/create-lesson";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/create/save")
    private String saveCreatedLesson(@ModelAttribute(name = "lesson") AdminLessonDto adminLessonDto,
                                     Model model) {

        Optional<CourseEntity> courseEntityOptional = courseService.getCourseById(adminLessonDto.getCourseId());

        if(courseEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses/course" + adminLessonDto.getCourseId() + "/lessons";
        }

        CourseEntity courseEntity = courseEntityOptional.get();

        LessonEntity lessonEntity = new LessonEntity();

        lessonEntity.setTitle(adminLessonDto.getTitle());
        lessonEntity.setCourseId(courseEntity.getId());
        lessonEntity.setContent(adminLessonDto.getContent());

        //Set order and update course
        lessonEntity.setOrderNumber(courseEntity.getLessonCount() + 1);
        courseEntity.setLessonCount(courseEntity.getLessonCount() + 1);
        lessonService.saveNewLesson(courseEntity, lessonEntity);

        return "redirect:/user-dashboard/manage-courses/course" + adminLessonDto.getCourseId() + "/lessons";
    }

    @GetMapping("/user-dashboard/manage-courses/course{courseId}/lessons/lesson{lessonId}/edit")
    private String editLesson(@PathVariable(name = "courseId") Integer courseId,
                                @PathVariable(name = "lessonId") Integer lessonId,
                                Model model) {

        Optional<CourseEntity> courseEntity = courseService.getCourseById(courseId);

        if(courseEntity.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses/course" + courseId + "/lessons";
        }

        Optional<LessonEntity> lessonEntityOptional = lessonService.findById(lessonId);

        if(lessonEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses/course" + courseId + "/lessons";
        }

        if(lessonEntityOptional.get().getCourseId() != courseId) {
            return "redirect:/user-dashboard/manage-courses/course" + courseId + "/lessons";
        }

        LessonEntity lessonEntity = lessonEntityOptional.get();

        AdminLessonDto adminLessonDto = new AdminLessonDto();

        adminLessonDto.setId(lessonEntity.getId());
        adminLessonDto.setCourseId(lessonEntity.getCourseId());
        adminLessonDto.setTitle(lessonEntity.getTitle());
        adminLessonDto.setContent(lessonEntity.getContent());

        model.addAttribute("lesson", adminLessonDto);

        return "user-dashboard/admin/courses/lessons/edit-lesson";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/edit/save")
    private String saveEditedLesson(@ModelAttribute(name = "lesson") AdminLessonDto adminLessonDto) {

        Optional<LessonEntity> lessonEntityOptional = lessonService.findById(adminLessonDto.getId());

        if(lessonEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses/course" + adminLessonDto.getCourseId() + "/lessons";
        }

        LessonEntity lessonEntity = lessonEntityOptional.get();

        lessonEntity.setTitle(adminLessonDto.getTitle());
        lessonEntity.setContent(adminLessonDto.getContent());

        lessonService.saveLesson(lessonEntity);

        return "redirect:/user-dashboard/manage-courses/course" + adminLessonDto.getCourseId() + "/lessons";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/lesson/up")
    private String moveLessonUp(@RequestParam(name = "lessonId") Integer lessonId) {

        //Get lesson to move
        Optional<LessonEntity> lessonEntityOptional = lessonService.findById(lessonId);

        if(lessonEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }

        LessonEntity lessonEntity = lessonEntityOptional.get();

        if(lessonEntity.getOrderNumber() == 1) {
            return "redirect:/user-dashboard/manage-courses/course" + lessonEntity.getCourseId() + "/lessons";
        }

        //Get lesson to switch with
        Optional<LessonEntity> previousLessonEntityOptional =
                lessonService.findByCourseIdAndOrderNumber(lessonEntity.getCourseId(), lessonEntity.getOrderNumber() - 1);

        if(previousLessonEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }

        LessonEntity previousLessonEntity = previousLessonEntityOptional.get();

        //Swap order
        int temp = previousLessonEntity.getOrderNumber();

        previousLessonEntity.setOrderNumber(lessonEntity.getOrderNumber());
        lessonEntity.setOrderNumber(temp);

        lessonService.saveAll(List.of(lessonEntity, previousLessonEntity));

        return "redirect:/user-dashboard/manage-courses/course" + lessonEntity.getCourseId() + "/lessons";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/lesson/down")
    private String moveLessonDown(@RequestParam(name = "lessonId") Integer lessonId) {
        //Get lesson to move
        Optional<LessonEntity> lessonEntityOptional = lessonService.findById(lessonId);

        if(lessonEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }

        LessonEntity lessonEntity = lessonEntityOptional.get();

        //Get the course to know how many lessons there
        Optional<CourseEntity> courseEntityOptional = courseService.getCourseById(lessonEntity.getCourseId());

        if(courseEntityOptional.isEmpty() || (lessonEntity.getOrderNumber() == courseEntityOptional.get().getLessonCount())) {
            return "redirect:/user-dashboard/manage-courses/course" + lessonEntity.getCourseId() + "/lessons";
        }

        //Get lesson to switch with
        Optional<LessonEntity> nextLessonEntityOptional =
                lessonService.findByCourseIdAndOrderNumber(lessonEntity.getCourseId(), lessonEntity.getOrderNumber() + 1);

        if(nextLessonEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }

        LessonEntity nextLessonEntity = nextLessonEntityOptional.get();

        //Swap order
        int temp = nextLessonEntity.getOrderNumber();

        nextLessonEntity.setOrderNumber(lessonEntity.getOrderNumber());
        lessonEntity.setOrderNumber(temp);

        lessonService.saveAll(List.of(lessonEntity, nextLessonEntity));

        return "redirect:/user-dashboard/manage-courses/course" + lessonEntity.getCourseId() + "/lessons";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/lesson/delete")
    private String editLesson(@RequestParam(name = "lessonId") Integer lessonId) {
        //Get lesson to delete
        Optional<LessonEntity> lessonEntityOptional = lessonService.findById(lessonId);

        if(lessonEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }
        LessonEntity lessonEntity = lessonEntityOptional.get();

        List<LessonEntity> lessonEntities = lessonService.findAllByCourseIdOrderByOrderNumber(lessonEntity.getCourseId());

        lessonEntities.removeIf(lesson -> lesson.getId() == lessonEntity.getId());

        //Reset order
        for (int i = 0; i < lessonEntities.size(); i++) {
            LessonEntity lesson = lessonEntities.get(i);
            lesson.setOrderNumber(i + 1);
        }

        lessonService.deleteLessonAndSaveList(lessonEntity, lessonEntities);

        return "redirect:/user-dashboard/manage-courses/course" + lessonEntity.getCourseId() + "/lessons";
    }
}
