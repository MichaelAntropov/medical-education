package me.hizencode.mededu.course.content.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseUserRepository extends JpaRepository<CourseUserEntity, Integer> {

    Optional<CourseUserEntity> findByUserIdAndCourseId(Integer userId, Integer courseId);

}
