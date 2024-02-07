package pl.edu.agh.to.thumbnails.server.processing;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.agh.to.thumbnails.server.TestsCommon;
import pl.edu.agh.to.thumbnails.server.images.models.Image;
import pl.edu.agh.to.thumbnails.server.mocks.NotifierMock;
import pl.edu.agh.to.thumbnails.server.mocks.ProcessingQueueMock;
import pl.edu.agh.to.thumbnails.server.notifying.interfaces.INotifier;
import pl.edu.agh.to.thumbnails.server.queue.interfaces.IProcessingQueue;
import pl.edu.agh.to.thumbnails.server.queue.interfaces.QueueItemRepository;
import pl.edu.agh.to.thumbnails.server.queue.models.ProcessingStatus;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.thumbnails.interfaces.ThumbnailRepository;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.utils.RestUtils;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static pl.edu.agh.to.thumbnails.server.TestsCommon.THREAD_DELAY;

@SpringBootTest(classes = BackgroundProcessorUnitTests.class)
public class BackgroundProcessorUnitTests {
    @Mock
    private IProcessingQueue processingQueue = new ProcessingQueueMock();

    @Mock
    private QueueItemRepository queueItemRepository;

    @Mock
    private ThumbnailRepository thumbnailRepository;

    @Mock
    private INotifier thumbnailNotifier = new NotifierMock();

    @InjectMocks
    private BackgroundImageProcessor backgroundImageProcessor;

    @Test
    public void processTaskProcesses() {
        // Given
        var bytes = RestUtils.decode(TestsCommon.VALID_IMAGE);
        var image = new Image(bytes);
        var thumbnail = new Thumbnail(image);
        var queueItem = new QueueItem(thumbnail, ThumbnailSize.SMALL.getSize());

        // When
        CompletableFuture<Void> future = backgroundImageProcessor.processingTask(queueItem, thumbnail);

        // Then
        assertDoesNotThrow(() -> future.join());
        assertEquals(ProcessingStatus.PROCESSED, queueItem.getProcessingStatus());
        assertNotEquals(null, thumbnail.getImage());
    }

    @Test
    public void processTaskFails() {
        // Given
        var bytes = RestUtils.decode(TestsCommon.INVALID_IMAGE);
        var image = new Image(bytes);
        var thumbnail = new Thumbnail(image);
        var queueItem = new QueueItem(thumbnail, ThumbnailSize.SMALL.getSize());

        // When
        CompletableFuture<Void> future = backgroundImageProcessor.processingTask(queueItem, thumbnail);

        // Then
        assertDoesNotThrow(() -> future.join());
        assertEquals(ProcessingStatus.FAILED, queueItem.getProcessingStatus());
    }
}
