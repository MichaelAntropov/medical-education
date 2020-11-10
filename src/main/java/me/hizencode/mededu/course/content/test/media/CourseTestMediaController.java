package me.hizencode.mededu.course.content.test.media;

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
public class CourseTestMediaController {
    /*Fields*/
    /*================================================================================================================*/
    private CourseTestMediaService testMediaService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setTestMediaService(CourseTestMediaService testMediaService) {
        this.testMediaService = testMediaService;
    }

    /*Methods*/
    /*================================================================================================================*/
    @GetMapping("/course/test/media/image{imageId}")
    public void showTestImage(@PathVariable Integer imageId,
                              HttpServletResponse response) {

        //TODO: Add authentication logic

        Optional<CourseTestMediaEntity> optionalImage = testMediaService.findById(imageId);

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
}
