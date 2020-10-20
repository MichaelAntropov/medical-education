package me.hizencode.mededu.course.test;

import me.hizencode.mededu.course.LearningItemProjection;
import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseTestServiceImpl implements CourseTestService {

    /*Fields*/
    /*================================================================================================================*/
    private CourseTestRepository courseTestRepository;

    private CourseQuestionRepository courseQuestionRepository;

    private CourseAnswerRepository courseAnswerRepository;

    private CourseService courseService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setCourseTestRepository(CourseTestRepository courseTestRepository) {
        this.courseTestRepository = courseTestRepository;
    }

    @Autowired
    public void setCourseQuestionRepository(CourseQuestionRepository courseQuestionRepository) {
        this.courseQuestionRepository = courseQuestionRepository;
    }

    @Autowired
    public void setCourseAnswerRepository(CourseAnswerRepository courseAnswerRepository) {
        this.courseAnswerRepository = courseAnswerRepository;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    /*Methods*/
    /*================================================================================================================*/
    @Override
    public void saveTest(CourseTestEntity courseTestEntity) {
        courseTestRepository.save(courseTestEntity);
    }

    @Override
    @Transactional
    public Integer saveNewTest(CourseEntity courseEntity, CourseTestEntity courseTestEntity) {
        CourseTestEntity savedTest = courseTestRepository.save(courseTestEntity);
        courseService.saveCourse(courseEntity);

        return savedTest.getId();
    }

    @Override
    public Optional<CourseTestEntity> findById(Integer testId) {
        return courseTestRepository.findById(testId);
    }

    @Override
    public Optional<CourseTestEntity> findByCourseIdAndOrderNumber(Integer testId, Integer orderNumber) {
        return courseTestRepository.findByCourseIdAndOrderNumber(testId, orderNumber);
    }

    @Override
    public List<CourseTestEntity> findAllByCourseId(int courseId) {
        return courseTestRepository.findAllByCourseId(courseId, CourseTestEntity.class);
    }

    @Override
    public List<LearningItemProjection> findAllByCourseIdAndTitleOnly(int courseId) {
        return courseTestRepository.findAllByCourseId(courseId, LearningItemProjection.class);
    }

    @Override
    public void saveAll(List<CourseTestEntity> courseTestEntities) {
        courseTestRepository.saveAll(courseTestEntities);
    }

    @Override
    @Transactional
    public void saveQuestions(List<CourseQuestionEntity> courseQuestionEntities, List<Integer> questionsToDelete, List<Integer> answersToDelete) {
        List<CourseQuestionEntity> questionEntities = courseQuestionRepository.findAllById(questionsToDelete);
        List<CourseAnswerEntity> courseAnswerEntities = courseAnswerRepository.findAllById(answersToDelete);

        courseQuestionRepository.deleteAll(questionEntities);
        courseAnswerRepository.deleteAll(courseAnswerEntities);

        saveAllQuestions(courseQuestionEntities);
    }

    @Transactional
    public void saveAllQuestions(List<CourseQuestionEntity> courseQuestionEntities) {

        courseQuestionEntities.forEach(courseQuestionEntity -> {
            CourseAnswerEntity correctAnswer = courseQuestionEntity.getCorrectAnswer();
            courseQuestionEntity.getAnswers().remove(correctAnswer);
            courseQuestionEntity.setCorrectAnswer(null);

            CourseQuestionEntity savedQuestion = courseQuestionRepository.save(courseQuestionEntity);

            courseQuestionEntity.getAnswers().forEach(courseAnswerEntity -> {
                courseAnswerEntity.setQuestion(savedQuestion);
            });
            courseAnswerRepository.saveAll(courseQuestionEntity.getAnswers());

            correctAnswer.setQuestion(savedQuestion);
            CourseAnswerEntity savedAnswer = courseAnswerRepository.save(correctAnswer);
            savedQuestion.setCorrectAnswer(savedAnswer);
            courseQuestionRepository.save(savedQuestion);
        });
    }

    @Override
    public void deleteCourseTest(CourseTestEntity courseTestEntity) {
        courseTestRepository.delete(courseTestEntity);
    }
}
