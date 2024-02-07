package agh.edu.pl.thumbnail.app.models;

import agh.edu.pl.thumbnail.app.ThumbnailApplication;
import agh.edu.pl.thumbnail.app.controllers.FolderController;
import agh.edu.pl.thumbnail.app.controllers.GalleryController;
import agh.edu.pl.thumbnail.app.controllers.PhotoController;
import agh.edu.pl.thumbnail.app.dtos.FolderDTO;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;

import java.io.IOException;

public class FolderListCell extends ListCell<FolderDTO> {

    @Override
    protected void updateItem(FolderDTO folder, boolean empty) {
        super.updateItem(folder, empty);

        if (empty || folder == null) {
            setGraphic(null);
        } else {
            try {
                var resource = ThumbnailApplication.class.getResource("folder-list-cell.fxml");
                FXMLLoader folderLoader = new FXMLLoader(resource);
                folderLoader.load();
                FolderController folderController = folderLoader.getController();

                GalleryController galleryController = GalleryController.getInstance();

                folderController.initialize(folder, galleryController);

                setGraphic(folderLoader.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
