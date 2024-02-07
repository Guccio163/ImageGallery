package pl.edu.agh.to.thumbnails.server.thumbnails;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.thumbnails.interfaces.IThumbnailService;
import pl.edu.agh.to.thumbnails.server.thumbnails.interfaces.ThumbnailRepository;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.utils.IValidator;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.List;

@Service
public class ThumbnailService implements IThumbnailService {

    @Autowired
    private ThumbnailRepository thumbnailRepository;

    @Autowired
    private IValidator<List<Thumbnail>> imageThumbnailsValidator;

    @Override
    @Transactional
    public List<Thumbnail> getAllThumbnails(ThumbnailSize size) {

        var thumbnails = thumbnailRepository.getAllByHeight(size.getSize())
                .stream()
                .toList();

        return thumbnails;
    }

    @Transactional
    public Result<List<Thumbnail>> getThumbnailsByImageId(Long imageId) {
        var imageList = thumbnailRepository.findByImageId(imageId);

        var validation = imageThumbnailsValidator.validate(imageList);

        if(validation.failed())
            return Result.fail(validation.getError());

        return Result.success(imageList);
    }

    @Override
    @Transactional
    public Result<Thumbnail> getThumbnail(Long imageId, ThumbnailSize size)
    {
        try {
            var thumbnail = thumbnailRepository.getByImageIdAndHeight(imageId, size.getSize());
            return Result.success(thumbnail);
        } catch (EntityNotFoundException exception) {
            return Result.fail(ThumbnailServiceErrors.ThumbnailNotFound);
        }
    }
}
