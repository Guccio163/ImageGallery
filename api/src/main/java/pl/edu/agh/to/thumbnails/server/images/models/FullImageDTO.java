package pl.edu.agh.to.thumbnails.server.images.models;

import pl.edu.agh.to.thumbnails.server.utils.RestUtils;

import java.time.LocalDateTime;


public class FullImageDTO {

    private Long id;

    private String image;
    private LocalDateTime createdAt;

    public FullImageDTO(){

    }

    public FullImageDTO(Image image) {
        this.image = RestUtils.encode(image.getImage());
        this.id = image.getId();
        this.createdAt = image.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
