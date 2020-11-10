package me.hizencode.mededu.course.content.user.test;

import javax.persistence.*;

@Entity
@IdClass(CourseUserTestId.class)
@Table(name = "course_user_test")
public class CourseUserTestEntity {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "test_id")
    private int testId;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "completed")
    private int completed;

    @Column(name = "score")
    private int score;

    public CourseUserTestEntity() {
    }

    public CourseUserTestEntity(int userId, int testId, int courseId) {
        this.userId = userId;
        this.testId = testId;
        this.courseId = courseId;
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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "CourseUserTestEntity{" +
                "userId=" + userId +
                ", testId=" + testId +
                ", courseId=" + courseId +
                ", completed=" + completed +
                ", score=" + score +
                '}';
    }
}
