package me.hizencode.mededu.course.content.user;

import java.io.Serializable;
import java.util.Objects;

public class CourseUserId implements Serializable {

    private int userId;

    private int courseId;

    public CourseUserId() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseUserId)) return false;
        CourseUserId that = (CourseUserId) o;
        return userId == that.userId &&
                courseId == that.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, courseId);
    }
}
