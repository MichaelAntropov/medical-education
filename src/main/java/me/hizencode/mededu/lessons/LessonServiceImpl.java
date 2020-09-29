package me.hizencode.mededu.lessons;

import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService{

    /*Fields*/
    /*================================================================================================================*/
    private LessonRepository lessonRepository;

    private CourseService courseService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setLessonRepository(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    /*Methods*/
    /*================================================================================================================*/
    @Override
    public Optional<LessonEntity> findById(int id) {
        return lessonRepository.findById(id);
    }

    @Override
    public List<LessonEntity> findAllByCourseId(int courseId) {
        return lessonRepository.findAllByCourseId(courseId, LessonEntity.class);
    }

    @Override
    public List<LessonIdTitleOrderNumberOnly> findAllByCourseIdOnlyIdAndTitleAndOrder(int courseId) {
        return lessonRepository.findAllByCourseIdOrderByOrderNumber(courseId, LessonIdTitleOrderNumberOnly.class);
    }

    @Override
    public List<LessonEntity> findAllByCourseIdOrderByOrderNumber(int courseId) {
        return lessonRepository.findAllByCourseIdOrderByOrderNumber(courseId, LessonEntity.class);
    }

    @Override
    public void saveLesson(LessonEntity lessonEntity) {
        lessonRepository.save(lessonEntity);
    }

    @Override
    @Transactional
    public void saveNewLesson(CourseEntity courseEntity, LessonEntity lessonEntity) {
        lessonRepository.save(lessonEntity);
        courseService.saveCourse(courseEntity);
    }

    @Override
    public Optional<LessonEntity> findByCourseIdAndOrderNumber(int courseId, int orderNumber) {
        return lessonRepository.findByCourseIdAndOrderNumber(courseId, orderNumber);
    }

    @Override
    public void saveAll(List<LessonEntity> lessonEntities) {
        lessonRepository.saveAll(lessonEntities);
    }

    @Override
    @Transactional
    public void deleteLessonAndSaveList(LessonEntity lessonEntity, List<LessonEntity> lessonEntities) {
        lessonRepository.delete(lessonEntity);
        saveAll(lessonEntities);
    }
}
