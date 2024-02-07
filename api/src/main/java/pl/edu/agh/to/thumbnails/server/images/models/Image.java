package pl.edu.agh.to.thumbnails.server.images.models;

import jakarta.persistence.*;
import pl.edu.agh.to.thumbnails.server.folders.models.Folder;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL)
    private List<Thumbnail> thumbnails = new ArrayList<>();

    private final LocalDateTime createdAt;

    public Image() {
        this.createdAt = LocalDateTime.now();
    }

    public Image(byte[] image) {
        this();
        this.image = image;
    }

    public Image(byte[] image, Folder folder) {
        this();
        this.image = image;
        this.folder = folder;
    }

    public Long getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Thumbnail> getThumbnails() {
        return this.thumbnails;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public void addThumbnail(Thumbnail thumbnail) {
        this.thumbnails.add(thumbnail);
    }

    @Override
    public final String toString() {
        return id.toString();
    }
}
