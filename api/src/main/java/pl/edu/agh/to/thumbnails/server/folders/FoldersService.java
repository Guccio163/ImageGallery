package pl.edu.agh.to.thumbnails.server.folders;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.thumbnails.server.folders.interfaqces.FolderRepository;
import pl.edu.agh.to.thumbnails.server.folders.models.Folder;
import pl.edu.agh.to.thumbnails.server.images.interfaces.ImageRepository;
import pl.edu.agh.to.thumbnails.server.images.models.Image;
import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.utils.IValidator;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoldersService {

    @Autowired
    private FolderRepository repository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private IValidator<Folder> folderValidator;

    public List<Folder> getAll() {

        var items = repository.findAll();

        return items;
    }

    @Transactional
    public Result addFolder(String path) {
        var exists = repository.findByPath(path);

        if(exists != null)
            return Result.fail(FolderErrors.FolderExists);

        var folder = new Folder(path);

        var validationResult = folderValidator.validate(folder);

        if (validationResult.failed())
            return validationResult;

        repository.save(folder);

        return Result.success();
    }

    @Transactional
    public Result addImages(Long id, List<Long> ids) {
        var folderTest = repository.findById(id);

        if(folderTest.isEmpty())
            return Result.fail(FolderErrors.FolderNotFound);

        var idsSet = new HashSet<>(ids);
        var images = imageRepository.findAllByIdIn(new ArrayList<>(idsSet));

        if(images.size() != ids.size()){
            return Result.fail(FolderErrors.ImageNotFound);
        }

        var folder = folderTest.get();
        var currentImages = folder.getImages().stream().map(Image::getId).collect(Collectors.toSet());

        if(idsSet.containsAll(currentImages) && !currentImages.isEmpty())
            return Result.fail(FolderErrors.ValidationFailed);

        for(var image : images)
        {
            var inFolder = currentImages.stream()
                    .filter(x -> Objects.equals(x, image.getId()))
                    .toList();

            if(inFolder.isEmpty())
            {
                image.setFolder(folder);
                imageRepository.save(image);
            }
        }

        repository.save(folder);

        return Result.success();
    }

    @Transactional
    public Result removeFolder(Long id) {

        var folder = repository.findById(id);

        if(folder.isEmpty())
            return Result.fail(FolderErrors.FolderNotFound);

        repository.delete(folder.get());

        return Result.success();
    }

    @Transactional
    public Result<List<Thumbnail>> getFolderThumbnails(Long id, ThumbnailSize size, PageRequest pageRequest) {

        var folderTest = repository.findById(id);

        if(folderTest.isEmpty())
            return Result.fail(FolderErrors.FolderNotFound);

        var page = imageRepository.findByFolderId(id, pageRequest);

        var images = page.get();

        var thumbnails = images
                .flatMap(x -> x.getThumbnails().stream())
                .filter(x -> x.getSize() == size)
                .toList();

        if (thumbnails.size() == pageRequest.getPageSize())
        {
            return Result.success(thumbnails);
        }

        if (thumbnails.size() != 0)
        {
            return Result.success(thumbnails);
        }

        return Result.fail(FolderErrors.InvalidThumbnailOnPage(pageRequest.getPageSize()));
    }
}
