package me.hizencode.mededu.course.content.user.lesson;

import javax.persistence.*;

@Entity
@IdClass(CourseUserLessonId.class)
@Table(name = "course_user_lesson")
public class CourseUserLessonEntity {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "lesson_id")
    private int lessonId;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "completed")
    private int completed;

    public CourseUserLessonEntity() {
    }

    public CourseUserLessonEntity(int userId, int lessonId, int courseId) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.courseId = courseId;
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

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
