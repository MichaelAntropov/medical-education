package me.hizencode.doctormanagement.courses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface CourseService {

    void saveNewCourse(CourseEntity courseEntity,
                       CourseDetailEntity courseDetailEntity,
                       CourseDescriptionEntity courseDescriptionEntity);

    void saveCourse(CourseEntity courseEntity);

    Page<CourseEntity> findAll(PageRequest pageRequest);

    Optional<CourseEntity> getCourseById(Integer courseId);

    void deleteCourseWhereId(Integer courseId);
}
