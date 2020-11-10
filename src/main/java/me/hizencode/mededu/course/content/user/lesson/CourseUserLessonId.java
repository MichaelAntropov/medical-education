package me.hizencode.mededu.course.content.user.lesson;

import java.io.Serializable;
import java.util.Objects;

public class CourseUserLessonId implements Serializable {

    private int userId;

    private int lessonId;

    public CourseUserLessonId() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseUserLessonId)) return false;
        CourseUserLessonId that = (CourseUserLessonId) o;
        return userId == that.userId &&
                lessonId == that.lessonId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, lessonId);
    }
}
