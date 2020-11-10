package me.hizencode.mededu.course.support.dto;

public class CourseContentItemDto {

    private int id;

    private String type;

    private int orderNumber;

    private int completed;

    private String title;

    public CourseContentItemDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CourseContentItemDto{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", orderNumber=" + orderNumber +
                ", completed=" + completed +
                ", title='" + title + '\'' +
                '}';
    }
}
