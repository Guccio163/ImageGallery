package pl.edu.agh.to.thumbnails.server.folders;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.thumbnails.server.FolderValidator;
import pl.edu.agh.to.thumbnails.server.folders.models.FolderDTO;
import pl.edu.agh.to.thumbnails.server.folders.requests.AddFolderRequest;
import pl.edu.agh.to.thumbnails.server.folders.requests.AddImagesToFolderRequest;
import pl.edu.agh.to.thumbnails.server.folders.responses.GetFoldersResponses;
import pl.edu.agh.to.thumbnails.server.images.ImageServiceErrors;
import pl.edu.agh.to.thumbnails.server.images.models.response.ThumbnailsListDTO;
import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/folders")
public class FoldersController {
    @Autowired
    private FoldersService folderService;

    public static final String PING_RESPONSE = "Folders are available!";

    @GetMapping("/ping")
    public String pingFolderController() {
        return PING_RESPONSE;
    }

    @GetMapping
    @Transactional
    public ResponseEntity<GetFoldersResponses> getFolders() {
        var folders = folderService.getAll();

        var dto = folders.stream()
                .map(FolderDTO::new)
                .collect(Collectors.toList());

        var response = new GetFoldersResponses(dto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFolder(@RequestBody AddFolderRequest request) {

        Result result = folderService.addFolder(request.getPath());

        if (result.failed()){

            if(result.getError() == FolderErrors.FolderExists)
                return ResponseEntity.status(HttpStatusCode.valueOf(409))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.getError());

            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.getError());
        }

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long id)
    {
        Result result = folderService.removeFolder(id);

        if (result.failed()){
            return ResponseEntity.status(HttpStatusCode.valueOf(409))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.getError());
        }

        return ResponseEntity
                .noContent()
                .build();

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> addImages(@RequestBody AddImagesToFolderRequest imagesIds, @PathVariable Long id) {

        if(imagesIds.getIds().size() == 0)
        {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(FolderErrors.ValidationFailed);
        }

        Result result = folderService.addImages(id, imagesIds.getIds());

        if(result.failed())
        {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.getError());
        }

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/{id}/thumbnails/{thumbnailSize}")
    @Transactional
    public ResponseEntity<?> getThumbnails(@PathVariable Long id,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @PathVariable String thumbnailSize) {

        var thumbnailsSize = ThumbnailSize.fromString(thumbnailSize);

        if (thumbnailsSize == ThumbnailSize.INVALID){
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ImageServiceErrors.InvalidThumbnailSize);
        }


        PageRequest pageRequest = PageRequest.of(page, size);

        var result = folderService.getFolderThumbnails(id, thumbnailsSize, pageRequest);

        if (result.failed())
        {
           return ResponseEntity.status(HttpStatusCode.valueOf(409))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.getError());
        }

        var payload = new ThumbnailsListDTO(result.getData());

        return ResponseEntity.ok(payload);
    }
}
