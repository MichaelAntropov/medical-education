package me.hizencode.mededu.course.test.media;

import java.util.Optional;

public interface CourseTestMediaService {

    void save(CourseTestMediaEntity courseTestMediaEntity);

    CourseTestMediaEntity saveAndReturn(CourseTestMediaEntity courseTestMediaEntity);

    void delete(CourseTestMediaEntity courseTestMediaEntity);

    void deleteById(Integer mediaId);

    Optional<CourseTestMediaEntity> findById(Integer mediaId);
}
