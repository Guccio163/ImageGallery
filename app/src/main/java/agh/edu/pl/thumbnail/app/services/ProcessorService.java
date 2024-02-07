package agh.edu.pl.thumbnail.app.services;

import agh.edu.pl.thumbnail.app.dtos.ProcessedImageDTO;
import agh.edu.pl.thumbnail.app.dtos.ProcessedZipDTO;
import agh.edu.pl.thumbnail.app.services.interfaces.IProcessorService;
import agh.edu.pl.thumbnail.app.services.requests.ProcessorImageRequest;
import agh.edu.pl.thumbnail.app.utils.Result;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProcessorService extends BaseService implements IProcessorService {

    private final GsonBuilder builder = new GsonBuilder();
    private final Gson gson = builder.create();

    public ProcessorService(String url) {
        super(url);
    }

    @Override
    public Result<ProcessedImageDTO> addImageForProcessing(String pathToImage) {
        Path filePath = Paths.get(pathToImage);
        byte[] fileContent = new byte[0];
        try {
            fileContent = Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return addImageForProcessing(fileContent);
    }

    public Result<ProcessedImageDTO> addImageForProcessing(byte[] fileContent)  {

        try{
            var uri = new URI(baseUrl + "process/image");

            String request = gson.toJson(new ProcessorImageRequest(fileContent));
            System.out.println(request);
            HttpResponse<String> response = HttpService.getInstance().sendPost(uri, request);

            ProcessedImageDTO userDTO = gson.fromJson(response.body(), ProcessedImageDTO.class);

            /// TODO: add MQTT listener

            return Result.success(userDTO);
        } catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    @Override
    public Result<ProcessedZipDTO> addImagesForProcessing(String pathToFolder) {
        try{
            /// TODO: add support for multiple files once
            var uri = new URI(baseUrl + "process/images");

            Path filePath = Paths.get("path/to/your/file.zip");

            // Read the file content into a byte array
            byte[] fileContent = Files.readAllBytes(filePath);

            // Build the request with a POST method and the defined URI
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/zip")  // Set the appropriate content type
                    .POST(HttpRequest.BodyPublishers.ofByteArray(fileContent))  // Set the file content as the request body
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());



            return Result.success();
        } catch (Exception e){
            return Result.fail();
        }
    }
}
