package pl.edu.agh.to.thumbnails.server;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.thumbnails.server.folders.FolderErrors;
import pl.edu.agh.to.thumbnails.server.folders.models.Folder;
import pl.edu.agh.to.thumbnails.server.utils.IValidator;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.regex.Pattern;

@Component
public class FolderValidator implements IValidator<Folder> {
    @Override
    public Result<Folder> validate(Folder item) {

        var path = item.getPath();

        Pattern validPathPattern = Pattern.compile("^[a-zA-Z0-9_\\-/.]+$" );

        if(path.length() >= 100)
        {
            return Result.fail(FolderErrors.ValidationFailed);
        }

        // Check if the string contains spaces
        if (path.contains(" ")) {
            return Result.fail(FolderErrors.ValidationFailed);
        }

        var result = validPathPattern.matcher(path).matches();

        if(!result)
            return Result.fail(FolderErrors.ValidationFailed);

        return Result.success(item);
    }
}
