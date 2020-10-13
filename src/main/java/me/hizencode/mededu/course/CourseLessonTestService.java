package me.hizencode.mededu.course;

import me.hizencode.mededu.course.lesson.LessonEntity;
import me.hizencode.mededu.course.test.CourseTestEntity;
import me.hizencode.mededu.courses.CourseEntity;

import java.util.List;

public interface CourseLessonTestService {

    void deleteLessonAndSaveItemsList(CourseEntity courseEntity, LessonEntity lessonEntity, List<LearningItem> items);

    void deleteTestAndSaveItemsList(CourseEntity courseEntity, CourseTestEntity courseTestEntity, List<LearningItem> items);

    void saveItemsList(List<LearningItem> items);
}
