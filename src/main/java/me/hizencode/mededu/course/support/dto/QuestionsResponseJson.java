package me.hizencode.mededu.course.support.dto;

import java.util.List;

public class QuestionsResponseJson {

    private String message;

    private List<QuestionJson> questions;

    public QuestionsResponseJson(String message) {
        this.message = message;
    }

    public QuestionsResponseJson(String message, List<QuestionJson> questions) {
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
}
