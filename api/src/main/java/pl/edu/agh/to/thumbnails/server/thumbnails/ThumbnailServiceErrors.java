package pl.edu.agh.to.thumbnails.server.thumbnails;

import pl.edu.agh.to.thumbnails.server.utils.Error;

public class ThumbnailServiceErrors {
    public static Error LackingThumbnails
            = new Error("Thumbnails.Get.NotFound","Failed to find 3 thumbnails for image.");
    public static Error InvalidThumbnailsFound
            = new Error("Thumbnails.Get.InvalidSizes","Failed to find 3 different thumbnails for image.");

    public static Error ThumbnailNotFound
            = new Error("Thumbnail.Get.NotFound","Failed to find thumbnail by size and image id.");

    public static Error ThumbnailsNotFound
            = new Error("Thumbnails.Get.NotFound","Failed to find thumbnails.");
}
