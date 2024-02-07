package agh.edu.pl.thumbnail.app.dtos;

import java.time.LocalDateTime;


public class ImageDetailsDTO {

    private Long id;
    private LocalDateTime createdAt;

    public ImageDetailsDTO(){

    }

    public ImageDetailsDTO(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
