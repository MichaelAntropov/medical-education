package me.hizencode.mededu.course.content.test;

import me.hizencode.mededu.course.content.CourseContentItemProjection;
import me.hizencode.mededu.course.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseTestService {

    void saveTest(CourseTestEntity courseTestEntity);

    Integer saveNewTest(CourseEntity courseEntity, CourseTestEntity courseTestEntity);

    Optional<CourseTestEntity> findById(Integer testId);

    Optional<CourseTestEntity> findByCourseIdAndOrderNumber(Integer testId, Integer orderNumber);

    List<CourseTestEntity>  findAllByCourseId(int courseId);

    List<CourseContentItemProjection>  findAllByCourseIdAndTitleOnly(int courseId);

    void saveAll(List<CourseTestEntity> courseTestEntities);

    void deleteCourseTest(CourseTestEntity courseTestEntity);

    void saveQuestions(List<CourseQuestionEntity> courseQuestionEntities,
                       List<Integer> questionsToDelete, List<Integer> answersToDelete);

    void markTestAsEdited(CourseTestEntity courseTestEntity);
}
