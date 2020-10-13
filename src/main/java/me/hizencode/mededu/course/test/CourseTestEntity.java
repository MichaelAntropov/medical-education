package me.hizencode.mededu.course.test;

import me.hizencode.mededu.course.LearningItem;
import me.hizencode.mededu.courses.CourseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course_test")
public class CourseTestEntity implements LearningItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @Column(name = "order_number")
    private int orderNumber;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @OneToMany(
            mappedBy = "test",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<CourseQuestionEntity> questions;

    public CourseTestEntity() {
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

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
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

    public void setContent(String content) {
        this.content = content;
    }

    public List<CourseQuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(List<CourseQuestionEntity> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "CourseTestEntity{" +
                "id=" + id +
                ", orderNumber=" + orderNumber +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
