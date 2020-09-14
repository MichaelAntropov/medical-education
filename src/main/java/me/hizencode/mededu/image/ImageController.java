package me.hizencode.mededu.image;

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
public class ImageController {
    /*Fields*/
    /*================================================================================================================*/
    private ImageService imageService;

    /*Setters*/
    /*================================================================================================================*/
    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    /*Methods*/
    /*================================================================================================================*/
    @GetMapping("/course/image/{id}")
    public void showCourseImage(@PathVariable Integer id,
                                HttpServletResponse response) {

        Optional<ImageEntity> optionalImage = imageService.findById(id);

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
