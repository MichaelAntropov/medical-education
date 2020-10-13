package me.hizencode.mededu.course.test;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseTestRepository extends JpaRepository<CourseTestEntity, Integer> {

    <T> List<T> findAllByCourseId(int courseId, Class<T> type);

    <T> List<T> findAllByCourseIdOrderByOrderNumber(int courseId, Class<T> type);

    Optional<CourseTestEntity> findByCourseIdAndOrderNumber(int courseId, int orderNumber);
}
