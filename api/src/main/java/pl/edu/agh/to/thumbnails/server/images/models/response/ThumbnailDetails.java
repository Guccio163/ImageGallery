package pl.edu.agh.to.thumbnails.server.images.models.response;

import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;

public class ThumbnailDetails {

    private Long id;
    private ThumbnailSize size;

    public ThumbnailDetails(){}

    public ThumbnailDetails(Thumbnail thumbnail) {
        this.id = thumbnail.getId();
        this.size = thumbnail.getSize();
    }

    public Long getId() {
        return id;
    }

    public ThumbnailSize getSize() {
        return size;
    }
}
