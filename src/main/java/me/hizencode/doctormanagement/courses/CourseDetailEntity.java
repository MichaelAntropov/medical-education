package me.hizencode.doctormanagement.courses;

import me.hizencode.doctormanagement.certificates.CertificateEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "doctor_management", name = "course_detail")
public class CourseDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "start_course")
    private Date startCourse;

    @Column(name = "end_course")
    private Date endCourse;

    @Column(name = "author")
    private String author;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(name = "certificate",
            joinColumns = @JoinColumn(name = "id"))
    private CertificateEntity certificate;

    public CourseDetailEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Date getStartCourse() {
        return startCourse;
    }

    public void setStartCourse(Date startCourse) {
        this.startCourse = startCourse;
    }

    public Date getEndCourse() {
        return endCourse;
    }

    public void setEndCourse(Date endCourse) {
        this.endCourse = endCourse;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public CertificateEntity getCertificate() {
        return certificate;
    }

    public void setCertificate(CertificateEntity certificate) {
        this.certificate = certificate;
    }
}
