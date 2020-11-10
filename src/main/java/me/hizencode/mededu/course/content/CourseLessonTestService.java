package me.hizencode.mededu.course.content;

import me.hizencode.mededu.course.content.lesson.LessonEntity;
import me.hizencode.mededu.course.content.test.CourseTestEntity;
import me.hizencode.mededu.course.CourseEntity;

import java.util.List;

public interface CourseLessonTestService {

    void deleteLessonAndSaveItemsList(CourseEntity courseEntity, LessonEntity lessonEntity, List<CourseContentItem> items);

    void deleteTestAndSaveItemsList(CourseEntity courseEntity, CourseTestEntity courseTestEntity, List<CourseContentItem> items);

    void saveItemsList(List<CourseContentItem> items);
}
