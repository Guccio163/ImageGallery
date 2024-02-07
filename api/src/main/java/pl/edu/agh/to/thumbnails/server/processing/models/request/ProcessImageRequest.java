package pl.edu.agh.to.thumbnails.server.processing.models.request;

public class ProcessImageRequest {
    private String image;

    public ProcessImageRequest() {
    }

    public ProcessImageRequest(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
