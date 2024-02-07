package pl.edu.agh.to.thumbnails.server.folders.models;

import java.time.LocalDateTime;

public class FolderDTO {

    private int imagesCount;
    private Long id;

    private String path;

    private LocalDateTime createdAt;

    public FolderDTO() {
    }

    public FolderDTO(Folder folder) {
        this.id = folder.getId();
        this.path = folder.getPath();
        this.imagesCount = folder.getImages().size();
        this.createdAt = folder.getCreatedAt();
    }

    public int getImagesCount() {
        return imagesCount;
    }
    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public final String toString() {
        return id.toString();
    }
}
