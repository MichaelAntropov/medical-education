package me.hizencode.mededu.course.test;

import me.hizencode.mededu.course.LearningItemProjection;
import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseTestServiceImpl implements CourseTestService {

    /*Fields*/
    /*================================================================================================================*/
    private CourseTestRepository courseTestRepository;

    private CourseService courseService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setCourseTestRepository(CourseTestRepository courseTestRepository) {
        this.courseTestRepository = courseTestRepository;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    /*Methods*/
    /*================================================================================================================*/
    @Override
    public void saveTest(CourseTestEntity courseTestEntity) {
        courseTestRepository.save(courseTestEntity);
    }

    @Override
    @Transactional
    public Integer saveNewTest(CourseEntity courseEntity, CourseTestEntity courseTestEntity) {
        CourseTestEntity savedTest = courseTestRepository.save(courseTestEntity);
        courseService.saveCourse(courseEntity);

        return savedTest.getId();
    }

    @Override
    public Optional<CourseTestEntity> findById(Integer testId) {
        return courseTestRepository.findById(testId);
    }

    @Override
    public Optional<CourseTestEntity> findByCourseIdAndOrderNumber(Integer testId, Integer orderNumber) {
        return courseTestRepository.findByCourseIdAndOrderNumber(testId, orderNumber);
    }

    @Override
    public List<CourseTestEntity> findAllByCourseId(int courseId) {
        return courseTestRepository.findAllByCourseId(courseId, CourseTestEntity.class);
    }

    @Override
    public List<LearningItemProjection> findAllByCourseIdAndTitleOnly(int courseId) {
        return courseTestRepository.findAllByCourseId(courseId, LearningItemProjection.class);
    }

    @Override
    public void saveAll(List<CourseTestEntity> courseTestEntities) {
        courseTestRepository.saveAll(courseTestEntities);
    }

    @Override
    public void deleteCourseTest(CourseTestEntity courseTestEntity) {
        courseTestRepository.delete(courseTestEntity);
    }
}
