package me.hizencode.mededu.dashboard.admin.json;

import java.util.List;

public class TestDataResponseJson {

    private String message;

    private Integer requiredScore;

    private List<QuestionJson> questions;

    public TestDataResponseJson() {
    }

    public TestDataResponseJson(String message) {
        this.message = message;
    }

    public TestDataResponseJson(String message, List<QuestionJson> questions) {
        this.message = message;
        this.questions = questions;
    }

    public TestDataResponseJson(String message, Integer requiredScore, List<QuestionJson> questions) {
        this.message = message;
        this.requiredScore = requiredScore;
        this.questions = questions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRequiredScore() {
        return requiredScore;
    }

    public void setRequiredScore(Integer requiredScore) {
        this.requiredScore = requiredScore;
    }

    public List<QuestionJson> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionJson> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "TestDataResponseJson{" +
                "message='" + message + '\'' +
                ", requiredScore=" + requiredScore +
                ", questions=" + questions +
                '}';
    }
}
