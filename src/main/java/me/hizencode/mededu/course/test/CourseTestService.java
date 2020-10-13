package me.hizencode.mededu.course.test;

import me.hizencode.mededu.course.LearningItemProjection;
import me.hizencode.mededu.courses.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseTestService {

    void saveTest(CourseTestEntity courseTestEntity);

    Integer saveNewTest(CourseEntity courseEntity, CourseTestEntity courseTestEntity);

    Optional<CourseTestEntity> findById(Integer testId);

    Optional<CourseTestEntity> findByCourseIdAndOrderNumber(Integer testId, Integer orderNumber);

    List<CourseTestEntity>  findAllByCourseId(int courseId);

    List<LearningItemProjection>  findAllByCourseIdAndTitleOnly(int courseId);

    void saveAll(List<CourseTestEntity> courseTestEntities);

    void deleteCourseTest(CourseTestEntity courseTestEntity);
}
