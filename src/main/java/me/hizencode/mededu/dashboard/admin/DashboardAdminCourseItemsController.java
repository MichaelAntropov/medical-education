package me.hizencode.mededu.dashboard.admin;

import me.hizencode.mededu.course.*;
import me.hizencode.mededu.course.lesson.LessonEntity;
import me.hizencode.mededu.course.lesson.LessonService;
import me.hizencode.mededu.course.lesson.media.LessonMediaEntity;
import me.hizencode.mededu.course.lesson.media.LessonMediaService;
import me.hizencode.mededu.course.test.CourseTestEntity;
import me.hizencode.mededu.course.test.CourseTestService;
import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import me.hizencode.mededu.dashboard.admin.dto.AdminCourseTestDto;
import me.hizencode.mededu.dashboard.admin.dto.AdminLessonDto;
import me.hizencode.mededu.dashboard.admin.dto.AdminTestMediaJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    Methods for lessons
    /////////////////////////////////////////////////////////
     */
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

    /*
    /////////////////////////////////////////////////////////
    Methods for tests
    /////////////////////////////////////////////////////////
     */

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

    @ResponseBody
    @GetMapping("/user-dashboard/manage-courses/course{courseId}/lessons/test{testId}/media")
    private AdminTestMediaJson getAllTestMedia(@PathVariable(name = "courseId") Integer courseId,
                                               @PathVariable(name = "testId") Integer testId) {

        return new AdminTestMediaJson("Test text");
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
