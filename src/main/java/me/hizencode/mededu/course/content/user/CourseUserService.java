package me.hizencode.mededu.course.content.user;

import me.hizencode.mededu.course.CourseEntity;
import me.hizencode.mededu.course.content.CourseContentItemType;
import me.hizencode.mededu.course.content.user.lesson.CourseUserLessonEntity;
import me.hizencode.mededu.course.content.user.test.CourseUserTestEntity;
import me.hizencode.mededu.user.UserPrincipal;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;

public interface CourseUserService {

    Optional<CourseUserEntity> findByUserIdAndCourseId(Integer userId, Integer courseId);

    Optional<CourseUserLessonEntity> findByUserIdAndLessonId(Integer userId, Integer lessonId);

    Optional<CourseUserTestEntity> findByUserIdAndTestId(Integer userId, Integer testId);

    List<CourseUserLessonEntity> findAllByUserIdAndLessonIdIn(Integer userId, List<Integer> lessonIds);

    List<CourseUserTestEntity> findAllByUserIdAndTestIdIn(Integer userId, List<Integer> testIds);

    boolean isUserSignedOnCourse(UserPrincipal userPrincipal, Integer courseId);

    void signUserForCourse(int userId, CourseEntity courseEntity);

    Pair<Integer, CourseContentItemType> findFirstContentItem(Integer courseId);

    Pair<Integer, CourseContentItemType> findContentItemByCourseIdAndOrderNumber(Integer courseId, Integer orderNumber);

    void saveCourseUserEntity(CourseUserEntity courseUserEntity);

    void saveCourseUserLessonEntity(CourseUserLessonEntity courseUserLessonEntity);

    void saveCourseUserTestEntity(CourseUserTestEntity courseUserTestEntity);

    Integer courseUserPassedCount(Integer courseId, Integer userId);
}
