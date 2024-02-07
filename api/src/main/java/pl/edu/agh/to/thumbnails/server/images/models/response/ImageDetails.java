package pl.edu.agh.to.thumbnails.server.images.models.response;

import pl.edu.agh.to.thumbnails.server.images.models.Image;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ImageDetails {
    private Long id;
    private List<ThumbnailDetails> thumbnails = new ArrayList<>();
    private LocalDateTime createdAt;

    public ImageDetails(){}

    public ImageDetails(Image image) {
        this.id = image.getId();
        this.createdAt = image.getCreatedAt();
        this.thumbnails.addAll(image.getThumbnails().stream().map(ThumbnailDetails::new).toList());
    }

    public Long getId() {
        return id;
    }

    public List<ThumbnailDetails> getThumbnails() {
        return thumbnails;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
