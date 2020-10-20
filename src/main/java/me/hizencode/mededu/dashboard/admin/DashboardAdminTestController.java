package me.hizencode.mededu.dashboard.admin;

import me.hizencode.mededu.course.CourseLessonTestService;
import me.hizencode.mededu.course.LearningItem;
import me.hizencode.mededu.course.LearningItemType;
import me.hizencode.mededu.course.lesson.LessonEntity;
import me.hizencode.mededu.course.lesson.LessonService;
import me.hizencode.mededu.course.test.CourseAnswerEntity;
import me.hizencode.mededu.course.test.CourseQuestionEntity;
import me.hizencode.mededu.course.test.CourseTestEntity;
import me.hizencode.mededu.course.test.CourseTestService;
import me.hizencode.mededu.course.test.media.CourseTestMediaEntity;
import me.hizencode.mededu.course.test.media.CourseTestMediaService;
import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import me.hizencode.mededu.dashboard.admin.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
public class DashboardAdminTestController {

    /*Fields*/
    /*================================================================================================================*/
    private LessonService lessonService;

    private CourseTestService testService;

    private CourseTestMediaService testMediaService;

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
    public void setTestMediaService(CourseTestMediaService testMediaService) {
        this.testMediaService = testMediaService;
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

    @ResponseBody
    @GetMapping("/user-dashboard/manage-courses/course{courseId}/lessons/test{testId}/images")
    public MediaResponseJson testGetImages(@PathVariable Integer courseId,
                                           @PathVariable Integer testId) {

        Optional<CourseTestEntity> testEntity = testService.findById(testId);

        if (testEntity.isEmpty() || testEntity.get().getCourse().getId() != courseId) {
            return new MediaResponseJson("Could not receive images. Incorrect data.");
        }

        List<MediaItemJson> mediaItemJsonList = new ArrayList<>();
        List<CourseTestMediaEntity> mediaEntityList = testEntity.get().getMedia();
        mediaEntityList.forEach(courseTestMediaEntity -> {
            MediaItemJson mediaItemJson = new MediaItemJson(courseTestMediaEntity);
            mediaItemJsonList.add(mediaItemJson);
        });

        return new MediaResponseJson("Images were successfully requested.", mediaItemJsonList);
    }

    @ResponseBody
    @PostMapping("/user-dashboard/manage-courses/course/lessons/test/image/upload")
    public MediaResponseJson testUploadImage(@RequestParam Integer testId,
                                       @RequestParam("image") MultipartFile image) {

        Optional<CourseTestEntity> testEntity = testService.findById(testId);

        if (testEntity.isEmpty()) {
            return new MediaResponseJson("Could not upload image. Incorrect data.");
        }

        CourseTestMediaEntity courseTestMediaEntity = null;
        //Get and save image
        if (!image.isEmpty()) {
            CourseTestMediaEntity mediaEntity = new CourseTestMediaEntity();
            mediaEntity.setName(image.getOriginalFilename());
            //TODO: Check type of the file
            mediaEntity.setType(image.getContentType());
            try {
                mediaEntity.setData(image.getInputStream().readAllBytes());
                mediaEntity.setTest(testEntity.get());

                courseTestMediaEntity = testMediaService.saveAndReturn(mediaEntity);
            } catch (IOException e) {
                e.printStackTrace();
                return new MediaResponseJson("Could not upload image. Internal server error.");
            }
        } else {
            return new MediaResponseJson("Could not upload image. Image file is empty.");
        }

        MediaItemJson mediaItemJson = new MediaItemJson(courseTestMediaEntity);

        return new MediaResponseJson("Image was successfully uploaded.", List.of(mediaItemJson));
    }

    @ResponseBody
    @PostMapping("/user-dashboard/manage-courses/course/lessons/test/image/delete")
    public MediaResponseJson testDeleteImage(@RequestParam Integer imageId) {

        Optional<CourseTestMediaEntity> mediaEntity = testMediaService.findById(imageId);

        if(mediaEntity.isEmpty()) {
            return new MediaResponseJson("Could not delete image. Incorrect data.");
        }

        testMediaService.delete(mediaEntity.get());

        return new MediaResponseJson("Image was successfully deleted.");
    }

    @ResponseBody
    @GetMapping("/user-dashboard/manage-courses/course{courseId}/lessons/test{testId}/test-data")
    public TestDataResponseJson testGetTestData(@PathVariable Integer courseId,
                                                @PathVariable Integer testId) {

        Optional<CourseTestEntity> testEntity = testService.findById(testId);

        if (testEntity.isEmpty() || testEntity.get().getCourse().getId() != courseId) {
            return new TestDataResponseJson("Could not receive the test data. Incorrect data.");
        }

        List<CourseQuestionEntity> questionEntities = testEntity.get().getQuestions();
        List<QuestionJson> questionsJsonList = new ArrayList<>();
        questionEntities.forEach(courseQuestionEntity -> {

            int correctAnswer = courseQuestionEntity.getCorrectAnswer().getId();
            List<CourseAnswerEntity> courseAnswerEntityList = courseQuestionEntity.getAnswers();

            QuestionJson questionJson = questionEntityToJson(courseQuestionEntity,
                    testId, correctAnswer, courseAnswerEntityList);

            questionsJsonList.add(questionJson);

        });

        return new TestDataResponseJson("Test data were successfully requested.", questionsJsonList);
    }

    @ResponseBody
    @PostMapping(value = "/user-dashboard/manage-courses/course/lessons/test/test-data/post",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public TestDataResponseJson testPostTestData(@RequestBody TestDataPostRequestJson testData) {

        Optional<CourseTestEntity> testEntity = testService.findById(testData.getTestId());

        if (testEntity.isEmpty() || testEntity.get().getId() != testData.getTestId()) {
            return new TestDataResponseJson("Could not update the test data. Incorrect data.");
        }

        List<CourseQuestionEntity> courseQuestionEntities = testEntity.get().getQuestions();

        courseQuestionEntities.removeIf(courseQuestionEntity ->
                testData.getQuestionsToDelete().contains(courseQuestionEntity.getId()));

        courseQuestionEntities.forEach(courseQuestionEntity ->
                courseQuestionEntity.getAnswers()
                        .removeIf(courseAnswerEntity ->
                                testData.getAnswersToDelete().contains(courseAnswerEntity.getId())
        ));

        updateCourseQuestionList(testEntity.get(), courseQuestionEntities, testData.getQuestions());

        testService.saveQuestions(courseQuestionEntities, testData.getQuestionsToDelete(), testData.getAnswersToDelete());

        return new TestDataResponseJson("Test data was updated.");
    }

    private void updateCourseQuestionList(CourseTestEntity courseTestEntity,
                                          List<CourseQuestionEntity> courseQuestionEntities,
                                          List<QuestionJson> questionJsonList) {
        questionJsonList.forEach(questionJson -> {
            if(questionJson.getAnswers().size() < 1) {
                throw  new IllegalStateException("Invalid data. Question should have at least one answer.");
            }
            if(questionJson.getId() != null) {
                Optional<CourseQuestionEntity> questionEntityOptional =  courseQuestionEntities.stream()
                        .filter(courseQuestionEntity -> courseQuestionEntity.getId() == questionJson.getId())
                        .findFirst();

                if(questionEntityOptional.isPresent()) {
                    CourseQuestionEntity questionEntity = questionEntityOptional.get();
                    questionEntity.setOrderNumber(questionJson.getOrderNumber());
                    questionEntity.setContent(questionJson.getContent());
                    CourseAnswerEntity correctAnswer =
                            updateCourseQuestionAnswerList(questionEntity, questionEntity.getAnswers(), questionJson.getAnswers());
                    questionEntity.setCorrectAnswer(correctAnswer);
                }
            }

            if(questionJson.getId() == null) {
                CourseQuestionEntity questionEntity = new CourseQuestionEntity();
                questionEntity.setTest(courseTestEntity);
                questionEntity.setAnswers(new ArrayList<>());
                CourseAnswerEntity correctAnswer =
                        updateCourseQuestionAnswerList(questionEntity, questionEntity.getAnswers(), questionJson.getAnswers());
                questionEntity.setCorrectAnswer(correctAnswer);
                questionEntity.setOrderNumber(questionJson.getOrderNumber());
                questionEntity.setContent(questionJson.getContent());
                courseQuestionEntities.add(questionEntity);
            }
        });
    }

    private CourseAnswerEntity updateCourseQuestionAnswerList(CourseQuestionEntity courseQuestionEntity,
                                                              List<CourseAnswerEntity> courseAnswerEntities,
                                                              List<AnswerJson> answerJsonList) {
        final CourseAnswerEntity[] correctAnswer = {null};

        answerJsonList.forEach(answerJson -> {
            //If exists - modify
            if(answerJson.getId() != null) {
                Optional<CourseAnswerEntity> answerEntityOptional = courseAnswerEntities.stream()
                        .filter(courseAnswerEntity ->  courseAnswerEntity.getId() == answerJson.getId())
                        .findFirst();

                if(answerEntityOptional.isPresent()) {
                    CourseAnswerEntity courseAnswerEntity = answerEntityOptional.get();
                    courseAnswerEntity.setOrderNumber(answerJson.getOrderNumber());
                    courseAnswerEntity.setContent(answerJson.getContent());
                    courseAnswerEntity.setQuestion(courseQuestionEntity);
                    if(answerJson.isCorrect()) {
                        correctAnswer[0] = courseAnswerEntity;
                    }
                }
            }
            //If not - add new
            if(answerJson.getId() == null) {
                CourseAnswerEntity courseAnswerEntity = new CourseAnswerEntity();
                courseAnswerEntity.setQuestion(courseQuestionEntity);
                courseAnswerEntity.setOrderNumber(answerJson.getOrderNumber());
                courseAnswerEntity.setContent(answerJson.getContent());
                if(answerJson.isCorrect()) {
                    correctAnswer[0] = courseAnswerEntity;
                }
                courseAnswerEntities.add(courseAnswerEntity);
            }
        });

        if(correctAnswer[0] == null) {
            throw new IllegalStateException("Invalid data. At least one answer should be a correct one.");
        }

        return correctAnswer[0];
    }

    private QuestionJson questionEntityToJson(CourseQuestionEntity questionEntity,
                                              int testId,
                                              int correctAnswer,
                                              List<CourseAnswerEntity> answerEntities) {

        QuestionJson questionJson = new QuestionJson();

        questionJson.setId(questionEntity.getId());
        questionJson.setTestId(testId);
        questionJson.setOrderNumber(questionEntity.getOrderNumber());
        questionJson.setEdited(false);
        questionJson.setContent(questionEntity.getContent());

        List<AnswerJson> answerJsonList = new ArrayList<>();
        answerEntities.forEach(courseAnswerEntity -> {
            AnswerJson answerJson = answerEntityToJson(courseAnswerEntity, questionEntity.getId());
            if(answerJson.getId() == correctAnswer) {
                answerJson.setCorrect(true);
            }
            answerJsonList.add(answerJson);
        });
        questionJson.setAnswers(answerJsonList);

        return questionJson;
    }

    private AnswerJson answerEntityToJson(CourseAnswerEntity answerEntity, int questionId) {
        AnswerJson answerJson = new AnswerJson();

        answerJson.setId(answerEntity.getId());
        answerJson.setQuestionId(questionId);
        answerJson.setOrderNumber(answerEntity.getOrderNumber());
        answerJson.setCorrect(false);
        answerJson.setEdited(false);
        answerJson.setContent(answerEntity.getContent());

        return answerJson;
    }
}
