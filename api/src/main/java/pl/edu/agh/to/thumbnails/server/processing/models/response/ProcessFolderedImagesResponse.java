package pl.edu.agh.to.thumbnails.server.processing.models.response;


import java.util.ArrayList;
import java.util.List;

public class ProcessFolderedImagesResponse {
    private final List<FolderedIdPair> folderedIdPairs;

    public ProcessFolderedImagesResponse(){
        folderedIdPairs = new ArrayList<>();
    }
    public ProcessFolderedImagesResponse(List<FolderedIdPair> processedImages) {
        this.folderedIdPairs = processedImages;
    }

    public List<FolderedIdPair> getProcessedImages() {
        return folderedIdPairs;
    }
}
