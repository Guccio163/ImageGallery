package pl.edu.agh.to.thumbnails.server.folders.requests;

public class AddFolderRequest {
    private String path;

    public AddFolderRequest(String path) {
        this.path = path;
    }

    public AddFolderRequest() {

    }

    public String getPath() {
        return path;
    }
}
