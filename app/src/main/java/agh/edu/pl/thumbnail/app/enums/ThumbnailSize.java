package agh.edu.pl.thumbnail.app.enums;

public enum ThumbnailSize {
    INVALID(-1),
    SMALL(32),
    MEDIUM(64),
    LARGE(128);

    private static final String SMALL_NAME = "small";
    private static final String MEDIUM_NAME = "medium";
    private static final String LARGE_NAME = "large";
    private static final String INVALID_NAME = "invalid";

    public String getName(){
        return switch(this){
            case SMALL -> SMALL_NAME;
            case MEDIUM -> MEDIUM_NAME;
            case LARGE -> LARGE_NAME;
            default -> INVALID_NAME;
        };
    }


    private final Integer size;

    ThumbnailSize(Integer size) {
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

    public static ThumbnailSize fromInteger(Integer height) {
        if (SMALL.getSize().equals(height))
            return SMALL;
        if (MEDIUM.getSize().equals(height))
            return MEDIUM;
        if (LARGE.getSize().equals(height))
            return LARGE;
        return INVALID;
    }

    public static ThumbnailSize fromString(String name) {
        if (name.equals(SMALL_NAME))
            return SMALL;
        if (name.equals(MEDIUM_NAME))
            return MEDIUM;
        if (name.equals(LARGE_NAME))
            return LARGE;
        return INVALID;
    }
}
