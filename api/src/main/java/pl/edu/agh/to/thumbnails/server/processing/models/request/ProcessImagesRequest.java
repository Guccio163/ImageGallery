package pl.edu.agh.to.thumbnails.server.processing.models.request;

import java.util.List;

public class ProcessImagesRequest {

    private List<String> images;

    public ProcessImagesRequest() {
    }

    public ProcessImagesRequest(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }
}
