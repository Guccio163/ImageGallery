package pl.edu.agh.to.thumbnails.server.queue.models;

import jakarta.persistence.*;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;

import java.time.LocalDateTime;

@Entity
public class QueueItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Thumbnail thumbnail;

    private int height;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private ProcessingStatus processingStatus;

    public QueueItem() {
        this.createdAt = LocalDateTime.now();
        this.processedAt = LocalDateTime.now();
    }

    public QueueItem(Thumbnail thumbnail, int height) {
        this();
        this.thumbnail = thumbnail;
        this.thumbnail.setQueueItem(this);
        this.height = height;
        this.processingStatus = ProcessingStatus.QUEUED;
    }

    public Long getId() {
        return id;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public int getHeight() {
        return height;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public ProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void processingFailed() {
        this.processingStatus = ProcessingStatus.FAILED;
        this.processedAt = LocalDateTime.now();
    }

    public void processingSucceeded(byte[] image) {
        this.processingStatus = ProcessingStatus.PROCESSED;
        this.processedAt = LocalDateTime.now();
        this.thumbnail.setThumbnail(image);
    }

    public void processingStarted() {
        this.processingStatus = ProcessingStatus.PROCESSING;
    }

    @Override
    public String toString()
    {
        return "";
    }

}
