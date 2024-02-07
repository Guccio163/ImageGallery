package agh.edu.pl.thumbnail.app.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Image {
    private Long id;

    private byte[] image;

    private Set<Thumbnail> thumbnails = new HashSet<>();

    private final LocalDateTime createdAt;

    public Image() {
        this.createdAt = LocalDateTime.now();
    }

    public Image(byte[] image) {
        this();
        this.image = image;
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

    public Set<Thumbnail> getThumbnails() {
        return this.thumbnails;
    }

    public void addThumbnail(Thumbnail thumbnail) {
        this.thumbnails.add(thumbnail);
    }

    @Override
    public final String toString() {
        return id.toString();
    }
}
