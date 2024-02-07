package pl.edu.agh.to.thumbnails.server.processing;

import jakarta.transaction.Transactional;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.thumbnails.server.notifying.interfaces.INotifier;
import pl.edu.agh.to.thumbnails.server.queue.interfaces.IProcessingQueue;
import pl.edu.agh.to.thumbnails.server.queue.interfaces.QueueItemRepository;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.thumbnails.interfaces.ThumbnailRepository;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

@Component
public class BackgroundImageProcessor {

    private static final Logger log = LoggerFactory.getLogger(ImageProcessorController.class);

    public final static int BACKGROUND_PROCESSOR_SPAN = 100;
    public final static String IMAGE_EXTENSION = "png";

    @Autowired
    private IProcessingQueue processingQueue;

    @Autowired
    private QueueItemRepository queueItemRepository;

    @Autowired
    private ThumbnailRepository thumbnailRepository;

    @Autowired
    private INotifier thumbnailNotifier;

    @Scheduled(fixedRate = BACKGROUND_PROCESSOR_SPAN)
    public void processImage() {

        var item = processingQueue.getItem();

        if(item.succeded())
        {
            var queueItem = item.getData();
            var thumbnail = queueItem.getThumbnail();

            processingTask(queueItem, thumbnail);
        }
    }

    @Transactional
    public CompletableFuture<Void> processingTask(QueueItem queueItem, Thumbnail thumbnail)
    {
        return CompletableFuture.runAsync(() ->
        {
            try
            {
                var image = queueItem.getThumbnail().getImage();
                ByteArrayInputStream stream = new ByteArrayInputStream(image.getImage());
                BufferedImage originalImage = ImageIO.read(stream);

                BufferedImage resizedImage = Scalr.resize(originalImage, queueItem.getHeight());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, IMAGE_EXTENSION, outputStream);
                var imageBytes = outputStream.toByteArray();

                queueItem.processingSucceeded(imageBytes);
                queueItemRepository.save(queueItem);
                thumbnailRepository.save(thumbnail);
                thumbnailNotifier.notifySuccess(thumbnail, queueItem);
                log.info("Processed thumbnail with id: " + thumbnail.getId());
            }
            catch (Exception e)
            {
                log.warn("Processing failed for thumbnail with id:" + thumbnail.getId());
                queueItem.processingFailed();
                thumbnailNotifier.notifyFail(thumbnail, queueItem);
                queueItemRepository.save(queueItem);
            }
        });
    }
}
