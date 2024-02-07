package pl.edu.agh.to.thumbnails.server.images.models.requests;

import java.util.List;

public class ImagesDefinitionRequest {

    private List<Long> ids;

    public ImagesDefinitionRequest() {
    }

    public ImagesDefinitionRequest(List<Long> images) {
        this.ids = images;
    }

    public void setIds(List<Long> ids){
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }
}
