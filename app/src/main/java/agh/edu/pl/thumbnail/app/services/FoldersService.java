package agh.edu.pl.thumbnail.app.services;

import agh.edu.pl.thumbnail.app.dtos.FolderDTO;
import agh.edu.pl.thumbnail.app.dtos.FoldersListDTO;
import agh.edu.pl.thumbnail.app.dtos.ThumbnailsListDTO;
import agh.edu.pl.thumbnail.app.utils.LocalDateTimeTypeAdapter;
import agh.edu.pl.thumbnail.app.utils.RestUtils;
import agh.edu.pl.thumbnail.app.utils.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.ReadOnlySetProperty;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class FoldersService {
    private static FoldersService instance = null;

    private FoldersService() {}

    public static FoldersService getInstance() {
        if (instance == null) {
            instance = new FoldersService();
        }
        return instance;
    }



    private String baseUrl = "http://localhost:8080/";
    private String url = baseUrl + "folders";

    private final GsonBuilder builder = new GsonBuilder()
            .registerTypeAdapter(
                    LocalDateTime.class,
                    new LocalDateTimeTypeAdapter()
            );

    private final Gson gson = builder.create();


    public Result<FoldersListDTO> getAllFolders() {

        URI uri = prepareUri("");

        HttpResponse<String> response = HttpService.getInstance().sendGet(uri);


        if (!RestUtils.isSuccessfullStatusCode(response)) {
            return Result.fail();
        }

        var result = gson.fromJson(response.body(), FoldersListDTO.class);

        return Result.success(result);
    }

    public Result<ThumbnailsListDTO> getAllThumbnails(FolderDTO folder, String size) {
        URI uri = prepareUri( "/" + folder.getId() + "/thumbnails/" + size);

        HttpResponse<String> response = HttpService.getInstance().sendGet(uri);

        var result = gson.fromJson(response.body(), ThumbnailsListDTO.class);
        if (RestUtils.isSuccessfullStatusCode(response)) {
            return Result.success(result);
        } else {
            return Result.fail();
        }
    }

    private URI prepareUri (String path) {
        try {
            return new URI(url + path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
