package me.hizencode.mededu.dashboard.admin.dto;

import javax.validation.constraints.Size;
import java.util.List;

public class QuestionJson {

    private Integer id;

    private Integer testId;

    private Integer orderNumber;

    private Boolean isEdited;

    private String content;

    @Size(min = 1)
    private List<AnswerJson> answers;

    public QuestionJson() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
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

    @Override
    public String toString() {
        return "QuestionJson{" +
                "id=" + id +
                ", testId=" + testId +
                ", orderNumber=" + orderNumber +
                ", isEdited=" + isEdited +
                ", content='" + content + '\'' +
                ", answers=" + answers +
                '}';
    }
}
