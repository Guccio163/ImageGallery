package pl.edu.agh.to.thumbnails.server.images;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.thumbnails.server.images.interfaces.IImageService;
import pl.edu.agh.to.thumbnails.server.images.models.FullImageDTO;
import pl.edu.agh.to.thumbnails.server.images.models.Image;
import pl.edu.agh.to.thumbnails.server.images.models.ImageDTO;
import pl.edu.agh.to.thumbnails.server.images.models.response.ImageListDTO;
import pl.edu.agh.to.thumbnails.server.images.models.response.ImageWithThumbnails;
import pl.edu.agh.to.thumbnails.server.images.models.response.ThumbnailsListDTO;
import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.thumbnails.interfaces.IThumbnailService;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.ThumbnailDTO;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private IImageService imageService;

    @Autowired
    private IThumbnailService thumbnailService;

    public static final String PING_RESPONSE = "Images are available!";

    @GetMapping("/ping")
    public String pingImageController() {
        return PING_RESPONSE;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getImages() {

        List<Image> result = imageService.getImages();

        var imageDTO = result.stream()
                .map(ImageDTO::new)
                .toList();

        var payload = new ImageListDTO(imageDTO);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);
    }
    @GetMapping("/{imageId}/full")
    public ResponseEntity<?> getFullImage(@PathVariable(name = "imageId") Long imageId) {
        Result<Image> result = imageService.getFullImage(imageId);

        if (result.failed()) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.getError());
        }

        var payload = new FullImageDTO(result.getData());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);
    }
    @GetMapping("/{imageId}/thumbnails/full")
    public ResponseEntity<?> getImageWithFullThumbnails(@PathVariable(name = "imageId") Long imageId) {
        Result<Image> result = imageService.getImageWithFullThumbnails(imageId);

        if (result.getError() == ImageServiceErrors.ImageNotFound) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.getError());
        }

        if(result.failed())
        {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.getError());
        }

        var payload = new ImageWithThumbnails(result.getData());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);
    }
    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> removeImage(@PathVariable(name = "imageId") Long imageId) {
        var result = imageService.removeImage(imageId);

        if (!result.succeded())
            return ResponseEntity.badRequest().build();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all/thumbnails/size/{size}")
    public ResponseEntity<?> getAllThumbnails(@PathVariable(name = "size") String size) {

        var thumbnailsSize = ThumbnailSize.fromString(size);

        if (thumbnailsSize == ThumbnailSize.INVALID){
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ImageServiceErrors.InvalidThumbnailSize);
        }

        List<Thumbnail> result = thumbnailService.getAllThumbnails(thumbnailsSize);

        var payload = new ThumbnailsListDTO(result);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);
    }

    @GetMapping("/nofolder/thumbnails/size/{thumbnailSize}")
    public ResponseEntity<?> getNoFolderThumbnails(@PathVariable(name = "thumbnailSize") String thumbnailSize,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size) {

        var thumbnailsSize = ThumbnailSize.fromString(thumbnailSize);

        if (thumbnailsSize == ThumbnailSize.INVALID){
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ImageServiceErrors.InvalidThumbnailSize);
        }

        PageRequest pageRequest = PageRequest.of(page, size);

        var result = imageService.getNoFolderThumbnails(thumbnailsSize, pageRequest);

        if(result.failed())
        {
            return ResponseEntity.status(HttpStatusCode.valueOf(409))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.getError());
        }

        var payload = new ThumbnailsListDTO(result.getData());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);
    }

    @GetMapping("/{imageId}/thumbnails")
    public ResponseEntity<?> getThumbnails(@PathVariable(name = "imageId") Long imageId) {
        var result = thumbnailService.getThumbnailsByImageId(imageId);

        if (result.failed()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.getError());
        }

        var payload = new ThumbnailsListDTO(result.getData());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);
    }

    @GetMapping("/{imageId}/thumbnail/size/{size}")
    public ResponseEntity<?> getThumbnail(@PathVariable(name = "imageId") Long imageId, @PathVariable(name = "size") String size) {

        var thumbnailsSize = ThumbnailSize.fromString(size);

        if (thumbnailsSize == ThumbnailSize.INVALID){
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ImageServiceErrors.InvalidThumbnailSize);
        }

        Result<Thumbnail> result = thumbnailService.getThumbnail(imageId, thumbnailsSize);

        if (result.failed()) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.getError());
        }

        var payload = new ThumbnailDTO(result.getData());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);
    }


}
