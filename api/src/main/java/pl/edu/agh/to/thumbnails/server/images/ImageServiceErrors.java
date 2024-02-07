package pl.edu.agh.to.thumbnails.server.images;

import pl.edu.agh.to.thumbnails.server.utils.Error;

public class ImageServiceErrors {

    public static Error ImageNotFound
            = new Error("Image.Get.NotFound","Image was not found.");

    public static Error InvalidThumbnailSize
            = new Error("Thumbnails.Get.Size","Provided invalid thumbnail size.");
}
