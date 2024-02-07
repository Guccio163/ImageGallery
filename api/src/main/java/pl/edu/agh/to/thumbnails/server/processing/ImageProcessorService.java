package pl.edu.agh.to.thumbnails.server.processing;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.thumbnails.server.folders.interfaqces.FolderRepository;
import pl.edu.agh.to.thumbnails.server.folders.models.Folder;
import pl.edu.agh.to.thumbnails.server.images.interfaces.ImageRepository;
import pl.edu.agh.to.thumbnails.server.images.models.Image;
import pl.edu.agh.to.thumbnails.server.processing.interfaces.IProcessorService;
import pl.edu.agh.to.thumbnails.server.processing.models.request.ByteFolderedPair;
import pl.edu.agh.to.thumbnails.server.processing.models.response.FolderedIdPair;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessFolderedImagesResponse;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessImageResponse;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessImagesResponse;
import pl.edu.agh.to.thumbnails.server.queue.interfaces.QueueItemRepository;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.thumbnails.interfaces.ThumbnailRepository;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.utils.IValidator;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageProcessorService implements IProcessorService {

    public final static List<Integer> THUMBNAIL_SIZES = List.of(ThumbnailSize.SMALL.getSize(),
            ThumbnailSize.MEDIUM.getSize(),
            ThumbnailSize.LARGE.getSize());

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ThumbnailRepository thumbnailRepository;
    @Autowired
    private QueueItemRepository queueItemRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private IValidator<Folder> folderValidator;


    @Transactional
    public Result<ProcessImageResponse> processImage(byte[] bytes)
    {
        var image = createImage(bytes);
        var response = new ProcessImageResponse(image.getId());
        return Result.success(response);
    }

    @Transactional
    public Result<ProcessImagesResponse> processImageList(List<byte[]> images)
    {
        var newImages = images.stream()
                .map(this::createImage)
                .map(Image::getId)
                .toList();

        var response = new ProcessImagesResponse(newImages);

        return Result.success(response);
    }

    @Override
    @Transactional
    public Result<List<FolderedIdPair>> processFolderedImageList(List<ByteFolderedPair> images) {

        var resultList = new ArrayList<FolderedIdPair>();

        for(var pair : images)
        {
            var newFolder = new Folder(pair.getPath());
            var validationResult = folderValidator.validate(newFolder);

            if (validationResult.failed())
                return Result.fail(validationResult.getError());

        }

        for(var pair : images)
        {
            var image = createImage(pair.getImage());

            if(pair.getPath().isEmpty())
            {
                resultList.add(new FolderedIdPair(-1L, image.getId()));
                continue;
            }

            var folder = folderRepository.findByPath(pair.getPath());

            if (folder == null)
            {
                var newFolder = new Folder(pair.getPath());
                var validationResult = folderValidator.validate(newFolder);

                if (validationResult.failed())
                    return Result.fail(validationResult.getError());

                folderRepository.save(newFolder);
                folder = newFolder;
            }

            image.setFolder(folder);
            imageRepository.save(image);
            resultList.add(new FolderedIdPair(folder.getId(), image.getId()));
        }

        return Result.success(resultList);
    }

    @Transactional
    public Image createImage(byte[] bytes)
    {
        var image = new Image(bytes);

        imageRepository.save(image);

        for(var size : THUMBNAIL_SIZES)
        {
            var thumbnail = new Thumbnail(image);
            thumbnailRepository.save(thumbnail);
            var queueItem = new QueueItem(thumbnail, size);
            queueItemRepository.save(queueItem);
        }

        return image;
    }
}
