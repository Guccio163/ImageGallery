package pl.edu.agh.to.thumbnails.server.notifying.models;

import pl.edu.agh.to.thumbnails.server.queue.models.ProcessingStatus;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.utils.RestUtils;

import java.time.LocalDateTime;

public class SuccessNotification extends Notification {

    private String image;

    public SuccessNotification(LocalDateTime processedAt, byte[] image) {
        super(ProcessingStatus.PROCESSED, processedAt);
        this.image = RestUtils.encode(image);
    }

    public String getImage() {
        return image;
    }

    public static SuccessNotification notification(QueueItem item, byte[] image){
        return new SuccessNotification(item.getProcessedAt(), image);
    }
}
