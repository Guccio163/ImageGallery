package agh.edu.pl.thumbnail.app.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.io.File;

public class AddingImage {
    private final Image image;
    private final String name;
    private final String path;
    private final String groupPath;
    private final byte[] fileContent;

    public AddingImage(Image image, String name, String path) {
        this.image = image;
        this.name = name;
        this.path = path;
        this.groupPath = "";
        this.fileContent = new byte[0];
    }
    public AddingImage(Image image, byte[] fileContent, String name, String groupPath) {
        this.image = image;
        this.name = name;
        this.fileContent = fileContent;
        this.path = "";
        this.groupPath = groupPath;
    }
    public String getName() {
        return name;
    }
    public Image getImage(){return image;}
    public String getPath() {
        return path;
    }
    public String getGroupPath(){return groupPath;}
    public byte[] getFileContent(){return fileContent;}

}
