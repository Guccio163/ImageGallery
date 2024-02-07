package pl.edu.agh.to.thumbnails.server.thumbnails;

import org.springframework.stereotype.Component;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.utils.IValidator;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImageThumbnailsValidator implements IValidator<List<Thumbnail>> {

    @Override
    public Result<List<Thumbnail>> validate(List<Thumbnail> items) {

        if(items.size() != 3)
        {
            return Result.fail(ThumbnailServiceErrors.LackingThumbnails);
        }

        var sizes = items.stream()
                .map(Thumbnail::getSize)
                .collect(Collectors.toSet());

        if(sizes.size() != 3)
        {
            return Result.fail(ThumbnailServiceErrors.InvalidThumbnailsFound);
        }

        return Result.success(items);
    }
}
