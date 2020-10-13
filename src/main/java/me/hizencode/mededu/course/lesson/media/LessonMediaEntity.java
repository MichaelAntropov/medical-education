package me.hizencode.mededu.course.lesson.media;

import me.hizencode.mededu.course.lesson.LessonEntity;

import javax.persistence.*;

@Entity
@Table(schema = "doctor_management", name = "lesson_media")
public class LessonMediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private LessonEntity lesson;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "data")
    private byte[] data;

    public LessonMediaEntity() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public LessonEntity getLesson() {
        return lesson;
    }

    public void setLesson(LessonEntity lesson) {
        this.lesson = lesson;
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
