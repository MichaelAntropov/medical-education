package me.hizencode.mededu.course.content.test;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course_test_question")
public class CourseQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseTestEntity test;

    @Column(name = "order_number")
    private int orderNumber;

    @OneToOne(fetch = FetchType.LAZY)
    private CourseAnswerEntity correctAnswer;

    @Column(name = "content")
    private String content;

    @OneToMany(
            mappedBy = "question",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<CourseAnswerEntity> answers;

    public CourseQuestionEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CourseTestEntity getTest() {
        return test;
    }

    public void setTest(CourseTestEntity test) {
        this.test = test;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public CourseAnswerEntity getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(CourseAnswerEntity correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CourseAnswerEntity> getAnswers() {
        return answers;
    }

    public void setAnswers(List<CourseAnswerEntity> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "CourseQuestionEntity{" +
                "id=" + id +
                ", test=" + test +
                ", orderNumber=" + orderNumber +
                ", correctAnswer=" + correctAnswer +
                ", content='" + content + '\'' +
                '}';
    }
}
