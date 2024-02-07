package pl.edu.agh.to.thumbnails.server.folders.models;

import jakarta.persistence.*;
import pl.edu.agh.to.thumbnails.server.images.models.Image;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();
    private String path;
    private final LocalDateTime createdAt;

    public Folder() {
        this.createdAt = LocalDateTime.now();
    }

    public Folder(String path) {
        this();
        this.path = path;
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

    public List<Image> getImages() {
        return this.images;
    }

    public void addImage(Image image) {
        this.images.add(image);
    }

    @Override
    public final String toString() {
        return id.toString();
    }
}
