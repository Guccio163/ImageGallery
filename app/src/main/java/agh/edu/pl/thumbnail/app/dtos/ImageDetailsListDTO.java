package agh.edu.pl.thumbnail.app.dtos;

import java.util.ArrayList;
import java.util.List;

public class ImageDetailsListDTO {
    private List<ImageDetailsDTO> images;

    public ImageDetailsListDTO() {
        images = new ArrayList<>();
    }

    public ImageDetailsListDTO(List<ImageDetailsDTO> images) {
        this.images = images;
    }

    public void setImages(List<ImageDetailsDTO> images) {
        this.images = images;
    }

    public List<ImageDetailsDTO> getImages() {
        return images;
    }
}
