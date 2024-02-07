package agh.edu.pl.thumbnail.app.services.mqtt.models;

import agh.edu.pl.thumbnail.app.enums.ProcessingStatus;
import agh.edu.pl.thumbnail.app.models.QueueItem;
import agh.edu.pl.thumbnail.app.utils.RestUtils;

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
