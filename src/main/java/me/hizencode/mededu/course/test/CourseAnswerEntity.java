package me.hizencode.mededu.course.test;

import javax.persistence.*;

@Entity
@Table(name = "course_answer")
public class CourseAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseTestEntity question;

    @Column(name = "order_number")
    private int orderNumber;

    @Column(name = "content")
    private String content;

    public CourseAnswerEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CourseTestEntity getQuestion() {
        return question;
    }

    public void setQuestion(CourseTestEntity question) {
        this.question = question;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CourseAnswerEntity{" +
                "id=" + id +
                ", orderNumber=" + orderNumber +
                ", content='" + content + '\'' +
                '}';
    }
}
