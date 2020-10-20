package me.hizencode.mededu.dashboard.admin;

import me.hizencode.mededu.course.*;
import me.hizencode.mededu.course.lesson.LessonEntity;
import me.hizencode.mededu.course.lesson.LessonService;
import me.hizencode.mededu.course.test.CourseTestEntity;
import me.hizencode.mededu.course.test.CourseTestService;
import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardAdminCourseItemsController {

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

    /*
    /////////////////////////////////////////////////////////
    Method for learning items
    /////////////////////////////////////////////////////////
     */
    @GetMapping("/user-dashboard/manage-courses/course{courseId}/lessons")
    private String showCourseLessons(@PathVariable(name = "courseId") Integer courseId, Model model) {

        //Check if the course exists if no return to main page
        Optional<CourseEntity> courseEntity = courseService.getCourseById(courseId);

        if (courseEntity.isEmpty()) {
            return "redirect:/user-dashboard/manage-courses";
        }

        //Get lessons and tests and combine
        List<LearningItemProjection> lessons = lessonService.findAllByCourseIdOnlyIdAndTitleAndOrder(courseId);
        List<LearningItemProjection> tests = testService.findAllByCourseIdAndTitleOnly(courseId);

        List<LearningItemProjection> learningItems = new ArrayList<>();
        learningItems.addAll(lessons);
        learningItems.addAll(tests);

        learningItems.sort(Comparator.comparingInt(LearningItemProjection::getOrderNumber));

        model.addAttribute("courseId", courseId);
        model.addAttribute("items", learningItems);
        model.addAttribute("courseName", courseEntity.get().getName());
        model.addAttribute("lessonCount", courseEntity.get().getLessonCount());

        return "user-dashboard/admin/courses/lessons/course-lessons";
    }

    /*
    /////////////////////////////////////////////////////////
    Methods to move lesson/test
    /////////////////////////////////////////////////////////
     */
    @PostMapping("/user-dashboard/manage-courses/course/lessons/item/up")
    private String moveLessonUp(@RequestParam(name = "itemId") Integer itemId,
                                @RequestParam(name = "type") String learningItemType) {

        Triple<LearningItem, LearningItem, CourseEntity> triple
                = getLearningItemsToMove(itemId, learningItemType, -1);

        if (triple == null) {
            return "redirect:/user-dashboard/manage-courses";
        }

        swapWithPrevious(triple.getA(), triple.getB());

        lessonTestService.saveItemsList(List.of(triple.getA(), triple.getB()));

        return "redirect:/user-dashboard/manage-courses/course" + triple.getC().getId() + "/lessons";
    }

    @PostMapping("/user-dashboard/manage-courses/course/lessons/item/down")
    private String moveLessonDown(@RequestParam(name = "itemId") Integer itemId,
                                  @RequestParam(name = "type") String learningItemType) {
        Triple<LearningItem, LearningItem, CourseEntity> triple
                = getLearningItemsToMove(itemId, learningItemType, 1);

        if (triple == null) {
            return "redirect:/user-dashboard/manage-courses";
        }

        swapWithNext(triple.getA(), triple.getB());

        lessonTestService.saveItemsList(List.of(triple.getA(), triple.getB()));

        return "redirect:/user-dashboard/manage-courses/course" + triple.getC().getId() + "/lessons";
    }

    private Triple<LearningItem, LearningItem, CourseEntity> getLearningItemsToMove(Integer itemId, String itemType, Integer orderShift) {
        //Get item to move
        LearningItem learningItem = null;

        if (itemType.equals(LearningItemType.LESSON.type)) {
            Optional<LessonEntity> lessonEntityOptional = lessonService.findById(itemId);
            if (lessonEntityOptional.isEmpty()) {
                return null;
            }
            learningItem = lessonEntityOptional.get();
        }

        if (itemType.equals(LearningItemType.TEST.type)) {
            Optional<CourseTestEntity> testEntityOptional = testService.findById(itemId);
            if (testEntityOptional.isEmpty()) {
                return null;
            }
            learningItem = testEntityOptional.get();
        }

        if (learningItem == null) {
            return null;
        }

        CourseEntity courseEntity = learningItem.getCourse();

        //Check if the item is first/last
        if (orderShift == -1 && learningItem.getOrderNumber() == 1) {
            return null;
        }

        if (orderShift == 1 && learningItem.getOrderNumber() == courseEntity.getLessonCount()) {
            return null;
        }

        //Get lesson to switch with
        LearningItem learningItemSwapWith = null;

        Optional<LessonEntity> lessonEntityOptional =
                lessonService.findByCourseIdAndOrderNumber(courseEntity.getId(), learningItem.getOrderNumber() + orderShift);

        Optional<CourseTestEntity> testEntityOptional =
                testService.findByCourseIdAndOrderNumber(courseEntity.getId(), learningItem.getOrderNumber() + orderShift);

        if (lessonEntityOptional.isPresent()) {
            learningItemSwapWith = lessonEntityOptional.get();
        }
        if (testEntityOptional.isPresent()) {
            learningItemSwapWith = testEntityOptional.get();
        }
        if (learningItemSwapWith == null) {
            return null;
        }

        return Triple.of(learningItem, learningItemSwapWith, courseEntity);
    }

    private void swapWithPrevious(LearningItem learningItem, LearningItem learningItemPrevious) {
        int temp = learningItemPrevious.getOrderNumber();

        learningItemPrevious.setOrderNumber(learningItem.getOrderNumber());
        learningItem.setOrderNumber(temp);
    }

    private void swapWithNext(LearningItem learningItem, LearningItem learningItemNext) {
        int temp = learningItem.getOrderNumber();

        learningItem.setOrderNumber(learningItemNext.getOrderNumber());
        learningItemNext.setOrderNumber(temp);
    }
}
