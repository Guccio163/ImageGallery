package pl.edu.agh.to.thumbnails.server.thumbnails.model;

import pl.edu.agh.to.thumbnails.server.queue.models.ProcessingStatus;
import pl.edu.agh.to.thumbnails.server.utils.RestUtils;

import java.time.LocalDateTime;

public class ThumbnailDTO {

    private Long thumbnailId;
    private Long imageId;
    private String image;
    private String size;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private ProcessingStatus processingStatus;

    public ThumbnailDTO(){}

    public ThumbnailDTO(Thumbnail thumbnail)
    {
        this.imageId = thumbnail.getImage().getId();
        this.thumbnailId = thumbnail.getId();
        this.createdAt = thumbnail.getQueueItem().getCreatedAt();
        this.size = thumbnail.getSize().name();

        this.image = RestUtils.encode(thumbnail.getThumbnail());

        var queueItem = thumbnail.getQueueItem();

        this.processedAt = queueItem.getProcessedAt();
        this.processingStatus = queueItem.getProcessingStatus();
    }

    public Long getThumbnailId() {
        return thumbnailId;
    }

    public Long getImageId() {
        return imageId;
    }

    public String getThumbnail() {
        return image;
    }

    public String getSize() {
        return size;
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
}
