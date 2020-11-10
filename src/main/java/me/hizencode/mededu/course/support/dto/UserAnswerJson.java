package me.hizencode.mededu.course.support.dto;

public class UserAnswerJson {

    private int questionId;

    private int answerId;

    public UserAnswerJson() {
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    @Override
    public String toString() {
        return "UserAnswerJson{" +
                "questionId=" + questionId +
                ", answerId=" + answerId +
                '}';
    }
}
