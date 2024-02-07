package agh.edu.pl.thumbnail.app.controllers;

import agh.edu.pl.thumbnail.app.dtos.*;
import agh.edu.pl.thumbnail.app.enums.ThumbnailSize;
import agh.edu.pl.thumbnail.app.models.*;
import agh.edu.pl.thumbnail.app.services.HttpService;
import agh.edu.pl.thumbnail.app.services.ImageService;
import agh.edu.pl.thumbnail.app.services.ProcessorService;
import agh.edu.pl.thumbnail.app.services.mqtt.MqttNotifierService;
import agh.edu.pl.thumbnail.app.services.mqtt.interfaces.IObserver;
import agh.edu.pl.thumbnail.app.utils.Result;
import agh.edu.pl.thumbnail.app.utils.Unzip;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static agh.edu.pl.thumbnail.app.utils.RestUtils.decode;
import static agh.edu.pl.thumbnail.app.utils.ZipFileChecker.isZip;

public class GalleryController implements Initializable, IObserver {
    @FXML
    private ListView<Photo> photoListView;
    private ObservableList<Photo> photoModels;
    @FXML
    private ListView<FolderDTO> folderListView;
    private ObservableList<FolderDTO> folders;


    @FXML
    private ListView<AddingImage> imagesListView;
    private ObservableList<AddingImage> items;

    @FXML
    private StackPane dragPane;
    @FXML
    private Button addImagesButton;
    private Rectangle rect;
    private ThumbnailSize quality = ThumbnailSize.SMALL;
    private final ImageService imgService = ImageService.getInstance();
    private final ProcessorService processor = new ProcessorService("http://localhost:8080/");
    private FolderDTO currentFolder = new FolderDTO("/");

    private static GalleryController instance;

    public GalleryController() {
        instance = this;
    }

    public static GalleryController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MqttNotifierService.subscribe(this);

        photoModels = FXCollections.observableArrayList();
        photoListView.setItems(photoModels);
        photoListView.setCellFactory(param -> new PhotoListCell());

        folders = FXCollections.observableArrayList();
        folderListView.setItems(folders);
        folderListView.setCellFactory(param -> new FolderListCell());

        items = FXCollections.observableArrayList();
        imagesListView.setItems(items);
        imagesListView.setCellFactory(param -> new AddingListCell());


        rect = new Rectangle(200, 200, Color.GREY);
        Label dragNdropLabel = new Label("+");
        dragNdropLabel.setFont(new Font(80));
        dragPane.getChildren().add(rect);
        dragPane.getChildren().add(dragNdropLabel);


        items.addListener(new ListChangeListener<AddingImage>() {
            @Override
            public void onChanged(Change<? extends AddingImage> c) {
                Platform.runLater(() -> {
                    imagesListView = new ListView<>(items);

                });
                imagesListView.refresh();
            }
        });


        dragPane.setOnDragEntered(event -> {
            rect.setFill(Color.DARKGREY);
        });

        dragPane.setOnDragExited(event -> {
            rect.setFill(Color.GREY);
        });

        dragPane.setOnDragOver(event -> {
            if (event.getGestureSource() != dragPane && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        dragPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                File file = db.getFiles().get(0);
                addToStaging(file);
            }
            event.setDropCompleted(success);
            event.consume();

        });

        getThumbnails();
        getFoldersInCurrentFolder();
    }

    public void sendFiles(ActionEvent actionEvent){
        for(AddingImage image: items){
            Result<ProcessedImageDTO> result;

            System.out.println("Image path: " + image.getPath());
            System.out.println("Image group: " + image.getGroupPath());
            System.out.println("Image name: " + image.getName());

            if(Objects.equals(image.getPath(), "")){
                result = processor.addImageForProcessing(image.getFileContent());
            }else{
                result = processor.addImageForProcessing(image.getPath());
            }

            if (!result.succeded()) {
                continue;
            }

            ProcessedImageDTO processedImageDTO = result.getData();

            photoModels.add(new Photo((long) processedImageDTO.getImageId(), "", ""));
            System.out.println("Wysyłam plik ["+ processedImageDTO.getImageId() +"]" + image.getName());
        }
        items.clear();

        refreshList();
    }

    public void refreshList(){
        this.photoListView.refresh();
    }

    public void getThumbnails(){
        System.out.println("Current path: " + currentFolder.getPath());
        if (!currentFolder.getPath().equals("/")) {
            getThumbnailsFromCurrentFolder();
        }

        Result<ThumbnailsListDTO> result = imgService.getAllThumbnails(quality.getName());

        if (!result.succeded()) {
            System.out.println("Failed to fetch thumbnails with size: " + quality.getName());
            return;
        }

        List<ThumbnailDTO> thumbnailsList = result.getData().getThumbnails();

        photoModels.clear();
        for (ThumbnailDTO thumbnailDTO: thumbnailsList) {
            photoModels.add(new Photo(thumbnailDTO.getImageId(), thumbnailDTO.getThumbnail(), thumbnailDTO.getThumbnail()));
        }

        photoListView.refresh();
    }

    public void getThumbnailsFromCurrentFolder(){
        if (currentFolder.getPath().equals("/")) {
            getThumbnails();
        }

        Result<ThumbnailsListDTO> result = imgService.getAllThumbnailsFromFolder(quality.getName(), currentFolder);

        if (!result.succeded()) {
            System.out.println("Failed to fetch thumbnails with size: " + quality.getName() + "from folder: " + currentFolder);
            return;
        }

        List<ThumbnailDTO> thumbnailsList = result.getData().getThumbnails();

        photoModels.clear();
        for (ThumbnailDTO thumbnailDTO: thumbnailsList) {
            photoModels.add(new Photo(thumbnailDTO.getImageId(), thumbnailDTO.getThumbnail(), thumbnailDTO.getThumbnail()));
        }
        /// TODO: Check why sometimes photoListView doesn't refresh
        System.out.println("PhotoModels: " + photoModels);
        photoListView.refresh();
    }

    public void getFoldersInCurrentFolder(){
        Result<FoldersListDTO> result = imgService.getFoldersInCurrentFolder();

        if (!result.succeded()) {
            System.out.println("Failed to fetch folders from folder: " + currentFolder.getPath());
            return;
        }

        List<String> foldersList = result.getData().getFolderIDs();
        List<FolderDTO> folderDTOs = result.getData().getFolders();

        refreshFolders(folderDTOs);
    }

    private void refreshFolders(List<FolderDTO> folders) {

        List<FolderDTO> preparedFolders = folders.stream().map(folder -> {
            folder.setPath("/" + folder.getPath());
            return folder;
        }).toList();

        folders.add(new FolderDTO("/"));

        this.folders.addAll(preparedFolders);

        folders = folders.stream().sorted(Comparator.comparing(FolderDTO::getPath)).toList();
        this.folders.clear();
        this.folders.addAll(folders);
        folderListView.refresh();
    }

    private void refreshImage(Long imageId) {
        Result<ThumbnailDTO> result = imgService.getThumbnail(imageId, quality.getName());
        if (!result.succeded())
            return;

        for (Photo photo: photoModels) {
            System.out.println(photo.getId() + " " + imageId);
            if (photo.getId().equals(imageId)) {
                photo.setThumbnailUrl(result.getData().getThumbnail());
                System.out.println(photo.getThumbnailUrl());
                photoListView.refresh();
                return;
            }
        }

        photoModels.add(new Photo(imageId, result.getData().getThumbnail(), ""));
        photoListView.refresh();
    }

    private void removeImage(Long imageId) {

        for (int index = 0; index < photoModels.size(); index++) {
            if (photoModels.get(index).getId().equals(imageId)) {
                photoModels.remove(index);
                photoListView.refresh();
                break;
            }
        }
    }

    public BufferedImage getFullImage(Long id) throws IOException {
        Result<FullImageDTO> result = imgService.getFullImage(id);
        if (result.succeded()){
            return ImageIO.read(new ByteArrayInputStream(decode(result.getData().getImage())));
        } else{
            System.out.println("cos sie zepsulo");
        }
        return null;
    }

    private void addToStaging(File file) {
        try {
            if(isZip(file)){
                items.addAll(Unzip.getUnzippedImages(file));
            }else{
                Image image = new Image(new FileInputStream(file));
                items.add(new AddingImage(image, file.getName(), file.getPath()));
            }
            this.addImagesButton.setDisable(false);
            imagesListView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chSmallS(){
        for(Photo photo : photoModels){
            photo.chSmallS();
        }
        photoListView.refresh();
    }
    public void chMediumS(){
        for(Photo photo : photoModels){
            photo.chMediumS();
        }
        photoListView.refresh();
    }
    public void chLargeS(){
        for(Photo photo : photoModels){
            photo.chLargeS();
        }
        photoListView.refresh();
    }

    public void chLowQ(){
        this.quality = ThumbnailSize.SMALL;
        getThumbnails();
    }
    public void chMediumQ(){
        this.quality = ThumbnailSize.MEDIUM;
        getThumbnails();
    }
    public void chHighQ(){
        this.quality = ThumbnailSize.LARGE;
        getThumbnails();
    }

    @Override
    public void signal(Long id, boolean success) {
        System.out.println(id + " " + success + " test");

        if (success) {
            refreshImage(id);
        } else {
            removeImage(id);
        }
    }

    public void setCurrentFolder(FolderDTO folder){
        this.currentFolder = folder;
    }
}
