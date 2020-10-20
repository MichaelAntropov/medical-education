package me.hizencode.mededu.dashboard.admin.dto;

import java.util.List;

public class TestDataResponseJson {

    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
                ", questions=" + questions +
                '}';
    }
}
