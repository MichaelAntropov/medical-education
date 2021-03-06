package me.hizencode.mededu.course.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {

    Optional<ImageEntity> findById(Integer id);
}
