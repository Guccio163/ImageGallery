package agh.edu.pl.thumbnail.app.dtos;

public class ProcessedImageDTO {
    private Long imageId;

    public ProcessedImageDTO() {
        this.imageId = -1L;
    }
    public ProcessedImageDTO(Long imageId) {
        this.imageId = imageId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}

