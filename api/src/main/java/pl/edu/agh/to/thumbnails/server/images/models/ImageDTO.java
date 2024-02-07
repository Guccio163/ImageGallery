package pl.edu.agh.to.thumbnails.server.images.models;

import java.time.LocalDateTime;


public class ImageDTO {

    private Long id;
    private LocalDateTime createdAt;

    public ImageDTO(){

    }

    public ImageDTO(Image image) {
        this.id = image.getId();
        this.createdAt = image.getCreatedAt();
    }

    public Long getId() {
        return id;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
