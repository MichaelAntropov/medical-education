package me.hizencode.mededu.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    @Override
    public Page<CourseEntity> findAllByNameIsLike(String name, Pageable pageable) {
        return courseRepository.findAllByNameContains(name, pageable);
    }

    @Transactional
    @Override
    public Page<CourseEntity> findAllBySpecialities(List<Integer> specialitiesId, Pageable pageable) {
        return courseRepository.findAllBySpecialities(specialitiesId, (long) specialitiesId.size(), pageable);
    }

    @Transactional
    @Override
    public Page<CourseEntity> findAllByNameAndSpecialities(String name, List<Integer> specialitiesId, Pageable pageable) {
        return courseRepository.findAllByNameAndSpecialities(name, specialitiesId, (long) specialitiesId.size(), pageable);
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
