package pl.edu.agh.to.thumbnails.server.processing.interfaces;

import org.springframework.web.bind.annotation.RequestBody;
import pl.edu.agh.to.thumbnails.server.processing.models.request.ByteFolderedPair;
import pl.edu.agh.to.thumbnails.server.processing.models.response.FolderedIdPair;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessFolderedImagesResponse;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessImageResponse;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessImagesResponse;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.List;

public interface IProcessorService {
    Result<ProcessImageResponse> processImage(@RequestBody byte[] bytes);
    Result<ProcessImagesResponse> processImageList(@RequestBody List<byte[]> images);
    Result<List<FolderedIdPair>> processFolderedImageList(@RequestBody List<ByteFolderedPair> images);
}
