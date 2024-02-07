package agh.edu.pl.thumbnail.app.models;

import agh.edu.pl.thumbnail.app.enums.ProcessingStatus;
import agh.edu.pl.thumbnail.app.enums.ThumbnailSize;

import java.time.LocalDateTime;

public class QueueItem {

    private Long id;

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
    public void setId(Long id) {
        this.id = id;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public void setHeight(ThumbnailSize size) {
        this.height = size.getSize();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime date) {
        this.createdAt = date;
    }
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    public void setProcessedAt(LocalDateTime date) {
        this.processedAt = date;
    }
    public ProcessingStatus getProcessingStatus() {
        return processingStatus;
    }
    public void setProcessingStatus(ProcessingStatus status) {
        this.processingStatus = status;
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

