package me.hizencode.mededu.course.support.dto;

public class AnswerJson {

    private int answerId;

    private String content;

    public AnswerJson(int answerId, String content) {
        this.answerId = answerId;
        this.content = content;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
