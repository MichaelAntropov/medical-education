package me.hizencode.mededu.course.content.user.test;

import java.io.Serializable;
import java.util.Objects;

public class CourseUserTestId implements Serializable {

    private int userId;

    private int testId;

    public CourseUserTestId() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseUserTestId)) return false;
        CourseUserTestId that = (CourseUserTestId) o;
        return userId == that.userId &&
                testId == that.testId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, testId);
    }
}
