package pl.edu.agh.to.thumbnails.server.images.models.response;

import pl.edu.agh.to.thumbnails.server.images.models.ImageDTO;

import java.util.ArrayList;
import java.util.List;

public class ImageListDTO {
    private List<ImageDTO> images;

    public ImageListDTO() {
        images = new ArrayList<>();
    }
    public ImageListDTO(List<ImageDTO> images){
        this.images = images;
    }

    public List<ImageDTO> getImages() {
        return images;
    }
}
