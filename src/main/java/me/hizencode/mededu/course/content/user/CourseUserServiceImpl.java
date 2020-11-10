package me.hizencode.mededu.course.content.user;

import me.hizencode.mededu.course.CourseEntity;
import me.hizencode.mededu.course.CourseRepository;
import me.hizencode.mededu.course.content.CourseContentItemType;
import me.hizencode.mededu.course.content.lesson.LessonEntity;
import me.hizencode.mededu.course.content.lesson.LessonService;
import me.hizencode.mededu.course.content.test.CourseTestEntity;
import me.hizencode.mededu.course.content.test.CourseTestService;
import me.hizencode.mededu.course.content.user.lesson.CourseUserLessonEntity;
import me.hizencode.mededu.course.content.user.lesson.CourseUserLessonRepository;
import me.hizencode.mededu.course.content.user.test.CourseUserTestEntity;
import me.hizencode.mededu.course.content.user.test.CourseUserTestRepository;
import me.hizencode.mededu.user.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    /*Fields*/
    /*================================================================================================================*/
    private CourseUserRepository courseUserRepository;

    private CourseUserLessonRepository courseUserLessonRepository;

    private CourseUserTestRepository courseUserTestRepository;

    private LessonService lessonService;

    private CourseTestService testService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setCourseUserRepository(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }

    @Autowired
    public void setCourseUserLessonRepository(CourseUserLessonRepository courseUserLessonRepository) {
        this.courseUserLessonRepository = courseUserLessonRepository;
    }

    @Autowired
    public void setCourseUserTestRepository(CourseUserTestRepository courseUserTestRepository) {
        this.courseUserTestRepository = courseUserTestRepository;
    }

    @Autowired
    public void setLessonService(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Autowired
    public void setTestService(CourseTestService testService) {
        this.testService = testService;
    }

    /*Methods*/
    /*================================================================================================================*/
    @Override
    public Optional<CourseUserEntity> findByUserIdAndCourseId(Integer userId, Integer courseId) {
        return courseUserRepository.findByUserIdAndCourseId(userId, courseId);
    }

    @Override
    public Optional<CourseUserLessonEntity> findByUserIdAndLessonId(Integer userId, Integer lessonId) {
        return courseUserLessonRepository.findByUserIdAndLessonId(userId, lessonId);
    }

    @Override
    public Optional<CourseUserTestEntity> findByUserIdAndTestId(Integer userId, Integer testId) {
        return courseUserTestRepository.findByUserIdAndTestId(userId, testId);
    }

    @Override
    public List<CourseUserLessonEntity> findAllByUserIdAndLessonIdIn(Integer userId, List<Integer> lessonIds) {
        return courseUserLessonRepository.findAllByUserIdAndLessonIdIn(userId, lessonIds);
    }

    @Override
    public List<CourseUserTestEntity> findAllByUserIdAndTestIdIn(Integer userId, List<Integer> testIds) {
        return courseUserTestRepository.findAllByUserIdAndTestIdIn(userId, testIds);
    }

    @Override
    public boolean isUserSignedOnCourse(UserPrincipal userPrincipal, Integer courseId) {

        if (userPrincipal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        Optional<CourseUserEntity> courseUserEntity =
                findByUserIdAndCourseId(userPrincipal.getId(), courseId);

        return courseUserEntity.isPresent();
    }

    @Override
    @Transactional
    public void signUserForCourse(int userId, CourseEntity courseEntity) {
        CourseUserEntity courseUserEntity = new CourseUserEntity();
        courseUserEntity.setUserId(userId);
        courseUserEntity.setCourseId(courseEntity.getId());

        List<CourseUserLessonEntity> userLessonEntities = new ArrayList<>();
        courseEntity.getLessons().forEach(lessonEntity -> {
            userLessonEntities.add(new CourseUserLessonEntity(userId, lessonEntity.getId(), courseEntity.getId()));
        });

        List<CourseUserTestEntity> userTestEntities = new ArrayList<>();
        courseEntity.getTests().forEach(courseTestEntity -> {
            userTestEntities.add(new CourseUserTestEntity(userId, courseTestEntity.getId(), courseEntity.getId()));
        });

        //Set last order number as 1 only if lessons/tests exist
        if (!userLessonEntities.isEmpty() || !userTestEntities.isEmpty()) {
            courseUserEntity.setLastOrderNumber(1);
        }

        courseUserLessonRepository.saveAll(userLessonEntities);
        courseUserTestRepository.saveAll(userTestEntities);
        courseUserRepository.save(courseUserEntity);
    }

    @Override
    public Pair<Integer, CourseContentItemType> findFirstContentItem(Integer courseId) {
        return findContentItemByCourseIdAndOrderNumber(courseId, 1);
    }

    @Override
    public Pair<Integer, CourseContentItemType> findContentItemByCourseIdAndOrderNumber(Integer courseId, Integer orderNumber) {
        Optional<LessonEntity> lessonEntity = lessonService.findByCourseIdAndOrderNumber(courseId, orderNumber);
        if (lessonEntity.isPresent()) {
            return Pair.of(lessonEntity.get().getId(), CourseContentItemType.LESSON);
        }

        Optional<CourseTestEntity> testEntity = testService.findByCourseIdAndOrderNumber(courseId, orderNumber);
        return testEntity
                .map(courseTestEntity -> Pair.of(courseTestEntity.getId(), CourseContentItemType.TEST))
                .orElse(null);
    }

    @Override
    public void saveCourseUserEntity(CourseUserEntity courseUserEntity) {
        courseUserRepository.save(courseUserEntity);
    }

    @Override
    public void saveCourseUserLessonEntity(CourseUserLessonEntity courseUserLessonEntity) {
        courseUserLessonRepository.save(courseUserLessonEntity);
    }

    @Override
    public void saveCourseUserTestEntity(CourseUserTestEntity courseUserTestEntity) {
        courseUserTestRepository.save(courseUserTestEntity);
    }

    @Override
    public Integer courseUserPassedCount(Integer courseId, Integer userId) {
        int passedLessons = courseUserLessonRepository.countByUserIdAndCourseIdAndCompleted(userId, courseId, 1);
        int passedTests = courseUserTestRepository.countByUserIdAndCourseIdAndCompleted(userId, courseId, 1);
        return passedLessons + passedTests;
    }
}
