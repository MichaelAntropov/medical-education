package me.hizencode.mededu.lessons;

import me.hizencode.mededu.courses.CourseEntity;

import javax.persistence.*;

@Entity
@Table(schema = "doctor_management", name = "lesson")
public class LessonEntity {

    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @Column(name = "order_number")
    private int orderNumber;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    public LessonEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity courseId) {
        this.course = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String text) {
        this.content = text;
    }

    public void setOrderNumber(int order) {
        this.orderNumber = order;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    @Override
    public String toString() {
        return "LessonEntity{" +
                "id=" + id +
                ", courseId=" + course.getId() +
                ", orderNumber=" + orderNumber +
                ", title='" + title + '\'' +
                '}';
    }
}
