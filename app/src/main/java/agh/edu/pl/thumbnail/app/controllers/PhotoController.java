package agh.edu.pl.thumbnail.app.controllers;

import agh.edu.pl.thumbnail.app.dtos.FullImageDTO;
import agh.edu.pl.thumbnail.app.models.Photo;
import agh.edu.pl.thumbnail.app.services.ImageService;
import agh.edu.pl.thumbnail.app.utils.RestUtils;
import agh.edu.pl.thumbnail.app.utils.Result;
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

public class PhotoController {
    @FXML
    private ImageView thumbnailImageView;
    private ImageView fullImageView = new ImageView();

    @FXML
    private Label nameLabel;

    @FXML
    private ProgressIndicator progressIndicator = new ProgressIndicator();
    private ProgressIndicator fullImageProgressIndicator = new ProgressIndicator();

    private Photo photo;


    public void initialize(Photo photoModel) {
        this.photo = photoModel;
        nameLabel.setText("Noname");

        // Load the thumbnail image if available
        String thumbnailEncodedImage = photoModel.getThumbnailUrl();
        if (thumbnailEncodedImage != null && !thumbnailEncodedImage.isEmpty()) {
            Image thumbnailImage;

            thumbnailImage = new Image(new ByteArrayInputStream(RestUtils.decode(thumbnailEncodedImage)));

            thumbnailImageView.setFitWidth(photoModel.getThumbnailSize()); // Żądany szerokość
            thumbnailImageView.setFitHeight(photoModel.getThumbnailSize()); // Żądany wysokość
            thumbnailImageView.setImage(thumbnailImage);
            thumbnailImageView.setVisible(true);
            progressIndicator.setVisible(false);
            thumbnailImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    openNewWindowWithImage();
                }
            });
        } else {
            thumbnailImageView.setVisible(false);
            progressIndicator.setVisible(true);
            nameLabel.setText("Loading...");
        }
    }

    private void openNewWindowWithImage() {
        Stage newStage = new Stage();
        Result<FullImageDTO> result = ImageService.getInstance().getFullImage(photo.getId());

        StackPane newRoot = new StackPane();
        newRoot.getChildren().add(fullImageView);
        newRoot.getChildren().add(fullImageProgressIndicator);

        if (result.succeded()) {
            byte[] image = RestUtils.decode(result.getData().getImage());
            Image fullImage = new Image(new ByteArrayInputStream(image));
            fullImageView.setImage(fullImage);
            fullImageView.setFitWidth(400);
            fullImageView.setFitHeight(300);
            fullImageProgressIndicator.setVisible(false);
            fullImageView.setVisible(true);
        } else {
            fullImageProgressIndicator.setVisible(true);
        }

        Scene newScene = new Scene(newRoot, 400, 300);
        newStage.initStyle(StageStyle.UTILITY);
        newStage.setTitle("Image Viewer");
        newStage.setScene(newScene);
        newStage.show();
    }
}
