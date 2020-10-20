package me.hizencode.mededu.dashboard.admin;

import me.hizencode.mededu.course.CourseLessonTestService;
import me.hizencode.mededu.course.LearningItem;
import me.hizencode.mededu.course.LearningItemType;
import me.hizencode.mededu.course.lesson.LessonEntity;
import me.hizencode.mededu.course.lesson.LessonService;
import me.hizencode.mededu.course.lesson.media.LessonMediaEntity;
import me.hizencode.mededu.course.lesson.media.LessonMediaService;
import me.hizencode.mededu.course.test.CourseTestEntity;
import me.hizencode.mededu.course.test.CourseTestService;
import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import me.hizencode.mededu.dashboard.admin.dto.AdminLessonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardAdminLessonController {

    /*Fields*/
    /*================================================================================================================*/
    private LessonService lessonService;

    private CourseTestService testService;

    private CourseService courseService;

    private LessonMediaService lessonMediaService;

    private CourseLessonTestService lessonTestService;

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

    @Autowired
    public void setLessonMediaService(LessonMediaService lessonMediaService) {
        this.lessonMediaService = lessonMediaService;
    }

    @Autowired
    public void setTestService(CourseTestService testService) {
        this.testService = testService;
    }

    @Autowired
    public void setLessonTestService(CourseLessonTestService lessonTestService) {
        this.lessonTestService = lessonTestService;
    }

    /*Methods*/
    /*================================================================================================================*/

    @GetMapping("/user-dashboard/manage-courses/course{courseId}/lessons/lesson/create")
    private String createLesson(@PathVariable(name = "courseId") Integer courseId, Model model) {

        AdminLessonDto adminLessonDto = new AdminLessonDto();

        adminLessonDto.setCourseId(courseId);

        model.addAttribute("lesson", adminLessonDto);

        return "user-dashboard/admin/courses/lessons/create-lesson";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/lesson/create/save")
    private String saveCreatedLesson(@ModelAttribute(name = "lesson") AdminLessonDto adminLessonDto) {

        Optional<CourseEntity> courseEntityOptional = courseService.getCourseById(adminLessonDto.getCourseId());

        if (courseEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses/course" + adminLessonDto.getCourseId() + "/lessons";
        }

        CourseEntity courseEntity = courseEntityOptional.get();

        LessonEntity lessonEntity = new LessonEntity();

        lessonEntity.setTitle(adminLessonDto.getTitle());
        lessonEntity.setCourse(courseEntity);

        //Set order and update course
        lessonEntity.setOrderNumber(courseEntity.getLessonCount() + 1);
        courseEntity.setLessonCount(courseEntity.getLessonCount() + 1);
        int lessonId = lessonService.saveNewLesson(courseEntity, lessonEntity);

        return "redirect:/user-dashboard/manage-courses/course" + courseEntity.getId() + "/lessons/lesson" + lessonId + "/edit";
    }

    @GetMapping("/user-dashboard/manage-courses/course{courseId}/lessons/lesson{lessonId}/edit")
    private String editLesson(@PathVariable(name = "courseId") Integer courseId,
                              @PathVariable(name = "lessonId") Integer lessonId,
                              Model model) {

        Optional<LessonEntity> lessonEntityOptional = lessonService.findById(lessonId);

        if (lessonEntityOptional.isEmpty() || (lessonEntityOptional.get().getCourse().getId() != courseId)) {
            return "redirect:/user-dashboard/manage-courses";
        }

        LessonEntity lessonEntity = lessonEntityOptional.get();

        AdminLessonDto adminLessonDto = new AdminLessonDto();

        adminLessonDto.setId(lessonEntity.getId());
        adminLessonDto.setCourseId(lessonEntity.getCourse().getId());
        adminLessonDto.setTitle(lessonEntity.getTitle());
        adminLessonDto.setContent(lessonEntity.getContent());

        List<LessonMediaEntity> mediaEntityList = lessonMediaService.findAllByLessonId(lessonEntity.getId());
        adminLessonDto.setMediaList(mediaEntityList);

        model.addAttribute("lesson", adminLessonDto);

        return "user-dashboard/admin/courses/lessons/edit-lesson";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/lesson/edit/save")
    private String saveEditedLesson(@ModelAttribute(name = "lesson") AdminLessonDto adminLessonDto) {

        Optional<LessonEntity> lessonEntityOptional = lessonService.findById(adminLessonDto.getId());

        if (lessonEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses/course" + adminLessonDto.getCourseId() + "/lessons";
        }

        LessonEntity lessonEntity = lessonEntityOptional.get();

        lessonEntity.setTitle(adminLessonDto.getTitle());
        lessonEntity.setContent(adminLessonDto.getContent());

        lessonService.saveLesson(lessonEntity);

        return "redirect:/user-dashboard/manage-courses/course" + adminLessonDto.getCourseId() + "/lessons";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/lesson/delete")
    private String deleteLesson(@RequestParam(name = "lessonId") Integer lessonId) {
        //Get lesson to delete
        Optional<LessonEntity> lessonEntityOptional = lessonService.findById(lessonId);

        if (lessonEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }
        LessonEntity lessonEntity = lessonEntityOptional.get();
        //Get the course to change amount of lessons
        CourseEntity courseEntity = lessonEntity.getCourse();

        List<LessonEntity> lessonEntities = lessonService.findAllByCourseId(courseEntity.getId());
        List<CourseTestEntity> testEntities = testService.findAllByCourseId(courseEntity.getId());


        List<LearningItem> items = new ArrayList<>();

        items.addAll(lessonEntities);
        items.addAll(testEntities);

        items.removeIf(lesson -> lesson.getId() == lessonEntity.getId()
                && lesson.getType() == LearningItemType.LESSON);

        //Reset order for lessons and tests
        for (int i = 0; i < items.size(); i++) {
            LearningItem item = items.get(i);
            item.setOrderNumber(i + 1);
        }
        //Reset amount of lessons in the course
        courseEntity.setLessonCount(courseEntity.getLessonCount() - 1);

        //Save changes
        lessonTestService.deleteLessonAndSaveItemsList(courseEntity, lessonEntity, items);

        return "redirect:/user-dashboard/manage-courses/course" + courseEntity.getId() + "/lessons";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/lesson/upload/media/image")
    private String uploadImage(@RequestParam(name = "courseId") Integer courseId,
                               @RequestParam(name = "lessonId") Integer lessonId,
                               @RequestParam("image") MultipartFile image) {

        Optional<LessonEntity> lessonEntity = lessonService.findById(lessonId);

        if (lessonEntity.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses/course" + courseId + "/lessons";
        }

        //Get and save image
        if (!image.isEmpty()) {
            LessonMediaEntity mediaEntity = new LessonMediaEntity();
            mediaEntity.setName(image.getOriginalFilename());
            //TODO: Check type of the file
            mediaEntity.setType(image.getContentType());
            try {
                mediaEntity.setData(image.getInputStream().readAllBytes());
                mediaEntity.setLesson(lessonEntity.get());

                lessonMediaService.save(mediaEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/user-dashboard/manage-courses/course" + courseId + "/lessons/lesson" + lessonId + "/edit";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/lesson/upload/media/pdf")
    private String uploadPdf(@RequestParam(name = "courseId") Integer courseId,
                             @RequestParam(name = "lessonId") Integer lessonId,
                             @RequestParam("pdfFile") MultipartFile pdfFile) {

        Optional<LessonEntity> lessonEntity = lessonService.findById(lessonId);

        if (lessonEntity.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses/course" + courseId + "/lessons";
        }

        //Get and save image
        if (!pdfFile.isEmpty()) {
            LessonMediaEntity mediaEntity = new LessonMediaEntity();
            mediaEntity.setName(pdfFile.getOriginalFilename());
            //TODO: Check type of the file
            mediaEntity.setType("application/pdf");
            try {
                mediaEntity.setData(pdfFile.getInputStream().readAllBytes());
                mediaEntity.setLesson(lessonEntity.get());

                lessonMediaService.save(mediaEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/user-dashboard/manage-courses/course" + courseId + "/lessons/lesson" + lessonId + "/edit";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/lesson/delete/media")
    private String deleteMedia(@RequestParam(name = "courseId") Integer courseId,
                               @RequestParam(name = "lessonId") Integer lessonId,
                               @RequestParam(name = "mediaId") Integer mediaId) {

        Optional<LessonMediaEntity> mediaEntity = lessonMediaService.findById(mediaId);

        if (mediaEntity.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses/course" + courseId + "/lessons";
        }

        lessonMediaService.delete(mediaEntity.get());

        return "redirect:/user-dashboard/manage-courses/course" + courseId + "/lessons/lesson" + lessonId + "/edit";
    }
}
