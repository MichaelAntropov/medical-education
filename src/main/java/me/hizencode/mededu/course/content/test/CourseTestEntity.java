package me.hizencode.mededu.course.content.test;

import me.hizencode.mededu.course.content.CourseContentItem;
import me.hizencode.mededu.course.content.test.media.CourseTestMediaEntity;
import me.hizencode.mededu.course.CourseEntity;
import me.hizencode.mededu.course.content.user.test.CourseUserTestEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course_test")
public class CourseTestEntity implements CourseContentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @OneToMany(
            mappedBy = "test",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<CourseTestMediaEntity> media;

    @Column(name = "order_number")
    private int orderNumber;

    @Column(name = "required")
    private int required;

    @Column(name = "required_score")
    private int requiredScore;

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

    @OneToMany(
            mappedBy = "testId",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<CourseUserTestEntity> courseUserTestEntities;

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

    public List<CourseTestMediaEntity> getMedia() {
        return media;
    }

    public void setMedia(List<CourseTestMediaEntity> media) {
        this.media = media;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public int getRequiredScore() {
        return requiredScore;
    }

    public void setRequiredScore(int requiredScore) {
        this.requiredScore = requiredScore;
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

    public List<CourseUserTestEntity> getCourseUserTestEntities() {
        return courseUserTestEntities;
    }

    public void setCourseUserTestEntities(List<CourseUserTestEntity> courseUserTestEntities) {
        this.courseUserTestEntities = courseUserTestEntities;
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
