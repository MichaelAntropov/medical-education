package me.hizencode.mededu.course;

import me.hizencode.mededu.course.lesson.LessonEntity;
import me.hizencode.mededu.course.test.CourseTestEntity;
import me.hizencode.mededu.courses.CourseEntity;

public interface LearningItem {

    int getId();

    void setId(int id);

    CourseEntity getCourse();

    void setCourse(CourseEntity courseId);

    String getTitle();

    void setTitle(String title);

    String getContent();

    void setContent(String text);

    void setOrderNumber(int order);

    public Integer getOrderNumber();

    default LearningItemType getType() {
        if(this instanceof LessonEntity) {
           return LearningItemType.LESSON;
        }
        if(this instanceof CourseTestEntity) {
            return LearningItemType.TEST;
        }
        return null;
    }

}
