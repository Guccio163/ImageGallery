package pl.edu.agh.to.thumbnails.server.thumbnails.enums;

import java.util.Map;

public enum ThumbnailSize {
    INVALID(-1),
    SMALL(32),
    MEDIUM(64),
    LARGE(128);

    private static final Map<String, ThumbnailSize> NAME_MAP =  Map.of(
            "small", SMALL,
            "medium", MEDIUM,
            "large", LARGE
    );

    private final Integer size;

    ThumbnailSize(Integer size) {
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

    public static ThumbnailSize fromInteger(int size) {
        for (ThumbnailSize thumbnailSize : ThumbnailSize.values()) {
            if (thumbnailSize.getSize() == size) {
                return thumbnailSize;
            }
        }
        return INVALID;
    }

    public static ThumbnailSize fromString(String name) {
        return NAME_MAP.getOrDefault(name.toLowerCase(), INVALID);
    }

}
