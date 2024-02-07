package pl.edu.agh.to.thumbnails.server.processing.models.response;


import java.util.ArrayList;
import java.util.List;

public class ProcessImagesResponse {
    private final List<Long> processedImages;

    public ProcessImagesResponse(){
        processedImages = new ArrayList<>();
    }
    public ProcessImagesResponse(List<Long> processedImages) {
        this.processedImages = processedImages;
    }

    public List<Long> getProcessedImages() {
        return processedImages;
    }
}
