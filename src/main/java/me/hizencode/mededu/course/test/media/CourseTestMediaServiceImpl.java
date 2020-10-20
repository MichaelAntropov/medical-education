package me.hizencode.mededu.course.test.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseTestMediaServiceImpl implements CourseTestMediaService {
    /*Fields*/
    /*================================================================================================================*/
    private CourseTestMediaRepository testMediaRepository;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setTestMediaRepository(CourseTestMediaRepository testMediaRepository) {
        this.testMediaRepository = testMediaRepository;
    }

    /*Methods*/
    /*================================================================================================================*/

    @Override
    public Optional<CourseTestMediaEntity> findById(Integer mediaId) {
        return testMediaRepository.findById(mediaId);
    }

    @Override
    public void delete(CourseTestMediaEntity courseTestMediaEntity) {
        testMediaRepository.delete(courseTestMediaEntity);
    }

    @Override
    public void deleteById(Integer mediaId) {
        testMediaRepository.deleteById(mediaId);
    }

    @Override
    public void save(CourseTestMediaEntity courseTestMediaEntity) {
        testMediaRepository.save(courseTestMediaEntity);
    }

    @Override
    public CourseTestMediaEntity saveAndReturn(CourseTestMediaEntity courseTestMediaEntity) {
        return testMediaRepository.save(courseTestMediaEntity);
    }
}
