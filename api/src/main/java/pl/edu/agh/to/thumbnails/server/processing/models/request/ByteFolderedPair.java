package pl.edu.agh.to.thumbnails.server.processing.models.request;

public class ByteFolderedPair {
    private String path;
    private byte[] image;

    public ByteFolderedPair(String path, byte[] image) {
        this.path = path;
        this.image = image;
    }

    public ByteFolderedPair() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
