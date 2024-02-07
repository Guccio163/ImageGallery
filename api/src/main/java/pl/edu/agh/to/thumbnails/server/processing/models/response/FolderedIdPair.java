package pl.edu.agh.to.thumbnails.server.processing.models.response;

public class FolderedIdPair {
    private Long folderId;
    private Long ImageId;

    public FolderedIdPair(Long folderId, Long imageId) {
        this.folderId = folderId;
        ImageId = imageId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getImageId() {
        return ImageId;
    }

    public void setImageId(Long imageId) {
        ImageId = imageId;
    }
}

