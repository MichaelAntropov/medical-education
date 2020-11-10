package me.hizencode.mededu.course.content.lesson.media;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonMediaRepository extends JpaRepository<LessonMediaEntity, Integer> {

    Optional<LessonMediaEntity> findById(Integer id);

    List<LessonMediaEntity> findAllByLesson_Id(Integer id);
}
