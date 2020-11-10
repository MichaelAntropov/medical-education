package me.hizencode.mededu.course.support;

import me.hizencode.mededu.course.content.CourseContentItemProjection;
import me.hizencode.mededu.course.content.CourseContentItemType;
import me.hizencode.mededu.course.content.lesson.LessonEntity;
import me.hizencode.mededu.course.content.lesson.LessonService;
import me.hizencode.mededu.course.content.test.CourseAnswerEntity;
import me.hizencode.mededu.course.content.test.CourseQuestionEntity;
import me.hizencode.mededu.course.content.test.CourseTestEntity;
import me.hizencode.mededu.course.content.test.CourseTestService;
import me.hizencode.mededu.course.content.user.CourseUserEntity;
import me.hizencode.mededu.course.content.user.CourseUserService;
import me.hizencode.mededu.course.content.user.lesson.CourseUserLessonEntity;
import me.hizencode.mededu.course.content.user.test.CourseUserTestEntity;
import me.hizencode.mededu.course.support.dto.*;
import me.hizencode.mededu.user.UserPrincipal;
import me.hizencode.mededu.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CourseContentController {

    /*Fields*/
    /*================================================================================================================*/
    private CourseUserService courseUserService;

    private LessonService lessonService;

    private CourseTestService testService;

    private UserService userService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setCourseUserService(CourseUserService courseUserService) {
        this.courseUserService = courseUserService;
    }

    @Autowired
    public void setLessonService(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Autowired
    public void setTestService(CourseTestService testService) {
        this.testService = testService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /*Methods*/
    /*================================================================================================================*/
    @GetMapping("/courses/course{courseId}/lesson{lessonId}")
    public String showLesson(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                             Authentication authentication, Model model) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        //Check if the user has access to the course
        if (!courseUserService.isUserSignedOnCourse(userPrincipal, courseId)) {
            return "redirect:/courses/course" + courseId;
        }

        //Get content items for user
        List<CourseContentItemDto> courseContentItemsDto =
                getContentItemsDto(userPrincipal.getId(), courseId);

        //Get required lesson to show content
        Optional<LessonEntity> lessonEntityOptional = lessonService.findById(lessonId);

        if (lessonEntityOptional.isEmpty() || lessonEntityOptional.get().getCourse().getId() != courseId) {
            return "redirect:/not-found";
        }

        LessonEntity lessonEntity = lessonEntityOptional.get();

        LessonDto lessonDto = new LessonDto();

        lessonDto.setTitle(lessonEntity.getTitle());
        lessonDto.setContent(lessonEntity.getContent());
        //Set next lesson/test
        if (lessonEntity.getOrderNumber() >= courseContentItemsDto.size()) {
            lessonDto.setNextItemId(0);
            lessonDto.setNextItemType(null);
        } else {
            CourseContentItemDto nextItem = courseContentItemsDto.get(lessonEntity.getOrderNumber());
            lessonDto.setNextItemId(nextItem.getId());
            lessonDto.setNextItemType(nextItem.getType());
        }

        model.addAttribute("courseId", courseId);
        model.addAttribute("items", courseContentItemsDto);
        model.addAttribute("lesson", lessonDto);

        //Set last viewed item
        Optional<CourseUserEntity> courseUserEntity =
                courseUserService.findByUserIdAndCourseId(userPrincipal.getId(), courseId);

        courseUserEntity.ifPresent(userEntity -> {
            userEntity.setLastOrderNumber(lessonEntity.getOrderNumber());
            courseUserService.saveCourseUserEntity(userEntity);
        });

        return "courses/content/lesson";
    }

    @GetMapping("/courses/course{courseId}/test{testId}")
    private String showTest(@PathVariable Integer courseId, @PathVariable Integer testId,
                            Authentication authentication, Model model) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        //Check if the user has access to the course
        if (!courseUserService.isUserSignedOnCourse(userPrincipal, courseId)) {
            return "redirect:/access-denied";
        }

        //Get content items for user
        List<CourseContentItemDto> courseContentItemsDto =
                getContentItemsDto(userPrincipal.getId(), courseId);

        //Get required test to show content
        Optional<CourseTestEntity> courseTestEntityOptional =
                testService.findById(testId);

        //Get user-test record
        Optional<CourseUserTestEntity> courseUserTestEntityOptional =
                courseUserService.findByUserIdAndTestId(userPrincipal.getId(), testId);

        if (courseTestEntityOptional.isEmpty() || courseTestEntityOptional.get().getCourse().getId() != courseId) {
            return "redirect:/not-found";
        }

        CourseTestEntity courseTestEntity = courseTestEntityOptional.get();

        TestDto testDto = new TestDto();

        testDto.setTitle(courseTestEntity.getTitle());
        testDto.setContent(courseTestEntity.getContent());

        //Set user score
        if (courseUserTestEntityOptional.isPresent()) {
            CourseUserTestEntity courseUserTestEntity = courseUserTestEntityOptional.get();
            testDto.setCompleted(courseUserTestEntity.getCompleted());
            testDto.setUserScore(courseUserTestEntity.getScore());
        } else {
            testDto.setCompleted(null);
            testDto.setUserScore(null);
        }

        testDto.setQuestionCount(courseTestEntity.getQuestions().size());
        testDto.setRequiredScore(courseTestEntity.getRequiredScore());

        //Set next lesson/test
        if (courseTestEntity.getOrderNumber() + 1 >= courseContentItemsDto.size()) {
            testDto.setNextItemId(0);
            testDto.setNextItemType(null);
        } else {
            CourseContentItemDto nextItem = courseContentItemsDto.get(courseTestEntity.getOrderNumber());
            testDto.setNextItemId(nextItem.getId());
            testDto.setNextItemType(nextItem.getType());
        }

        model.addAttribute("courseId", courseId);
        model.addAttribute("items", courseContentItemsDto);
        model.addAttribute("test", testDto);

        return "courses/content/test";
    }

    @ResponseBody
    @GetMapping(value = "/courses/course{courseId}/test{testId}/questions", produces = "application/json")
    private QuestionsResponseJson getTestQuestions(@PathVariable Integer courseId, @PathVariable Integer testId,
                                                   Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        //Check if the user has access to the course
        if (!courseUserService.isUserSignedOnCourse(userPrincipal, courseId)) {
            return new QuestionsResponseJson("User is not signed on course");
        }

        //Get test to verify
        Optional<CourseTestEntity> courseTestEntityOptional =
                testService.findById(testId);

        if (courseTestEntityOptional.isEmpty() || courseTestEntityOptional.get().getCourse().getId() != courseId) {
            return new QuestionsResponseJson("Course/test not found");
        }

        CourseTestEntity courseTestEntity = courseTestEntityOptional.get();

        List<QuestionJson> questions = new ArrayList<>();

        courseTestEntity.getQuestions()
                .stream().sorted(Comparator.comparingInt(CourseQuestionEntity::getOrderNumber)).forEach(courseQuestionEntity -> {
            List<AnswerJson> answers = new ArrayList<>();
            courseQuestionEntity.getAnswers()
                    .stream().sorted(Comparator.comparingInt(CourseAnswerEntity::getOrderNumber)).forEach(courseAnswerEntity -> {
                answers.add(new AnswerJson(courseAnswerEntity.getId(), courseAnswerEntity.getContent()));
            });

            QuestionJson questionJson = new QuestionJson(courseQuestionEntity.getId(), courseQuestionEntity.getOrderNumber(),
                    courseQuestionEntity.getContent(), answers);

            questions.add(questionJson);
        });

        return new QuestionsResponseJson("Questions delivered", questions);

    }

    @PostMapping("/courses/course/lesson/complete")
    private String completeLesson(@RequestParam Integer lessonId, Authentication authentication,
                                  @RequestParam Integer nextId, @RequestParam String nextType) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if (userService.userHasRole(authentication, "ADMIN")) {
            return "redirect:/courses";
        }

        Optional<LessonEntity> lessonEntityOptional =
                lessonService.findById(lessonId);

        if (lessonEntityOptional.isEmpty()
                || !courseUserService.isUserSignedOnCourse(userPrincipal, lessonEntityOptional.get().getCourse().getId())) {
            return "redirect:/courses";
        }

        Optional<CourseUserLessonEntity> userLessonEntityOptional =
                courseUserService.findByUserIdAndLessonId(userPrincipal.getId(), lessonId);

        Optional<CourseUserEntity> courseUserEntityOptional =
                courseUserService.findByUserIdAndCourseId(userPrincipal.getId(), lessonEntityOptional.get().getCourse().getId());

        if (userLessonEntityOptional.isPresent() && courseUserEntityOptional.isPresent()) {

            CourseUserLessonEntity courseUserLessonEntity = userLessonEntityOptional.get();

            if (courseUserLessonEntity.getCompleted() != 0) {
                courseUserLessonEntity.setCompleted(1);
            }
            courseUserService.saveCourseUserLessonEntity(courseUserLessonEntity);
        }

        if (nextType.equals(CourseContentItemType.LESSON.value)) {
            return "redirect:/courses/course" + lessonEntityOptional.get().getCourse().getId() + "/lesson" + nextId;
        } else {
            return "redirect:/courses/course" + lessonEntityOptional.get().getCourse().getId() + "/test" + nextId;
        }
    }

    @ResponseBody
    @PostMapping(value = "/courses/course/test/submit", consumes = MediaType.APPLICATION_JSON_VALUE)
    private void acceptSubmittedTest(@RequestBody TestSubmitPostJson testSubmit,
                                     Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if (userService.userHasRole(authentication, "ADMIN")) {
            throw new IllegalStateException("User is not allowed to submit test");
        }

        //Check if the user has access to the course
        if (!courseUserService.isUserSignedOnCourse(userPrincipal, testSubmit.getCourseId())) {
            throw new IllegalStateException("User is not signed on course");
        }

        //Get test to verify
        Optional<CourseTestEntity> courseTestEntityOptional =
                testService.findById(testSubmit.getTestId());

        if (courseTestEntityOptional.isEmpty() || courseTestEntityOptional.get().getCourse().getId() != testSubmit.getCourseId()) {
            throw new IllegalStateException("Course/test not found");
        }

        CourseTestEntity courseTestEntity = courseTestEntityOptional.get();

        Optional<CourseUserTestEntity> courseUserTestEntityOptional =
                courseUserService.findByUserIdAndTestId(userPrincipal.getId(), courseTestEntity.getId());

        if (courseUserTestEntityOptional.isEmpty()) {
            throw new IllegalStateException("User/test record was not found");
        }

        CourseUserTestEntity courseUserTestEntity = courseUserTestEntityOptional.get();

        List<CourseQuestionEntity> courseQuestionEntities = courseTestEntity.getQuestions();

        //Check amount of right answers
        Integer[] userScore = {0};

        Map<Integer, Integer> userAnswersMap = testSubmit.getUserAnswers().stream()
                .collect(Collectors.toMap(UserAnswerJson::getQuestionId, UserAnswerJson::getAnswerId));

        courseQuestionEntities.forEach(courseQuestionEntity -> {
            int questionId = courseQuestionEntity.getId();

            if (userAnswersMap.containsKey(questionId)) {

                Integer userQuestionAnswer = userAnswersMap.get(questionId);

                if (userQuestionAnswer != null) {
                    if (userQuestionAnswer == courseQuestionEntity.getCorrectAnswer().getId()) {
                        userScore[0]++;
                    }
                }
            }
        });

        //Fill the user/test record
        courseUserTestEntity.setScore(userScore[0]);

        if (userScore[0] >= courseTestEntity.getRequiredScore()) {
            courseUserTestEntity.setCompleted(1);
        } else {
            courseUserTestEntity.setCompleted(-1);
        }

        courseUserService.saveCourseUserTestEntity(courseUserTestEntity);
    }

    private List<CourseContentItemDto> getContentItemsDto(int userId, int courseId) {

        List<CourseContentItemDto> courseContentItemsDto = new ArrayList<>();

        //Get lessons and tests, get user progress and combine
        List<CourseContentItemProjection> lessons = lessonService.findAllByCourseIdOnlyIdAndTitleAndOrder(courseId);
        List<CourseContentItemProjection> tests = testService.findAllByCourseIdAndTitleOnly(courseId);

        List<CourseUserLessonEntity> userLessons =
                courseUserService.findAllByUserIdAndLessonIdIn(userId,
                        lessons.stream().map(CourseContentItemProjection::getId).collect(Collectors.toList()));

        List<CourseUserTestEntity> userTests =
                courseUserService.findAllByUserIdAndTestIdIn(userId,
                        tests.stream().map(CourseContentItemProjection::getId).collect(Collectors.toList()));

        //This needed to set the completed property for items
        lessons.forEach(itemProjection -> {
            CourseContentItemDto contentItemDto = new CourseContentItemDto();
            contentItemDto.setId(itemProjection.getId());
            contentItemDto.setType(itemProjection.getType());
            contentItemDto.setOrderNumber(itemProjection.getOrderNumber());
            contentItemDto.setTitle(itemProjection.getTitle());

            //Find user-lesson entity with the same id
            Optional<CourseUserLessonEntity> userLessonEntity = userLessons.stream()
                    .filter(courseUserLessonEntity -> courseUserLessonEntity.getLessonId() == itemProjection.getId()).findFirst();
            //Set value of completed or if not(as for admins) set as 0
            userLessonEntity.ifPresentOrElse(courseUserLessonEntity ->
                            contentItemDto.setCompleted(courseUserLessonEntity.getCompleted()),
                    () -> contentItemDto.setCompleted(0));

            courseContentItemsDto.add(contentItemDto);
        });

        tests.forEach(itemProjection -> {
            CourseContentItemDto contentItemDto = new CourseContentItemDto();
            contentItemDto.setId(itemProjection.getId());
            contentItemDto.setType(itemProjection.getType());
            contentItemDto.setOrderNumber(itemProjection.getOrderNumber());
            contentItemDto.setTitle(itemProjection.getTitle());

            //Find user-test entity with the same id
            Optional<CourseUserTestEntity> userTestEntity = userTests.stream()
                    .filter(courseUserTestEntity -> courseUserTestEntity.getTestId() == itemProjection.getId()).findFirst();
            //Set value of completed or if not(as for admins) set as 0
            userTestEntity.ifPresentOrElse(courseUserTestEntity ->
                            contentItemDto.setCompleted(courseUserTestEntity.getCompleted()),
                    () -> contentItemDto.setCompleted(0));

            courseContentItemsDto.add(contentItemDto);
        });

        courseContentItemsDto.sort(Comparator.comparingInt(CourseContentItemDto::getOrderNumber));

        return courseContentItemsDto;
    }
}
