package me.hizencode.mededu.dashboard.admin.dto;

import me.hizencode.mededu.specialities.SpecialityEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

public class AdminCourseDto {

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Integer courseId;

    @Size(max = 128)
    private String name;

    @Size(max = 255)
    private String videoUrl;

    @DateTimeFormat(pattern = "dd/MM/yyyy h:mm a")
    private Date startCourse;

    @DateTimeFormat(pattern = "dd/MM/yyyy h:mm a")
    private Date endCourse;

    @Size(max = 128)
    private String author;

    @Size(max = 32768)
    private String description;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Integer imageId;

    private List<SpecialityEntity> chosenSpecialities;

    private boolean isCertificateAvailable;

    public AdminCourseDto() {
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public List<SpecialityEntity> getChosenSpecialities() {
        return chosenSpecialities;
    }

    public void setChosenSpecialities(List<SpecialityEntity> chosenSpecialities) {
        this.chosenSpecialities = chosenSpecialities;
    }

    public boolean isCertificateAvailable() {
        return isCertificateAvailable;
    }

    public void setCertificateAvailable(boolean certificateAvailable) {
        isCertificateAvailable = certificateAvailable;
    }

    @Override
    public String toString() {
        return "CourseDto{" +
                "name='" + name + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", startCourse=" + startCourse +
                ", endCourse=" + endCourse +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", imageId='" + imageId + '\'' +
                ", specialities='" + chosenSpecialities + '\'' +
                '}';
    }
}
