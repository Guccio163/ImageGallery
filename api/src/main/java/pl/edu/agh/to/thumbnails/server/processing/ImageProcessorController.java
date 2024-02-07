package pl.edu.agh.to.thumbnails.server.processing;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.thumbnails.server.ErrorResponse;
import pl.edu.agh.to.thumbnails.server.processing.interfaces.IProcessorService;
import pl.edu.agh.to.thumbnails.server.processing.models.request.*;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessImageResponse;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessImagesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.to.thumbnails.server.utils.RestUtils;

@RestController
@RequestMapping("/process")
public class ImageProcessorController {

    public final static int MAX_ZIP_IMAGES = 10;
    public static final String PROCESSING_PING_MESSAGE = "Processing is available!";
    private static final Logger log = LoggerFactory.getLogger(ImageProcessorController.class);

    @Autowired
    private IProcessorService processorService;

    @GetMapping("/ping")
    public String pingProcessorController() {
        return PROCESSING_PING_MESSAGE;
    }

    @PostMapping("/image")
    public ResponseEntity<?> processImage(@RequestBody ProcessImageRequest request) {
        try {
            var decodedBytes = RestUtils.decode(request.getImage());

            var processSetup = processorService.processImage(decodedBytes);

            if (!processSetup.succeded())
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(processSetup.getError());

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(processSetup.getData());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorResponse(e, "Invalid base64 string."));
        }
    }

    @PostMapping("/images")
    public ResponseEntity<?> processImageList(@RequestBody ProcessImagesRequest request) {
        try {
            var images = request.getImages();

            if (images.isEmpty() || images.size() > MAX_ZIP_IMAGES)
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ErrorResponse("TooMuchImages", "To much images uploaded at once, max is"+MAX_ZIP_IMAGES));

            var imageList = Observable.fromIterable(images)
                    .flatMap(image -> Observable.just(image)
                            .subscribeOn(Schedulers.io())
                            .map(RestUtils::decode)
                    )
                    .toList()
                    .blockingGet();

            var processSetup = processorService.processImageList(imageList);

            if (!processSetup.succeded())
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(processSetup.getError());

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(processSetup.getData());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorResponse(e, "Invalid base64 string."));
        }
    }

    @PostMapping("/images/foldering")
    public ResponseEntity<?> processFoldeedImageList(@RequestBody ProcessImagesFoldered request) {
        try {
            var images = request.getPairs();

            if (images.isEmpty() || images.size() > MAX_ZIP_IMAGES)
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ErrorResponse("TooMuchImages", "To much images uploaded at once, max is"+MAX_ZIP_IMAGES));

            var imageList = Observable.fromIterable(images)
                    .flatMap(image -> Observable.just(image)
                            .subscribeOn(Schedulers.io())
                            .map(x -> new ByteFolderedPair(x.getPath(), RestUtils.decode(x.getImage())))
                    )
                    .toList()
                    .blockingGet();

            var processSetup = processorService.processFolderedImageList(imageList);

            if (!processSetup.succeded())
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON).body(processSetup.getError());

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(processSetup.getData());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorResponse(e, "Invalid base64 string."));
        }
    }
}
