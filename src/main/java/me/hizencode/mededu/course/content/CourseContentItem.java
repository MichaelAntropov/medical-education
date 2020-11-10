package me.hizencode.mededu.course.content;

import me.hizencode.mededu.course.content.lesson.LessonEntity;
import me.hizencode.mededu.course.content.test.CourseTestEntity;
import me.hizencode.mededu.course.CourseEntity;

public interface CourseContentItem {

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

    default CourseContentItemType getType() {
        if(this instanceof LessonEntity) {
           return CourseContentItemType.LESSON;
        }
        if(this instanceof CourseTestEntity) {
            return CourseContentItemType.TEST;
        }
        return null;
    }

}
