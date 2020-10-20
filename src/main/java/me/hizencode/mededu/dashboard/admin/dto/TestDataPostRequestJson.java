package me.hizencode.mededu.dashboard.admin.dto;

import java.util.List;

public class TestDataPostRequestJson {

    private Integer testId;

    private List<QuestionJson> questions;

    private List<Integer> questionsToDelete;

    private List<Integer> answersToDelete;

    public TestDataPostRequestJson() {

    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public List<QuestionJson> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionJson> questions) {
        this.questions = questions;
    }

    public List<Integer> getQuestionsToDelete() {
        return questionsToDelete;
    }

    public void setQuestionsToDelete(List<Integer> questionsToDelete) {
        this.questionsToDelete = questionsToDelete;
    }

    public List<Integer> getAnswersToDelete() {
        return answersToDelete;
    }

    public void setAnswersToDelete(List<Integer> answersToDelete) {
        this.answersToDelete = answersToDelete;
    }

    @Override
    public String toString() {
        return "TestDataPostRequestJson{" +
                "questions=" + questions +
                ", questionsToDelete=" + questionsToDelete +
                ", answersToDelete=" + answersToDelete +
                '}';
    }
}
