package me.hizencode.mededu.course.content.lesson;

import me.hizencode.mededu.course.content.CourseContentItem;
import me.hizencode.mededu.course.content.lesson.media.LessonMediaEntity;
import me.hizencode.mededu.course.CourseEntity;
import me.hizencode.mededu.course.content.user.lesson.CourseUserLessonEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "doctor_management", name = "course_lesson")
public class LessonEntity implements CourseContentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @OneToMany(
            mappedBy = "lesson",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<LessonMediaEntity> media;

    @Column(name = "order_number")
    private int orderNumber;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @OneToMany(
            mappedBy = "lessonId",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<CourseUserLessonEntity> courseUserLessonEntities;

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

    public List<LessonMediaEntity> getMedia() {
        return media;
    }

    public void setMedia(List<LessonMediaEntity> media) {
        this.media = media;
    }

    public List<CourseUserLessonEntity> getCourseUserLessonEntities() {
        return courseUserLessonEntities;
    }

    public void setCourseUserLessonEntities(List<CourseUserLessonEntity> courseUserLessonEntities) {
        this.courseUserLessonEntities = courseUserLessonEntities;
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
