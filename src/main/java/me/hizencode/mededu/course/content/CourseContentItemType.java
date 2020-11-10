package me.hizencode.mededu.course.content;

public enum CourseContentItemType {

    TEST("test"),
    LESSON("lesson");

    public final String value;

    CourseContentItemType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
