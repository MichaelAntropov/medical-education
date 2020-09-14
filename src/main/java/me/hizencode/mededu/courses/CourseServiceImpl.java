package me.hizencode.mededu.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    /*Fields*/
    /*================================================================================================================*/
    private CourseRepository courseRepository;

    private CourseDetailRepository courseDetailRepository;

    private CourseDescriptionRepository courseDescriptionRepository;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Autowired
    public void setCourseDetailRepository(CourseDetailRepository courseDetailRepository) {
        this.courseDetailRepository = courseDetailRepository;
    }

    @Autowired
    public void setCourseDescriptionRepository(CourseDescriptionRepository courseDescriptionRepository) {
        this.courseDescriptionRepository = courseDescriptionRepository;
    }

    /*Methods*/
    /*================================================================================================================*/
    @Transactional
    @Override
    public void saveNewCourse(CourseEntity courseEntity, CourseDetailEntity courseDetailEntity, CourseDescriptionEntity courseDescriptionEntity) {

        courseEntity.setCourseDetails(courseDetailRepository.save(courseDetailEntity));
        courseEntity.setCourseDescription(courseDescriptionRepository.save(courseDescriptionEntity));

        courseRepository.save(courseEntity);
    }

    @Transactional
    @Override
    public void saveCourse(CourseEntity courseEntity) {
        courseRepository.save(courseEntity);
    }

    @Transactional
    @Override
    public Page<CourseEntity> findAll(PageRequest pageRequest) {
        return courseRepository.findAll(pageRequest);
    }

    @Override
    public Optional<CourseEntity> getCourseById(Integer courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public void deleteCourseWhereId(Integer courseId) {
        courseRepository.deleteById(courseId);
    }
}
