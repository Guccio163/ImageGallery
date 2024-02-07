package pl.edu.agh.to.thumbnails.server.folders.requests;

import java.util.List;

public class AddImagesToFolderRequest {
    private List<Long> ids;


    public AddImagesToFolderRequest() {

    }

    public AddImagesToFolderRequest(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }
}
