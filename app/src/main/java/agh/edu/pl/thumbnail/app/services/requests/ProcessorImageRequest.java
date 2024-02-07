package agh.edu.pl.thumbnail.app.services.requests;

import agh.edu.pl.thumbnail.app.utils.RestUtils;

public class ProcessorImageRequest {

    private String image;

    public ProcessorImageRequest(String imageBase64) {
        this.image = imageBase64;
    }

    public ProcessorImageRequest(byte[] image) {
        this.image = RestUtils.encode(image);
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String imageBase64) {
        this.image = imageBase64;
    }
}
