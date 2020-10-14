package me.hizencode.mededu.dashboard.admin;

import me.hizencode.mededu.course.CourseLessonTestService;
import me.hizencode.mededu.course.LearningItem;
import me.hizencode.mededu.course.LearningItemType;
import me.hizencode.mededu.course.lesson.LessonEntity;
import me.hizencode.mededu.course.lesson.LessonService;
import me.hizencode.mededu.course.test.CourseTestEntity;
import me.hizencode.mededu.course.test.CourseTestService;
import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import me.hizencode.mededu.dashboard.admin.dto.AdminCourseTestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardAdminTestController {

    /*Fields*/
    /*================================================================================================================*/
    private LessonService lessonService;

    private CourseTestService testService;

    private CourseService courseService;

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
    public void setTestService(CourseTestService testService) {
        this.testService = testService;
    }

    @Autowired
    public void setLessonTestService(CourseLessonTestService lessonTestService) {
        this.lessonTestService = lessonTestService;
    }

    /*Methods*/
    /*================================================================================================================*/

    @GetMapping("/user-dashboard/manage-courses/course{courseId}/lessons/test/create")
    private String createTest(@PathVariable(name = "courseId") Integer courseId, Model model) {

        AdminCourseTestDto adminTestDto = new AdminCourseTestDto();

        adminTestDto.setCourseId(courseId);

        model.addAttribute("test", adminTestDto);

        return "user-dashboard/admin/courses/tests/create-test";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/test/create/save")
    private String saveCreatedTest(@ModelAttribute(name = "test") AdminCourseTestDto adminCourseTestDto) {

        Optional<CourseEntity> courseEntityOptional = courseService.getCourseById(adminCourseTestDto.getCourseId());

        if (courseEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }

        CourseEntity courseEntity = courseEntityOptional.get();

        CourseTestEntity courseTestEntity = new CourseTestEntity();

        courseTestEntity.setCourse(courseEntity);
        courseTestEntity.setTitle(adminCourseTestDto.getTitle());

        //Set order and update course
        courseTestEntity.setOrderNumber(courseEntity.getLessonCount() + 1);
        courseEntity.setLessonCount(courseEntity.getLessonCount() + 1);
        int testId = testService.saveNewTest(courseEntity, courseTestEntity);

        return "redirect:/user-dashboard/manage-courses/course" + courseEntity.getId() + "/lessons/test" + testId + "/edit";
    }

    @GetMapping("/user-dashboard/manage-courses/course{courseId}/lessons/test{testId}/edit")
    private String editTest(@PathVariable(name = "courseId") Integer courseId,
                            @PathVariable(name = "testId") Integer testId,
                            Model model) {

        Optional<CourseTestEntity> optionalCourseTestEntity = testService.findById(testId);

        if (optionalCourseTestEntity.isEmpty() || (optionalCourseTestEntity.get().getCourse().getId() != courseId)) {
            return "redirect:/user-dashboard/manage-courses";
        }

        CourseTestEntity courseTestEntity = optionalCourseTestEntity.get();

        AdminCourseTestDto adminCourseTestDto = new AdminCourseTestDto();

        adminCourseTestDto.setId(courseTestEntity.getId());
        adminCourseTestDto.setCourseId(courseTestEntity.getCourse().getId());
        adminCourseTestDto.setTitle(courseTestEntity.getTitle());
        adminCourseTestDto.setContent(courseTestEntity.getContent());

        model.addAttribute("test", adminCourseTestDto);

        return "user-dashboard/admin/courses/tests/edit-test";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/test/edit/save")
    private String saveEditedTest(@ModelAttribute(name = "test") AdminCourseTestDto adminCourseTestDto) {

        Optional<CourseTestEntity> courseTestEntityOptional
                = testService.findById(adminCourseTestDto.getId());

        if (courseTestEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses/course" + adminCourseTestDto.getCourseId() + "/lessons";
        }

        CourseTestEntity testEntity = courseTestEntityOptional.get();

        testEntity.setTitle(adminCourseTestDto.getTitle());
        testEntity.setContent(adminCourseTestDto.getContent());

        testService.saveTest(testEntity);

        return "redirect:/user-dashboard/manage-courses/course" + adminCourseTestDto.getCourseId() + "/lessons";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/test/delete")
    private String deleteTest(@RequestParam(name = "testId") Integer lessonId) {
        //Get lesson to delete
        Optional<CourseTestEntity> testEntityOptional = testService.findById(lessonId);

        if (testEntityOptional.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }
        CourseTestEntity testEntity = testEntityOptional.get();
        //Get the course to change amount of lessons
        CourseEntity courseEntity = testEntity.getCourse();

        List<LessonEntity> lessonEntities = lessonService.findAllByCourseId(courseEntity.getId());
        List<CourseTestEntity> testEntities = testService.findAllByCourseId(courseEntity.getId());


        List<LearningItem> items = new ArrayList<>();

        items.addAll(lessonEntities);
        items.addAll(testEntities);

        items.removeIf(test -> test.getId() == testEntity.getId()
                && test.getType() == LearningItemType.TEST);

        //Reset order for lessons and tests
        for (int i = 0; i < items.size(); i++) {
            LearningItem item = items.get(i);
            item.setOrderNumber(i + 1);
        }
        //Reset amount of lessons in the course
        courseEntity.setLessonCount(courseEntity.getLessonCount() - 1);

        //Save changes
        lessonTestService.deleteTestAndSaveItemsList(courseEntity, testEntity, items);

        return "redirect:/user-dashboard/manage-courses/course" + courseEntity.getId() + "/lessons";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/test/upload/image")
    public void testPostMethod(@RequestParam Integer courseId,
                               @RequestParam Integer testId,
                               @RequestParam("image") MultipartFile image) {

        System.out.println("////////////////////////////////////////////////////////////////////////////");
        System.out.println(courseId);
        System.out.println(testId);
        if(!image.isEmpty()) {
            try {
                System.out.println(Arrays.toString(image.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
