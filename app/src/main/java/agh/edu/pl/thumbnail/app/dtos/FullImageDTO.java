package agh.edu.pl.thumbnail.app.dtos;

import java.time.LocalDateTime;


public class FullImageDTO {

    private Long id;

    private String image;
    private LocalDateTime createdAt;

    public FullImageDTO(){}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
