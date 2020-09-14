package me.hizencode.mededu.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    /*Fields*/
    /*================================================================================================================*/
    private ImageRepository imageRepository;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /*Methods*/
    /*================================================================================================================*/
    @Override
    @Transactional
    public void store(ImageEntity image) {
        imageRepository.save(image);
    }

    @Override
    public Optional<ImageEntity> findById(Integer id) {
        return imageRepository.findById(id);
    }
}
