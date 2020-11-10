package me.hizencode.mededu.dashboard.admin.dto;

import me.hizencode.mededu.course.content.lesson.media.LessonMediaEntity;

import java.util.List;

public class AdminLessonDto {

    private int id;

    private int courseId;

    private String title;

    private String content;

    private List<LessonMediaEntity> mediaList;

    public AdminLessonDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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

    public List<LessonMediaEntity> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<LessonMediaEntity> mediaList) {
        this.mediaList = mediaList;
    }

    @Override
    public String toString() {
        return "AdminLessonDto{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
