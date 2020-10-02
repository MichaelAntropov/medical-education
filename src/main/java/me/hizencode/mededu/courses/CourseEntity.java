package me.hizencode.mededu.courses;

import me.hizencode.mededu.lessons.LessonEntity;
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
