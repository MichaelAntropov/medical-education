package me.hizencode.mededu.courses;

import javax.persistence.*;

@Entity
@IdClass(CourseSpecialityId.class)
@Table(schema = "doctor_management", name = "course_speciality")
public class CourseSpecialityEntity {

    @Id
    @Column(name = "course_id")
    private int courseId;

    @Id
    @Column(name = "speciality_id")
    private int specialityId;

    public CourseSpecialityEntity() {
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
    }
}
