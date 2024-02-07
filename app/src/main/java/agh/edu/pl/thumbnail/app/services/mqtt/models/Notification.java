package agh.edu.pl.thumbnail.app.services.mqtt.models;

import agh.edu.pl.thumbnail.app.enums.ProcessingStatus;
import agh.edu.pl.thumbnail.app.models.QueueItem;

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
