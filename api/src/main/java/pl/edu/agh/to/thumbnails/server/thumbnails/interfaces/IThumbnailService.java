package pl.edu.agh.to.thumbnails.server.thumbnails.interfaces;

import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.List;

public interface IThumbnailService {
    List<Thumbnail> getAllThumbnails(ThumbnailSize size);
    Result<List<Thumbnail>> getThumbnailsByImageId(Long imageId);
    Result<Thumbnail> getThumbnail(Long imageId, ThumbnailSize size);
}
