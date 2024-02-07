package pl.edu.agh.to.thumbnails.server.processing.models.response;

public class ProcessImageResponse {
    private Long imageId;

    public ProcessImageResponse() {
        this.imageId = -1L;
    }
    public ProcessImageResponse(Long imageId) {
        this.imageId = imageId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
