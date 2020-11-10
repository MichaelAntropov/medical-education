package me.hizencode.mededu.course.support.dto;

public class TestDto {

    private Integer completed;

    private Integer questionCount;

    private Integer userScore;

    private Integer requiredScore;

    private String title;

    private String content;

    private Integer nextItemId;

    private String nextItemType;

    public TestDto() {
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(Integer userScore) {
        this.userScore = userScore;
    }

    public Integer getRequiredScore() {
        return requiredScore;
    }

    public void setRequiredScore(Integer requiredScore) {
        this.requiredScore = requiredScore;
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

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
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
