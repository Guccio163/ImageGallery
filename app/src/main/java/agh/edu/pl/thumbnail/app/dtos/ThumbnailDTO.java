package agh.edu.pl.thumbnail.app.dtos;

import agh.edu.pl.thumbnail.app.enums.ProcessingStatus;

import java.time.LocalDateTime;

public class ThumbnailDTO {

    private Long thumbnailId;
    private Long imageId;
    private String thumbnail;
    private String size;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private ProcessingStatus processingStatus;

    public ThumbnailDTO(){}

    public Long getThumbnailId() {
        return thumbnailId;
    }
    public void setThumbnailId(Long thumbnailId) {
        this.thumbnailId = thumbnailId;
    }


    public Long getImageId() {
        return imageId;
    }
    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public ProcessingStatus getProcessingStatus() {
        return processingStatus;
    }
    public void setProcessingStatus(ProcessingStatus status) {
        this.processingStatus = status;
    }
}
