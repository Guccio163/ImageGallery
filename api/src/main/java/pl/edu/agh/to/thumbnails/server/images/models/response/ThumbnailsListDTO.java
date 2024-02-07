package pl.edu.agh.to.thumbnails.server.images.models.response;

import pl.edu.agh.to.thumbnails.server.images.models.Image;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.ThumbnailDTO;

import java.util.ArrayList;
import java.util.List;

public class ThumbnailsListDTO {
    private final List<ThumbnailDTO> thumbnails = new ArrayList<>();

    public ThumbnailsListDTO(){}

    public ThumbnailsListDTO(List<Thumbnail> thumbnails)
    {
        for (Thumbnail thumbnail : thumbnails) {
            this.thumbnails.add( new ThumbnailDTO(thumbnail));
        }
    }

    public List<ThumbnailDTO> getThumbnails() {
        return thumbnails;
    }

}
