package me.hizencode.mededu.course.content.user.test;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseUserTestRepository extends JpaRepository<CourseUserTestEntity, Integer> {

    List<CourseUserTestEntity> findAllByCourseId(Integer courseId);

    List<CourseUserTestEntity> findAllByUserIdAndTestIdIn(Integer userId, List<Integer> testIds);

    Optional<CourseUserTestEntity> findByUserIdAndTestId(Integer userId, Integer testId);

    Integer countByUserIdAndCourseIdAndCompleted(Integer userId, Integer courseId, Integer completed);
}
