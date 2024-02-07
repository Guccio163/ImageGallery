package agh.edu.pl.thumbnail.app.services.interfaces;

import agh.edu.pl.thumbnail.app.dtos.ProcessedImageDTO;
import agh.edu.pl.thumbnail.app.dtos.ProcessedZipDTO;
import agh.edu.pl.thumbnail.app.utils.Result;

public interface IProcessorService {

    Result<ProcessedImageDTO> addImageForProcessing(String pathToImage);
    Result<ProcessedZipDTO> addImagesForProcessing(String pathToFolder);
}
