package me.hizencode.mededu.lessons;

import me.hizencode.mededu.courses.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface LessonService {

    Optional<LessonEntity> findById(int id);

    List<LessonEntity> findAllByCourseId(int courseId);

    List<LessonIdTitleOrderNumberOnly> findAllByCourseIdOnlyIdAndTitleAndOrder(int courseId);

    List<LessonEntity> findAllByCourseIdOrderByOrderNumber(int courseId);

    void saveLesson(LessonEntity lessonEntity);

    void saveNewLesson(CourseEntity courseEntity, LessonEntity lessonEntity);

    Optional<LessonEntity> findByCourseIdAndOrderNumber(int courseId, int orderNumber);

    void saveAll(List<LessonEntity> lessonEntities);

    void deleteLessonAndSaveList(CourseEntity courseEntity, LessonEntity lessonEntity, List<LessonEntity> lessonEntities);
}
