package me.hizencode.mededu.course.content.lesson.media;

import java.util.List;
import java.util.Optional;

public interface LessonMediaService {

    Optional<LessonMediaEntity> findById(Integer id);

    void save(LessonMediaEntity lessonMediaEntity);

    List<LessonMediaEntity> findAllByLessonId(Integer id);

    void delete(LessonMediaEntity lessonMediaEntity);
}
