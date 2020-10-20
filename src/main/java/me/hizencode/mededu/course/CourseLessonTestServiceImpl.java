package me.hizencode.mededu.course;

import me.hizencode.mededu.course.lesson.LessonEntity;
import me.hizencode.mededu.course.lesson.LessonService;
import me.hizencode.mededu.course.test.CourseTestEntity;
import me.hizencode.mededu.course.test.CourseTestService;
import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseLessonTestServiceImpl implements CourseLessonTestService {

    /*Fields*/
    /*================================================================================================================*/
    private CourseService courseService;

    private LessonService lessonService;

    private CourseTestService testService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setLessonService(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Autowired
    public void setTestService(CourseTestService testService) {
        this.testService = testService;
    }
    /*Methods*/
    /*================================================================================================================*/

    @Override
    @Transactional
    public void deleteLessonAndSaveItemsList(CourseEntity courseEntity, LessonEntity lessonEntity, List<LearningItem> items) {
        saveCourseAndSaveItemsList(courseEntity, items);
        lessonService.deleteLesson(lessonEntity);
    }

    @Override
    @Transactional
    public void deleteTestAndSaveItemsList(CourseEntity courseEntity, CourseTestEntity courseTestEntity, List<LearningItem> items) {
        saveCourseAndSaveItemsList(courseEntity, items);
        testService.deleteCourseTest(courseTestEntity);
    }

    public void saveCourseAndSaveItemsList(CourseEntity courseEntity, List<LearningItem> items) {
        List<LessonEntity> lessons = new ArrayList<>();
        List<CourseTestEntity> tests = new ArrayList<>();

        items.forEach(item -> {
            if(item.getType().equals(LearningItemType.LESSON)) {
                lessons.add((LessonEntity)item);
            }
            if(item.getType().equals(LearningItemType.TEST)) {
                tests.add((CourseTestEntity)item);
            }
        });

        lessonService.saveAll(lessons);
        testService.saveAll(tests);
        courseService.saveCourse(courseEntity);
    }

    @Override
    @Transactional
    public void saveItemsList(List<LearningItem> items) {
        List<LessonEntity> lessons = new ArrayList<>();
        List<CourseTestEntity> tests = new ArrayList<>();

        items.forEach(item -> {
            if(item.getType().equals(LearningItemType.LESSON)) {
                lessons.add((LessonEntity)item);
            }
            if(item.getType().equals(LearningItemType.TEST)) {
                tests.add((CourseTestEntity)item);
            }
        });

        lessonService.saveAll(lessons);
        testService.saveAll(tests);
    }
}
