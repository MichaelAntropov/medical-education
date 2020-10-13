package me.hizencode.mededu.course.lesson;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<LessonEntity, Integer> {

    Optional<LessonEntity> findById(int id);

    <T> List<T> findAllByCourseId(int courseId, Class<T> type);

    <T> List<T> findAllByCourseIdOrderByOrderNumber(int courseId, Class<T> type);

    Optional<LessonEntity> findByCourseIdAndOrderNumber(int courseId, int orderNumber);
}
