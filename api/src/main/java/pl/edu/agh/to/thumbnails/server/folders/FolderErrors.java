package pl.edu.agh.to.thumbnails.server.folders;

import pl.edu.agh.to.thumbnails.server.utils.Error;

public class FolderErrors {
    public static Error ValidationFailed
            = new Error("Folder.Add.Images","Validation failed, check path.");

    public static Error FolderNotFound
            = new Error("Folder.NotFound","Folder was not found.");

    public static Error ImageNotFound = new Error("Folder.Image.NotFound", "Some images was not found.");

    public static Error FolderExists
            = new Error("Folder.Exists","Folder with this name exists.");

    public static Error InvalidThumbnailOnPage(int pageSize) {
        return new Error("Folde.Image.Thumbnail.TooMuch", "Invalid number of thumbnails on page "+pageSize);
    }
}
