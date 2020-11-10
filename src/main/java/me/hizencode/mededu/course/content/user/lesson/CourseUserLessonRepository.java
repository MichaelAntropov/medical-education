package me.hizencode.mededu.course.content.user.lesson;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseUserLessonRepository extends JpaRepository<CourseUserLessonEntity, Integer> {

    List<CourseUserLessonEntity> findAllByUserIdAndLessonIdIn(Integer userId, List<Integer> lessonIds);

    Optional<CourseUserLessonEntity> findByUserIdAndLessonId(Integer userId, Integer lessonId);

    Integer countByUserIdAndCourseIdAndCompleted(Integer userId, Integer courseId, Integer completed);
}
