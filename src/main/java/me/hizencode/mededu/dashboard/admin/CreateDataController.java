package me.hizencode.mededu.dashboard.admin;

import me.hizencode.mededu.courses.CourseDescriptionEntity;
import me.hizencode.mededu.courses.CourseDetailEntity;
import me.hizencode.mededu.courses.CourseEntity;
import me.hizencode.mededu.courses.CourseService;
import me.hizencode.mededu.image.ImageEntity;
import me.hizencode.mededu.rest.RestData;
import me.hizencode.mededu.rest.photos.PhotoData;
import me.hizencode.mededu.specialities.SpecialityEntity;
import me.hizencode.mededu.specialities.SpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class CreateDataController {

    private RestData restData;

    private CourseService courseService;

    private SpecialityService specialityService;

    private final WebClient webClient;


    public CreateDataController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Autowired
    public void setRestData(RestData restData) {
        this.restData = restData;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setSpecialityService(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @GetMapping("user-dashboard/manage-courses/create-courses")
    private String createCourses() throws InterruptedException {

        int amountOfCourses = 1;

        Mono<PhotoData> photoDataMono = restData.getImages(amountOfCourses, 2);
        PhotoData photoData = photoDataMono.block();
        ArrayList<ImageEntity> imageEntities = new ArrayList<>();
        ArrayList<String> videos = getVideos();
        ArrayList<SpecialityEntity> specialityEntities = specialityService.getAllSpecialities();

        if (photoData != null) {
            photoData.getPhotos().forEach(photo -> {
                try {
                    imageEntities.add(getImage(photo.getSrc().getLarge()));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        for (int i = 0; i < amountOfCourses; i++) {
            System.out.println("Creating course: " + i);
            CourseDescriptionEntity courseDescriptionEntity = new CourseDescriptionEntity();
            CourseDetailEntity courseDetailEntity = new CourseDetailEntity();
            CourseEntity courseEntity = new CourseEntity();

            courseEntity.setName(restData.getTextWords(2, 7));
            courseEntity.setCreationDate(randomCreationDate());
            courseEntity.setEditDate(randomEditDate());
            courseEntity.setSpecialities(getRandomSpecialities(specialityEntities));

            courseDetailEntity.setImage(imageEntities.get(i));
            courseDetailEntity.setVideoUrl(videos.get(i));
            courseDetailEntity.setStartCourse(randomStartDate());
            courseDetailEntity.setEndCourse(randomEndDate());
            courseDetailEntity.setAuthor(restData.getTextWords(2, 6));

            courseDescriptionEntity.setText(restData.getTextParagraphs(2, 5));


            courseService.saveNewCourse(courseEntity, courseDetailEntity, courseDescriptionEntity);

            Thread.sleep(100);
        }

        return "redirect:/";
    }

    private ImageEntity getImage(String urlString) throws IOException, InterruptedException {
        URL url = new URL(urlString);
        InputStream is = readAsInputStream(urlString);

        ImageEntity imageEntity = new ImageEntity();

        imageEntity.setType("image/jpeg");

        String path = url.getPath();
        imageEntity.setName(path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".")));

        imageEntity.setData(is.readAllBytes());

        Thread.sleep(100);

        return imageEntity;
    }

    //https://stackoverflow.com/a/58272048/13556506
    private InputStream readAsInputStream(String url) throws IOException {
        PipedOutputStream osPipe = new PipedOutputStream();
        PipedInputStream isPipe = new PipedInputStream(osPipe);

        ClientResponse response = webClient.get().uri(url)
                .exchange()
                .block();

        final int statusCode = response.rawStatusCode();
        // check HTTP status code, can throw exception if needed
        // ....
        if (statusCode != 200) {
            throw new IllegalStateException("Status code: " + statusCode);
        }

        Flux<DataBuffer> body = response.body(BodyExtractors.toDataBuffers())
                .doOnError(t -> {
                    System.out.println(t.getMessage());
                    // close pipe to force InputStream to error,
                    // otherwise the returned InputStream will hang forever if an error occurs
                    try {
                        isPipe.close();
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }
                })
                .doFinally(s -> {
                    try{
                        osPipe.close();
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }
                });

        DataBufferUtils.write(body, osPipe)
                .subscribe(DataBufferUtils.releaseConsumer());

        return isPipe;
    }

    private ArrayList<SpecialityEntity> getRandomSpecialities(ArrayList<SpecialityEntity> specialityEntities) {
        ArrayList<SpecialityEntity> result = new ArrayList<>();

        for (SpecialityEntity specialityEntity : specialityEntities) {
            if (Math.random() < 0.15) {
                result.add(specialityEntity);
            }
        }

        return result;
    }

    private Date randomCreationDate() {
        return new Date(1600215200021L + (long) (864000L * Math.random()));
    }

    private Date randomEditDate() {
        return new Date(1600635200021L + (long) (864000L * Math.random()) + (long) (134323L * Math.random()));
    }

    private Date randomStartDate() {
        return new Date(1600646400021L + (long) (864000000L * Math.random()));
    }

    private Date randomEndDate() {
        return new Date(1600646400021L + (long) (864000000L * Math.random()) + (long) (7776000000L * Math.random()));
    }

    private ArrayList<String> getVideos() {
        ArrayList<String> videos = new ArrayList<>();

        videos.add("https://www.youtube.com/watch?v=NPnLxESfv50&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=_QM4znwyFNo&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=ZATMh49j49M&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=iTSpmnHMVS4&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=J4x_Gj4LoCU&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=NPnLxESfv50&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=_QM4znwyFNo&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=ZATMh49j49M&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=iTSpmnHMVS4&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=J4x_Gj4LoCU&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=NPnLxESfv50&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=_QM4znwyFNo&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=ZATMh49j49M&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=iTSpmnHMVS4&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=J4x_Gj4LoCU&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=NPnLxESfv50&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=_QM4znwyFNo&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=ZATMh49j49M&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=iTSpmnHMVS4&ab_channel=AudioLibrary—Musicforcontentcreators");
        videos.add("https://www.youtube.com/watch?v=J4x_Gj4LoCU&ab_channel=AudioLibrary—Musicforcontentcreators");

        return videos;
    }
}
