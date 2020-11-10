package me.hizencode.mededu.course;

import me.hizencode.mededu.course.content.test.CourseTestEntity;
import me.hizencode.mededu.course.content.lesson.LessonEntity;
import me.hizencode.mededu.course.content.user.CourseUserEntity;
import me.hizencode.mededu.course.content.user.lesson.CourseUserLessonEntity;
import me.hizencode.mededu.course.content.user.test.CourseUserTestEntity;
import me.hizencode.mededu.specialities.SpecialityEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "doctor_management", name = "course")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "course_details_id", referencedColumnName = "id")
    private CourseDetailEntity courseDetails;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "course_description_id", referencedColumnName = "id")
    private CourseDescriptionEntity courseDescription;

    @Column(name = "lesson_count")
    private int lessonCount;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "edit_date")
    private Date editDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "course_speciality",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "speciality_id"))
    private List<SpecialityEntity> specialities;

    @OneToMany(
            mappedBy = "course",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<LessonEntity> lessons;

    @OneToMany(
            mappedBy = "course",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<CourseTestEntity> tests;

    @OneToMany(
            mappedBy = "courseId",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<CourseUserLessonEntity> courseUserLessonEntities;

    @OneToMany(
            mappedBy = "courseId",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<CourseUserTestEntity> courseUserTestEntities;

    @OneToMany(
            mappedBy = "courseId",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<CourseUserEntity> courseUserEntities;

    public CourseEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(int lessonCount) {
        this.lessonCount = lessonCount;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public CourseDetailEntity getCourseDetails() {
        return courseDetails;
    }

    public void setCourseDetails(CourseDetailEntity courseDetails) {
        this.courseDetails = courseDetails;
    }

    public CourseDescriptionEntity getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(CourseDescriptionEntity courseDescription) {
        this.courseDescription = courseDescription;
    }

    public List<SpecialityEntity> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<SpecialityEntity> specialities) {
        this.specialities = specialities;
    }

    public List<LessonEntity> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonEntity> lessons) {
        this.lessons = lessons;
    }

    public List<CourseTestEntity> getTests() {
        return tests;
    }

    public void setTests(List<CourseTestEntity> tests) {
        this.tests = tests;
    }

    public List<CourseUserLessonEntity> getCourseUserLessonEntities() {
        return courseUserLessonEntities;
    }

    public void setCourseUserLessonEntities(List<CourseUserLessonEntity> courseUserLessonEntities) {
        this.courseUserLessonEntities = courseUserLessonEntities;
    }

    public List<CourseUserTestEntity> getCourseUserTestEntities() {
        return courseUserTestEntities;
    }

    public void setCourseUserTestEntities(List<CourseUserTestEntity> courseUserTestEntities) {
        this.courseUserTestEntities = courseUserTestEntities;
    }

    public List<CourseUserEntity> getCourseUserEntities() {
        return courseUserEntities;
    }

    public void setCourseUserEntities(List<CourseUserEntity> courseUserEntities) {
        this.courseUserEntities = courseUserEntities;
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courseDetails=" + courseDetails +
                ", courseDescription=" + courseDescription +
                ", specialities=" + specialities +
                '}';
    }
}
