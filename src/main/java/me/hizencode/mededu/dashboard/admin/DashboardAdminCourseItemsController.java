package me.hizencode.mededu.dashboard.admin;

import me.hizencode.mededu.course.content.*;
import me.hizencode.mededu.course.content.lesson.LessonEntity;
import me.hizencode.mededu.course.content.lesson.LessonService;
import me.hizencode.mededu.course.content.test.CourseTestEntity;
import me.hizencode.mededu.course.content.test.CourseTestService;
import me.hizencode.mededu.course.CourseEntity;
import me.hizencode.mededu.course.CourseService;
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
        List<CourseContentItemProjection> lessons = lessonService.findAllByCourseIdOnlyIdAndTitleAndOrder(courseId);
        List<CourseContentItemProjection> tests = testService.findAllByCourseIdAndTitleOnly(courseId);

        List<CourseContentItemProjection> courseContentItems = new ArrayList<>();
        courseContentItems.addAll(lessons);
        courseContentItems.addAll(tests);

        courseContentItems.sort(Comparator.comparingInt(CourseContentItemProjection::getOrderNumber));

        model.addAttribute("courseId", courseId);
        model.addAttribute("items", courseContentItems);
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

        Triple<CourseContentItem, CourseContentItem, CourseEntity> triple
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
        Triple<CourseContentItem, CourseContentItem, CourseEntity> triple
                = getLearningItemsToMove(itemId, learningItemType, 1);

        if (triple == null) {
            return "redirect:/user-dashboard/manage-courses";
        }

        swapWithNext(triple.getA(), triple.getB());

        lessonTestService.saveItemsList(List.of(triple.getA(), triple.getB()));

        return "redirect:/user-dashboard/manage-courses/course" + triple.getC().getId() + "/lessons";
    }

    private Triple<CourseContentItem, CourseContentItem, CourseEntity> getLearningItemsToMove(Integer itemId, String itemType, Integer orderShift) {
        //Get item to move
        CourseContentItem courseContentItem = null;

        if (itemType.equals(CourseContentItemType.LESSON.value)) {
            Optional<LessonEntity> lessonEntityOptional = lessonService.findById(itemId);
            if (lessonEntityOptional.isEmpty()) {
                return null;
            }
            courseContentItem = lessonEntityOptional.get();
        }

        if (itemType.equals(CourseContentItemType.TEST.value)) {
            Optional<CourseTestEntity> testEntityOptional = testService.findById(itemId);
            if (testEntityOptional.isEmpty()) {
                return null;
            }
            courseContentItem = testEntityOptional.get();
        }

        if (courseContentItem == null) {
            return null;
        }

        CourseEntity courseEntity = courseContentItem.getCourse();

        //Check if the item is first/last
        if (orderShift == -1 && courseContentItem.getOrderNumber() == 1) {
            return null;
        }

        if (orderShift == 1 && courseContentItem.getOrderNumber() == courseEntity.getLessonCount()) {
            return null;
        }

        //Get lesson to switch with
        CourseContentItem courseContentItemSwapWith = null;

        Optional<LessonEntity> lessonEntityOptional =
                lessonService.findByCourseIdAndOrderNumber(courseEntity.getId(), courseContentItem.getOrderNumber() + orderShift);

        Optional<CourseTestEntity> testEntityOptional =
                testService.findByCourseIdAndOrderNumber(courseEntity.getId(), courseContentItem.getOrderNumber() + orderShift);

        if (lessonEntityOptional.isPresent()) {
            courseContentItemSwapWith = lessonEntityOptional.get();
        }
        if (testEntityOptional.isPresent()) {
            courseContentItemSwapWith = testEntityOptional.get();
        }
        if (courseContentItemSwapWith == null) {
            return null;
        }

        return Triple.of(courseContentItem, courseContentItemSwapWith, courseEntity);
    }

    private void swapWithPrevious(CourseContentItem courseContentItem, CourseContentItem courseContentItemPrevious) {
        int temp = courseContentItemPrevious.getOrderNumber();

        courseContentItemPrevious.setOrderNumber(courseContentItem.getOrderNumber());
        courseContentItem.setOrderNumber(temp);
    }

    private void swapWithNext(CourseContentItem courseContentItem, CourseContentItem courseContentItemNext) {
        int temp = courseContentItem.getOrderNumber();

        courseContentItem.setOrderNumber(courseContentItemNext.getOrderNumber());
        courseContentItemNext.setOrderNumber(temp);
    }
}
