package pl.edu.agh.to.thumbnails.server.thumbnails;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.agh.to.thumbnails.server.images.models.Image;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;

import static org.powermock.api.mockito.PowerMockito.mock;


@SpringBootTest(classes = ThumbnailUnitTests.class)
public class ThumbnailUnitTests {

    @Test
    public void thumbnailCreatorTest() {
        Image img = mock(Image.class);
        Thumbnail thumbnail = new Thumbnail(img);
        Assertions.assertEquals(img, thumbnail.getImage());
    }
}