package pl.edu.agh.to.thumbnails.server.images.interfaces;

import org.springframework.data.domain.PageRequest;
import pl.edu.agh.to.thumbnails.server.images.models.Image;
import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.List;

public interface IImageService {
    List<Image> getImages();
    Result<List<Image>> getImages(List<Long> ids);
    Result<Image> getFullImage(Long imageId);
    Result<Image> getImageWithFullThumbnails(Long imageId);
    Result removeImage(Long imageId);
    Result<List<Thumbnail>> getNoFolderThumbnails(ThumbnailSize thumbnailsSize, PageRequest pageRequest);
}
