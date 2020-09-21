package me.hizencode.mededu.courses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    void saveNewCourse(CourseEntity courseEntity,
                       CourseDetailEntity courseDetailEntity,
                       CourseDescriptionEntity courseDescriptionEntity);

    void saveCourse(CourseEntity courseEntity);

    Page<CourseEntity> findAll(PageRequest pageRequest);

    Page<CourseEntity> findAllByNameIsLike(String name, Pageable pageable);

    Page<CourseEntity> findAllBySpecialities(List<Integer> specialitiesId, Pageable pageable);

    Page<CourseEntity> findAllByNameAndSpecialities(String name, List<Integer> specialitiesId, Pageable pageable);

    Optional<CourseEntity> getCourseById(Integer courseId);

    void deleteCourseWhereId(Integer courseId);
}
