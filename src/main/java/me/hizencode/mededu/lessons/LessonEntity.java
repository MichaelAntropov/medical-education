package me.hizencode.mededu.lessons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "doctor_management", name = "lesson")
public class LessonEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "course_id")
    private int courseId;

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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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
                ", title='" + title + '\'' +
                '}';
    }
}
