package agh.edu.pl.thumbnail.app.models;

import agh.edu.pl.thumbnail.app.ThumbnailApplication;
import agh.edu.pl.thumbnail.app.controllers.ImageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;

import java.io.IOException;

public class AddingListCell extends ListCell<AddingImage> {

    @Override
    protected void updateItem(AddingImage addingImage, boolean empty) {
        super.updateItem(addingImage, empty);

        if (empty || addingImage.getImage() == null) {
            setGraphic(null);
        } else {
            try {
                var resource = ThumbnailApplication.class.getResource("adding-list-cell.fxml");
                FXMLLoader loader = new FXMLLoader(resource);
                loader.load();

                ImageController controller = loader.getController();
                controller.initialize(addingImage.getImage(), addingImage.getName());

                setGraphic(loader.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
