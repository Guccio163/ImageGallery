package agh.edu.pl.thumbnail.app.dtos;

public class ImageWithThumbnailsDTO {

    private ThumbnailDTO small;
    private ThumbnailDTO medium;
    private ThumbnailDTO large;
    private ImageDetailsDTO image;

    public ImageWithThumbnailsDTO(){}

    public ThumbnailDTO getSmall() {
        return small;
    }

    public ThumbnailDTO getMedium() {
        return medium;
    }

    public ThumbnailDTO getLarge() {
        return large;
    }

    public ImageDetailsDTO getImage() {
        return image;
    }
}
