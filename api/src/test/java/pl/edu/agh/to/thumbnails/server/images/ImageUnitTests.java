package pl.edu.agh.to.thumbnails.server.images;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.agh.to.thumbnails.server.images.models.Image;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ImageUnitTests.class)
public class ImageUnitTests {

    private final byte[] exampleImageBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    @Test
    public void imageCreatedAtIsSet(){
        //Given
        var image = new Image(exampleImageBytes);
        //When
        var time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        //Then
        assertEquals(time, image.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void createImageWithImage() {
        //Given
        Image image = new Image(exampleImageBytes);
        //When

        //Then
        assertEquals(exampleImageBytes, image.getImage());
    }

    @Test
    public void addThumbnailsToImage() {
        //Given
        Image image = new Image(exampleImageBytes);
        //When

        image.addThumbnail(new Thumbnail());
        image.addThumbnail(new Thumbnail());
        image.addThumbnail(new Thumbnail());

        //Then
        assertEquals(image.getThumbnails().size(), 3);
    }
}