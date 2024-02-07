package pl.edu.agh.to.thumbnails.server.notifying.models;

import pl.edu.agh.to.thumbnails.server.queue.models.ProcessingStatus;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;

import java.time.LocalDateTime;

public class Notification {
    private String processingStatus;
    private LocalDateTime processedAt;

    public Notification(ProcessingStatus status, LocalDateTime processedAt) {
        this.processingStatus = status.toString();
        this.processedAt = processedAt;
    }

    public String getProcessingStatus(){ return processingStatus; }

    public static Notification failNotification(QueueItem item){
        return new Notification(item.getProcessingStatus(), item.getProcessedAt());
    }
}
