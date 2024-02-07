package agh.edu.pl.thumbnail.app.models;

import agh.edu.pl.thumbnail.app.ThumbnailApplication;
import agh.edu.pl.thumbnail.app.controllers.PhotoController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;

import java.io.IOException;

public class PhotoListCell extends ListCell<Photo> {

    @Override
    protected void updateItem(Photo photoModel, boolean empty) {
        super.updateItem(photoModel, empty);

        if (empty || photoModel == null) {
            setGraphic(null);
        } else {
            try {
                var resource = ThumbnailApplication.class.getResource("photo-list-cell.fxml");
                FXMLLoader loader = new FXMLLoader(resource);
                loader.load();

                PhotoController controller = loader.getController();
                controller.initialize(photoModel);

                setGraphic(loader.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
