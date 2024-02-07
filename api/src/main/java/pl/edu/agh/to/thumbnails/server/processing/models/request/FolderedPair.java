package pl.edu.agh.to.thumbnails.server.processing.models.request;

public class FolderedPair {
    private String path;
    private String image;

    public FolderedPair(String path, String image) {
        this.path = path;
        this.image = image;
    }

    public FolderedPair() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
