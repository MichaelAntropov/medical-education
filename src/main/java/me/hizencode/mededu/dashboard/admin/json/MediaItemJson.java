package me.hizencode.mededu.dashboard.admin.json;

import me.hizencode.mededu.course.content.test.media.CourseTestMediaEntity;

public class MediaItemJson {

    private int id;
    private int testId;
    private String name;
    private String type;
    private String src;

    public MediaItemJson() {
    }

    public MediaItemJson(CourseTestMediaEntity courseTestMediaEntity) {
        setId(courseTestMediaEntity.getId());
        setName(courseTestMediaEntity.getName());
        setType(courseTestMediaEntity.getType());
        setSrc("/course/test/media/image" + courseTestMediaEntity.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return "MediaItemJson{" +
                "id=" + id +
                ", testId=" + testId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", src='" + src + '\'' +
                '}';
    }
}
