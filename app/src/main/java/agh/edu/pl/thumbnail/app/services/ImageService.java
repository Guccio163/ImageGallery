package agh.edu.pl.thumbnail.app.services;

import agh.edu.pl.thumbnail.app.dtos.*;
import agh.edu.pl.thumbnail.app.models.Thumbnail;
import agh.edu.pl.thumbnail.app.utils.LocalDateTimeTypeAdapter;
import agh.edu.pl.thumbnail.app.utils.RestUtils;
import agh.edu.pl.thumbnail.app.utils.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ImageService {

    private final GsonBuilder builder = new GsonBuilder()
        .registerTypeAdapter(
            LocalDateTime.class,
            new LocalDateTimeTypeAdapter()
        );

    private final Gson gson = builder.create();

    //// TODO: do it better
    private String baseUrl = "http://localhost:8080/";
    private String url = baseUrl + "images";

    private static ImageService instance = null;

    public static ImageService getInstance() {
        if (instance == null)
            instance = new ImageService();
        return instance;
    }

    private ImageService() {}

    private URI prepareUri (String path) {
        try {
            return new URI(url + path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    };
    public Result<ImageDetailsListDTO> getAllImages() {
        URI uri = prepareUri("/all");

        HttpResponse<String> response = HttpService.getInstance().sendGet(uri);

        if (RestUtils.isSuccessfullStatusCode(response)) {
            return Result.success(gson.fromJson(response.body(), ImageDetailsListDTO.class));
        } else {
            return Result.fail();
        }
    };

    public Result<FullImageDTO> getFullImage(Long imageId) {
        URI uri = prepareUri("/" + imageId + "/full");

        HttpResponse<String> response = HttpService.getInstance().sendGet(uri);

        if (RestUtils.isSuccessfullStatusCode(response)) {
            return Result.success(gson.fromJson(response.body(), FullImageDTO.class));
        } else {
            return Result.fail();
        }
    }

    public Result<ImageWithThumbnailsDTO> getImageWithFullThumbnails(Long imageId) {
        URI uri = prepareUri("/" + imageId + "/thumbnails/full");

        HttpResponse<String> response = HttpService.getInstance().sendGet(uri);

        if (RestUtils.isSuccessfullStatusCode(response)) {
            return Result.success(gson.fromJson(response.body(), ImageWithThumbnailsDTO.class));
        } else {
            return Result.fail();
        }
    }

    public Result<ThumbnailsListDTO> getAllThumbnails(String size) {
        URI uri = prepareUri( "/all/thumbnails/size/" + size);

        HttpResponse<String> response = HttpService.getInstance().sendGet(uri);

        var result = gson.fromJson(response.body(), ThumbnailsListDTO.class);
        if (RestUtils.isSuccessfullStatusCode(response)) {
            return Result.success(result);
        } else {
            return Result.fail();
        }
    }

    public Result<ThumbnailsListDTO> getThumbnails(Long imageId) {
        URI uri = prepareUri("/" + imageId + "/thumbnails");

        HttpResponse<String> response = HttpService.getInstance().sendGet(uri);

        var result = gson.fromJson(response.body(), ThumbnailsListDTO.class);
        if (RestUtils.isSuccessfullStatusCode(response)) {
            return Result.success(result);
        } else {
            return Result.fail();
        }
    }

    public Result<ThumbnailDTO> getThumbnail(Long imageId, String size) {
        URI uri = prepareUri( "/"+ imageId + "/thumbnail/size/" + size);

        HttpResponse<String> response = HttpService.getInstance().sendGet(uri);

        var result = gson.fromJson(response.body(), ThumbnailDTO.class);
        if (RestUtils.isSuccessfullStatusCode(response)) {
            return Result.success(result);
        } else {
            return Result.fail();
        }
    }

    //TODO
    public Result<FoldersListDTO> getFolders(String currentFolderID){
        return Result.success(new FoldersListDTO());
    }

    //TODO
    public Result<ThumbnailsListDTO> getAllThumbnailsFromFolder(String size, FolderDTO folder) {
        return FoldersService.getInstance().getAllThumbnails(folder, size);
    }

    //TODO
    public Result<FoldersListDTO> getFoldersInCurrentFolder() {
        return FoldersService.getInstance().getAllFolders();
    }

}