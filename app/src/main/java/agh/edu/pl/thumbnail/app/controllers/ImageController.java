package agh.edu.pl.thumbnail.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageController {
    @FXML
    private ImageView thumbnailImageView;
    @FXML
    private Label nameLabel;

    public void initialize(Image img, String name) {
        nameLabel.setText(name);
        thumbnailImageView.setFitWidth(100);
        thumbnailImageView.setFitHeight(100);
        thumbnailImageView.setImage(img);
        thumbnailImageView.setVisible(true);
    }
}
