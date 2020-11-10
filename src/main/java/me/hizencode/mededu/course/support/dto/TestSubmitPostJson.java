package me.hizencode.mededu.course.support.dto;

import java.util.List;

public class TestSubmitPostJson {

    private int courseId;

    private int testId;

    private List<UserAnswerJson> userAnswers;

    public TestSubmitPostJson() {
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public List<UserAnswerJson> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<UserAnswerJson> userAnswers) {
        this.userAnswers = userAnswers;
    }

    @Override
    public String toString() {
        return "TestSubmitPostJson{" +
                "courseId=" + courseId +
                ", testId=" + testId +
                ", userAnswers=" + userAnswers +
                '}';
    }
}
