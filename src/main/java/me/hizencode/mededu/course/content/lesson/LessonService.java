package me.hizencode.mededu.course.content.lesson;

import me.hizencode.mededu.course.content.CourseContentItemProjection;
import me.hizencode.mededu.course.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface LessonService {

    Optional<LessonEntity> findById(int id);

    List<LessonEntity> findAllByCourseId(int courseId);

    List<CourseContentItemProjection> findAllByCourseIdOnlyIdAndTitleAndOrder(int courseId);

    List<LessonEntity> findAllByCourseIdOrderByOrderNumber(int courseId);

    void saveLesson(LessonEntity lessonEntity);

    /**
     * Saves new lesson and returns its id.
     *
     * @param courseEntity course which the lesson belongs to
     * @param lessonEntity lesson to save
     * @return returns the id of the lesson
     */
    Integer saveNewLesson(CourseEntity courseEntity, LessonEntity lessonEntity);

    Optional<LessonEntity> findByCourseIdAndOrderNumber(int courseId, int orderNumber);

    void saveAll(List<LessonEntity> lessonEntities);

    void deleteLesson(LessonEntity lessonEntity);
}
