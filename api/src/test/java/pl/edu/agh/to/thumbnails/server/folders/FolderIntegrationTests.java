package pl.edu.agh.to.thumbnails.server.folders;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import pl.edu.agh.to.thumbnails.server.TestsCommon;
import pl.edu.agh.to.thumbnails.server.folders.requests.AddFolderRequest;
import pl.edu.agh.to.thumbnails.server.folders.requests.AddImagesToFolderRequest;
import pl.edu.agh.to.thumbnails.server.folders.responses.GetFoldersResponses;
import pl.edu.agh.to.thumbnails.server.images.models.ImageDTO;
import pl.edu.agh.to.thumbnails.server.images.models.response.ImageListDTO;
import pl.edu.agh.to.thumbnails.server.processing.models.request.ProcessImageRequest;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessImageResponse;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FolderIntegrationTests {

    private static final String existingFolderName = "folder1";

    @Autowired
    private TestRestTemplate restTemplate;
    @BeforeEach
    public void setUp()  {

        //Given
        var payload1 = new ProcessImageRequest(TestsCommon.VALID_IMAGE);
        var addFolderRequest = new AddFolderRequest(existingFolderName);

        //When
        var response1 = this.restTemplate.postForEntity("/process/image", payload1, ProcessImageResponse.class);
        var response3 = this.restTemplate.postForEntity("/process/image", payload1, ProcessImageResponse.class);

        var r2 = this.restTemplate.postForEntity("/folders/add", addFolderRequest, Object.class);

        //Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response1.getStatusCode().value());
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response3.getStatusCode().value());
        assertEquals(TestsCommon.NO_CONTENT, r2.getStatusCode().value());
    }

    @AfterEach
    public void tearDown() {
        var response = restTemplate.getForEntity("/images/all", ImageListDTO.class);

        for(var item : Objects.requireNonNull(response.getBody()).getImages())
            restTemplate.delete("/images/"+item.getId());


        var response2 = restTemplate.getForEntity("/folders", GetFoldersResponses.class);

        for(var item : Objects.requireNonNull(response2.getBody()).getFolders())
            restTemplate.delete("/folders/"+item.getId());

    }

    @Order(1)
    @Test
    public void canGetFolders(){
        //Given

        //When
        var response = restTemplate.getForEntity("/folders", GetFoldersResponses.class);

        //Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getFolders().size());
    }

    @Order(2)
    @Test
    public void canAddFolder(){
        //Given
        var folder = new AddFolderRequest("new test folder");
        //When
        var response = restTemplate.getForEntity("/folders", GetFoldersResponses.class);
        var addResponse = this.restTemplate.postForEntity("/folders/add", folder, Object.class);
        var response2 = restTemplate.getForEntity("/folders", GetFoldersResponses.class);

        //Then

        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getFolders().size());

        assertEquals(TestsCommon.NO_CONTENT, addResponse.getStatusCode().value());

        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response2.getStatusCode().value());
        assertEquals(2, Objects.requireNonNull(response2.getBody()).getFolders().size());
    }

    @Order(3)
    @Test
    public void canNotAddSameFolder(){
        //Given
        var folder = new AddFolderRequest(existingFolderName);
        //When
        var response = restTemplate.getForEntity("/folders", GetFoldersResponses.class);
        var addResponse = this.restTemplate.postForEntity("/folders/add", folder, Object.class);
        var response2 = restTemplate.getForEntity("/folders", GetFoldersResponses.class);
        //Then

        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getFolders().size());

        assertEquals(TestsCommon.CONFLICT, addResponse.getStatusCode().value());

        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response2.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response2.getBody()).getFolders().size());
    }

    @Order(4)
    @Test
    public void canDeleteFolder(){
        //Given
        var response = restTemplate.getForEntity("/folders", GetFoldersResponses.class);
        var folderId = Objects.requireNonNull(response.getBody()).getFolders().get(0).getId();

        //When
        this.restTemplate.delete("/folders/"+folderId);
        var response2 = restTemplate.getForEntity("/folders", GetFoldersResponses.class);

        //Then
        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getFolders().size());

        assertEquals(TestsCommon.SUCCESSFUL_STATUS_CODE, response2.getStatusCode().value());
        assertEquals(0, Objects.requireNonNull(response2.getBody()).getFolders().size());
    }

}
