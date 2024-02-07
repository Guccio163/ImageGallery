package agh.edu.pl.thumbnail.app.dtos;

import java.time.LocalDateTime;

public class FolderDTO {
    private Long id = null;

    private String path;
    private LocalDateTime createdAt;

    public FolderDTO() {
        this.createdAt = LocalDateTime.now();
    }

    public FolderDTO(String path) {
        this();
        this.path = path;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public final String toString() {
        return id.toString();
    }
}
