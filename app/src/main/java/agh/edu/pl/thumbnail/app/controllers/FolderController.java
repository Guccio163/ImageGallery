package agh.edu.pl.thumbnail.app.controllers;

import agh.edu.pl.thumbnail.app.dtos.FolderDTO;
import agh.edu.pl.thumbnail.app.dtos.FullImageDTO;
import agh.edu.pl.thumbnail.app.models.Photo;
import agh.edu.pl.thumbnail.app.services.ImageService;
import agh.edu.pl.thumbnail.app.utils.RestUtils;
import agh.edu.pl.thumbnail.app.utils.Result;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;

public class FolderController {
    @FXML
    private Label nameLabel;
    private FolderDTO folder;

    public void initialize(FolderDTO folder, GalleryController thisGC) {
        this.folder = folder;
        nameLabel.setText(this.folder.getPath());

        nameLabel.setOnMouseClicked(event -> Platform.runLater(() -> {
            System.out.println("Clicked on: "+ folder.getPath());
            thisGC.setCurrentFolder(folder);
            thisGC.getThumbnailsFromCurrentFolder();
            thisGC.getFoldersInCurrentFolder();
            System.out.println("Otwarto folder " + folder.getPath());
        }));
    }


}
