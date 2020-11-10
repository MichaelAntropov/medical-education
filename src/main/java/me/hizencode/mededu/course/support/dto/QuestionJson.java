package me.hizencode.mededu.course.support.dto;

import java.util.List;

public class QuestionJson {

    private int questionId;

    private int orderNumber;

    private String content;

    private List<AnswerJson> answers;

    public QuestionJson(int questionId, int orderNumber, String content, List<AnswerJson> answers) {
        this.questionId = questionId;
        this.orderNumber = orderNumber;
        this.content = content;
        this.answers = answers;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AnswerJson> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerJson> answers) {
        this.answers = answers;
    }
}
