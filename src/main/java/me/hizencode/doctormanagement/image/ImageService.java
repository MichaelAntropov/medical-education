package me.hizencode.doctormanagement.image;

import java.util.Optional;

public interface ImageService {

    void store(ImageEntity image);

    Optional<ImageEntity> findById(Integer id);
}
