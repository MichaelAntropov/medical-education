package me.hizencode.mededu.course.content.user;

import javax.persistence.*;

@Entity
@IdClass(CourseUserId.class)
@Table(schema = "doctor_management", name = "course_user_record")
public class CourseUserEntity {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "course_id")
    private int courseId;

    @Column(name = "last_order_number")
    private int lastOrderNumber;

    public CourseUserEntity() {
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

    public int getLastOrderNumber() {
        return lastOrderNumber;
    }

    public void setLastOrderNumber(int lastOrderNumber) {
        this.lastOrderNumber = lastOrderNumber;
    }

}

