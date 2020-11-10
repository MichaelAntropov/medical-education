package me.hizencode.mededu.course;

import java.io.Serializable;
import java.util.Objects;

public class CourseSpecialityId implements Serializable {

    private int courseId;

    private int specialityId;

    public CourseSpecialityId() {
    }

    public CourseSpecialityId(int courseId, int specialityId) {
        this.courseId = courseId;
        this.specialityId = specialityId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseSpecialityId)) return false;
        CourseSpecialityId that = (CourseSpecialityId) o;
        return courseId == that.courseId &&
                specialityId == that.specialityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, specialityId, this.getClass().getName());
    }
}
