package me.hizencode.mededu.course;

public enum LearningItemType {

    TEST("test"),
    LESSON("lesson");

    public final String type;

    LearningItemType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
