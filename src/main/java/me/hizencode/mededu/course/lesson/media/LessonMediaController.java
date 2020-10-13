package me.hizencode.mededu.course.lesson.media;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
public class LessonMediaController {
    /*Fields*/
    /*================================================================================================================*/
    private LessonMediaService lessonMediaService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setLessonMediaService(LessonMediaService lessonMediaService) {
        this.lessonMediaService = lessonMediaService;
    }

    /*Methods*/
    /*================================================================================================================*/
    @GetMapping("/course/lesson/media/image{imageId}")
    public void showCourseImage(@PathVariable Integer imageId,
                                HttpServletResponse response) {

        //TODO: Add authentication logic

        Optional<LessonMediaEntity> optionalImage = lessonMediaService.findById(imageId);

        if (optionalImage.isPresent()) {
            response.setContentType(optionalImage.get().getType());

            InputStream stream = new ByteArrayInputStream(optionalImage.get().getData());

            try {
                IOUtils.copy(stream, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/course/lesson/media/pdf{pdfId}")
    public void showCoursePdfFile(@PathVariable Integer pdfId,
                                HttpServletResponse response) {

        //TODO: Add authentication logic

        Optional<LessonMediaEntity> optionalPdfFile = lessonMediaService.findById(pdfId);

        if (optionalPdfFile.isPresent()) {
            response.setContentType(optionalPdfFile.get().getType());

            InputStream stream = new ByteArrayInputStream(optionalPdfFile.get().getData());

            try {
                IOUtils.copy(stream, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/user-dashboard/manage-courses/course/lesson/media/image{imageId}")
    public void showCourseImageAdmin(@PathVariable Integer imageId,
                                HttpServletResponse response) {

        Optional<LessonMediaEntity> optionalImage = lessonMediaService.findById(imageId);

        if (optionalImage.isPresent()) {
            response.setContentType(optionalImage.get().getType());

            InputStream stream = new ByteArrayInputStream(optionalImage.get().getData());

            try {
                IOUtils.copy(stream, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/user-dashboard/manage-courses/course/lesson/media/pdf{pdfId}")
    public void showCoursePdfAdmin(@PathVariable Integer pdfId,
                                     HttpServletResponse response) {

        Optional<LessonMediaEntity> optionalPdfFile = lessonMediaService.findById(pdfId);

        if (optionalPdfFile.isPresent()) {
            response.setContentType(optionalPdfFile.get().getType());

            InputStream stream = new ByteArrayInputStream(optionalPdfFile.get().getData());

            try {
                IOUtils.copy(stream, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
