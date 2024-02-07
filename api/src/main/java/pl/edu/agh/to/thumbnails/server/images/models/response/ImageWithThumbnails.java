package pl.edu.agh.to.thumbnails.server.images.models.response;

import pl.edu.agh.to.thumbnails.server.images.models.Image;
import pl.edu.agh.to.thumbnails.server.images.models.ImageDTO;
import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.ThumbnailDTO;

public class ImageWithThumbnails {

    private ThumbnailDTO small;
    private ThumbnailDTO medium;
    private ThumbnailDTO large;
    private ImageDTO image;
    public ImageWithThumbnails(){}
    public ImageWithThumbnails(Image image){
        this.image = new ImageDTO(image);
        var thumbnails = image.getThumbnails();
        for (Thumbnail thumbnail : thumbnails) {
            var dto = new ThumbnailDTO(thumbnail);

            if(thumbnail.getSize() == ThumbnailSize.SMALL)
                small = dto;
            else if(thumbnail.getSize() == ThumbnailSize.MEDIUM)
                medium = dto;
            else
                large = dto;
        }
    }

    public ThumbnailDTO getSmall() {
        return small;
    }

    public ThumbnailDTO getMedium() {
        return medium;
    }

    public ThumbnailDTO getLarge() {
        return large;
    }

    public ImageDTO getImage() {
        return image;
    }
}
