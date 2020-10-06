package me.hizencode.mededu.lessons.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonMediaServiceImpl implements LessonMediaService {

    /*Fields*/
    /*================================================================================================================*/
    private LessonMediaRepository lessonMediaRepository;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setLessonMediaRepository(LessonMediaRepository lessonMediaRepository) {
        this.lessonMediaRepository = lessonMediaRepository;
    }

    /*Methods*/
    /*================================================================================================================*/
    @Override
    public Optional<LessonMediaEntity> findById(Integer id) {
        return lessonMediaRepository.findById(id);
    }

    @Override
    public void save(LessonMediaEntity lessonMediaEntity) {
        lessonMediaRepository.save(lessonMediaEntity);
    }

    @Override
    public List<LessonMediaEntity> findAllByLessonId(Integer id) {
        return lessonMediaRepository.findAllByLesson_Id(id);
    }

    @Override
    public void delete(LessonMediaEntity lessonMediaEntity) {
        lessonMediaRepository.delete(lessonMediaEntity);
    }
}
