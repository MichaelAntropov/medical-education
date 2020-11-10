package me.hizencode.mededu.dashboard.admin.json;

public class AnswerJson {

    private Integer id;

    private Integer questionId;

    private Integer orderNumber;

    private Boolean isCorrect;

    private Boolean isEdited;

    private String content;

    public AnswerJson() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
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

    @Override
    public String toString() {
        return "AnswerJson{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", orderNumber=" + orderNumber +
                ", isEdited=" + isEdited +
                ", content='" + content + '\'' +
                '}';
    }
}
