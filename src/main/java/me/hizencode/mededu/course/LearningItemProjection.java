package me.hizencode.mededu.course;

import org.springframework.beans.factory.annotation.Value;

public interface LearningItemProjection {

    int getId();

    void setId(Integer id);

    String getTitle();

    void setTitle(String title);

    int getOrderNumber();

    void setOrderNumber(Integer orderNumber);

    @Value("#{target.getClass().getSimpleName() == 'LessonEntity' ? 'lesson' : 'test'}")
    String getType();
}
