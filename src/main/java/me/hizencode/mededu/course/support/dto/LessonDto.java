package me.hizencode.mededu.course.support.dto;

public class LessonDto {

    private String title;

    private String content;

    private Integer nextItemId;

    private String nextItemType;

    public LessonDto() {
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

    public Integer getNextItemId() {
        return nextItemId;
    }

    public void setNextItemId(Integer nextItemId) {
        this.nextItemId = nextItemId;
    }

    public String getNextItemType() {
        return nextItemType;
    }

    public void setNextItemType(String nextItemType) {
        this.nextItemType = nextItemType;
    }
}
