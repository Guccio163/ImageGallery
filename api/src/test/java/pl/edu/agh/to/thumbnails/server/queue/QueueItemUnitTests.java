package pl.edu.agh.to.thumbnails.server.queue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.agh.to.thumbnails.server.queue.models.ProcessingStatus;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = QueueItemUnitTests.class)
public class QueueItemUnitTests {

    private final byte[] exampleImageBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    @Test
    public void imageProcessedSuccessfully() {
        //Given
        byte[] img2 = {10, 9,8 , 7, 6, 5, 4, 3, 2, 1};

        var thumbnail = new Thumbnail();

        QueueItem queue = new QueueItem(thumbnail, ThumbnailSize.INVALID.getSize());
        QueueItem queue2 = new QueueItem(thumbnail, ThumbnailSize.INVALID.getSize());
        //When
        queue.processingSucceeded(exampleImageBytes);
        queue2.processingSucceeded(img2);

        // Then
        assertEquals(ProcessingStatus.PROCESSED, queue.getProcessingStatus());
        assertEquals(ProcessingStatus.PROCESSED, queue2.getProcessingStatus());
    }

    @Test
    public void imageProcessingStarted() {
        //Given
        QueueItem queue = new QueueItem();
        //When
        queue.processingStarted();

        // Then
        assertEquals(ProcessingStatus.PROCESSING, queue.getProcessingStatus());
    }

    @Test
    public void imageProcessingFailed() {
        //Given
        QueueItem queue = new QueueItem();
        //When
        queue.processingFailed();

        // Then
        assertEquals(ProcessingStatus.FAILED, queue.getProcessingStatus());
    }
}


