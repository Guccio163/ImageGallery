package pl.edu.agh.to.thumbnails.server.images;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.thumbnails.server.folders.FolderErrors;
import pl.edu.agh.to.thumbnails.server.images.interfaces.IImageService;
import pl.edu.agh.to.thumbnails.server.images.interfaces.ImageRepository;
import pl.edu.agh.to.thumbnails.server.images.models.Image;
import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.thumbnails.interfaces.ThumbnailRepository;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.utils.IValidator;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService implements IImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private IValidator<List<Thumbnail>> imageThumbnailsValidator;

    @Override
    public List<Image> getImages() {
        List<Image> result = new ArrayList<>(imageRepository
                .findAll());

        return result;
    }

    @Override
    @Transactional
    public Result<Image> getFullImage(Long imageId) {
        var image = imageRepository.findById(imageId);

        return image.map(Result::success)
                .orElseGet(() -> Result.fail(ImageServiceErrors.ImageNotFound));

    }

    @Override
    @Transactional
    public Result<List<Image>> getImages(List<Long> ids) {
        List<Image> result = imageRepository
                .findAll()
                .stream()
                .filter(x -> ids.contains(x.getId()))
                .collect(Collectors.toList());

        return Result.success(result);
    }

    @Override
    @Transactional
    public Result<Image> getImageWithFullThumbnails(Long imageId) {

        var image = imageRepository.findById(imageId);

        if (image.isEmpty())
            return Result.fail(ImageServiceErrors.ImageNotFound);

        var validation = imageThumbnailsValidator.validate(image.get().getThumbnails());

        if (validation.failed())
            return Result.fail(validation.getError());

        return Result.success(image.get());

    }

    @Override
    @Transactional
    public Result removeImage(Long imageId) {
        imageRepository.deleteById(imageId);
        return Result.success();
    }

    @Override
    @Transactional
    public Result<List<Thumbnail>> getNoFolderThumbnails(ThumbnailSize thumbnailsSize, PageRequest pageRequest) {

        var page = imageRepository.findNoFolderImages(pageRequest);

        var images = page.get();

        var thumbnails = images
                .flatMap(x -> x.getThumbnails().stream())
                .filter(x -> x.getSize() == thumbnailsSize)
                .toList();

        if (thumbnails.size() == pageRequest.getPageSize())
            return Result.success(thumbnails);

        if (thumbnails.size() != 0)
            return Result.success(thumbnails);

        return Result.fail(FolderErrors.InvalidThumbnailOnPage(pageRequest.getPageSize()));
    }
}
