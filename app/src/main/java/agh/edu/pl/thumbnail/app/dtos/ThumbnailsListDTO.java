package agh.edu.pl.thumbnail.app.dtos;

import agh.edu.pl.thumbnail.app.models.Thumbnail;

import java.util.ArrayList;
import java.util.List;

public class ThumbnailsListDTO {
    private List<ThumbnailDTO> thumbnails = new ArrayList<>();

    public ThumbnailsListDTO(){}

    public List<ThumbnailDTO> getThumbnails() {
        return thumbnails;
    }
    public void setThumbnails(List<ThumbnailDTO> thumbnails) {
        this.thumbnails = thumbnails;
    }
}

