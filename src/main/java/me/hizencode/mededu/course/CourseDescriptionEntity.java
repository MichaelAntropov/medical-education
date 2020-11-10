package me.hizencode.mededu.course;

import javax.persistence.*;

@Entity
@Table(schema = "doctor_management", name = "course_description")
public class CourseDescriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "text")
    private String text;

    public CourseDescriptionEntity() {
    }

    public CourseDescriptionEntity(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CourseDescriptionEntity{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
