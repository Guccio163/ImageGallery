package pl.edu.agh.to.thumbnails.server.processing.models.request;

import java.util.List;

public class ProcessImagesFoldered {
    private List<FolderedPair> pairs;

    public ProcessImagesFoldered() {
    }

    public ProcessImagesFoldered(List<FolderedPair> pairs) {
        this.pairs = pairs;
    }

    public List<FolderedPair> getPairs() {
        return pairs;
    }
}
