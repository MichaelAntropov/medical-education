package me.hizencode.doctormanagement.courses;

import me.hizencode.doctormanagement.specialities.SpecialityEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "doctor_management", name = "courses")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(name = "courses_details",
            joinColumns = @JoinColumn(name = "id"))
    private CourseDetailsEntity courseDetails;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(name = "courses_description",
            joinColumns = @JoinColumn(name = "id"))
    private CourseDescriptionEntity courseDescription;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "courses_specialities",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "speciality_id"))
    private List<SpecialityEntity> specialities;

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

    public CourseDetailsEntity getCourseDetails() {
        return courseDetails;
    }

    public void setCourseDetails(CourseDetailsEntity courseDetails) {
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
}
