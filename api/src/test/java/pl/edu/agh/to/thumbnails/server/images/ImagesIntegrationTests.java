package pl.edu.agh.to.thumbnails.server.images;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import pl.edu.agh.to.thumbnails.server.TestsCommon;
import pl.edu.agh.to.thumbnails.server.images.interfaces.ImageRepository;
import pl.edu.agh.to.thumbnails.server.images.models.FullImageDTO;
import pl.edu.agh.to.thumbnails.server.images.models.response.ImageListDTO;
import pl.edu.agh.to.thumbnails.server.images.models.response.ImageWithThumbnails;
import pl.edu.agh.to.thumbnails.server.images.models.response.ThumbnailsListDTO;
import pl.edu.agh.to.thumbnails.server.processing.models.request.ProcessImageRequest;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessImageResponse;
import pl.edu.agh.to.thumbnails.server.queue.interfaces.QueueItemRepository;
import pl.edu.agh.to.thumbnails.server.queue.models.ProcessingStatus;
import pl.edu.agh.to.thumbnails.server.thumbnails.interfaces.ThumbnailRepository;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.ThumbnailDTO;


import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImagesIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUpImages() throws InterruptedException {
        //Given
        var payload1 = new ProcessImageRequest(TestsCommon.VALID_IMAGE);
        var payload2 = new ProcessImageRequest(TestsCommon.INVALID_IMAGE);

        //When
        var response1 = this.restTemplate.postForEntity("/process/image", payload1, ProcessImageResponse.class);
        var response2 = this.restTemplate.postForEntity("/process/image", payload2, ProcessImageResponse.class);
        var response3 = this.restTemplate.postForEntity("/process/image", payload1, ProcessImageResponse.class);
        var response4 = this.restTemplate.postForEntity("/process/image", payload2, ProcessImageResponse.class);

        //Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response1.getStatusCode().value());
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response2.getStatusCode().value());
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response3.getStatusCode().value());
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response4.getStatusCode().value());

        Thread.sleep(2000);
    }

    @AfterEach
    public void tearDown() {
        var response = restTemplate.getForEntity("/images/all", ImageListDTO.class);

        for(var item : response.getBody().getImages())
            restTemplate.delete("/images/"+item.getId());

    }

    @Test
    @Order(6)
    public void imagesEndpointWorking() {
        // Given

        // When
        var response = restTemplate.getForEntity("/images/ping", String.class);

        // Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(ImageController.PING_RESPONSE, response.getBody());
    }

    @Test
    @Order(2)
    public void getAllReturningValidNumberOfEntities() {
        // Given

        // When
        var response = restTemplate.getForEntity("/images/all", ImageListDTO.class);

        // Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(4, response.getBody().getImages().size());
    }

    @Test
    @Order(4)
    public void getFullImage() {
        // Given
        var response = restTemplate.getForEntity("/images/all", ImageListDTO.class);
        var imageId = response.getBody().getImages().get(0).getId();

        // When
        var data = restTemplate.getForEntity("/images/"+imageId+"/full", FullImageDTO.class);

        // Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, data.getStatusCode().value());
        var image = data.getBody()
                .getImage();
        assertTrue(image.equals(TestsCommon.INVALID_IMAGE) || image.equals(TestsCommon.VALID_IMAGE));

    }

    @Test
    @Order(5)
    public void getImageWithThumbnails() {
        // Given
        var response = restTemplate.getForEntity("/images/all", ImageListDTO.class);
        var imageId = Objects.requireNonNull(response.getBody()).getImages().get(0).getId();

        // When
        var data = restTemplate.getForEntity("/images/"+imageId+"/thumbnails/full", ImageWithThumbnails.class);

        // Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, data.getStatusCode().value());
        var body = data.getBody();
        assertNotNull(body);
    }

    private boolean isValidThumbnail(ThumbnailDTO dto){
        if (dto.getProcessingStatus() == ProcessingStatus.QUEUED ||
                dto.getProcessingStatus() == ProcessingStatus.PROCESSING)
            return true;
        if(dto.getProcessingStatus() == ProcessingStatus.FAILED)
            return dto.getThumbnail() == null || dto.getThumbnail().isEmpty();

        return !dto.getThumbnail().isEmpty();
    }

    @Test
    @Order(1)
    public void removedImage() {
        // Given
        var response = restTemplate.getForEntity("/images/all", ImageListDTO.class);
        var imageId = Objects.requireNonNull(response.getBody()).getImages().get(0).getId();

        // When
        restTemplate.delete("/images/"+imageId);

        var data = restTemplate.getForEntity("/images/all", ImageListDTO.class);

        // Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, data.getStatusCode().value());
        assertEquals(3, Objects.requireNonNull(data.getBody()).getImages().size());
    }

    @Test
    @Order(7)
    public void gettingAllSmallThumbnails() {
        // Given

        // When
        var data = restTemplate.getForEntity("/images/all/thumbnails/size/small", ThumbnailsListDTO.class);

        // Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, data.getStatusCode().value());
        assertEquals(4, Objects.requireNonNull(data.getBody()).getThumbnails().size());
    }

    @Test
    @Order(8)
    public void getImageThumbnails() {
        // Given
        var response = restTemplate.getForEntity("/images/all", ImageListDTO.class);
        var imageId = Objects.requireNonNull(response.getBody()).getImages().get(0).getId();

        // When
        var data = restTemplate.getForEntity("/images/" + imageId + "/thumbnails", ThumbnailsListDTO.class);

        // Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, data.getStatusCode().value());
        assertEquals(3, Objects.requireNonNull(data.getBody()).getThumbnails().size());
    }

    @Test
    @Order(9)
    public void getImageSpecificThumbnail() {
        // Given
        var response = restTemplate.getForEntity("/images/all", ImageListDTO.class);
        var imageId = Objects.requireNonNull(response.getBody()).getImages().get(0).getId();

        // When
        var data = restTemplate.getForEntity("/images/" + imageId + "/thumbnail/size/small", ThumbnailDTO.class);

        // Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, data.getStatusCode().value());
    }
}
