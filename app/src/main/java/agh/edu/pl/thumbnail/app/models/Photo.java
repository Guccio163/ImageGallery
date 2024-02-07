package agh.edu.pl.thumbnail.app.models;

import javafx.beans.property.*;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;

import java.io.File;

public class Photo{
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty thumbnailUrl = new SimpleStringProperty();
    private final StringProperty fullImageUrl = new SimpleStringProperty();
    private IntegerProperty thumbnailSize = new SimpleIntegerProperty();
    private final StringProperty groupPath = new SimpleStringProperty();

    public Photo(Long id, String thumbnailUrl, String fullImageUrl) {
            this.id.set(id);
            this.thumbnailUrl.set(thumbnailUrl);
            this.fullImageUrl.set(fullImageUrl);
            this.thumbnailSize.set(120);
            this.groupPath.set("");
        }
    public Photo(Long id, String thumbnailUrl, String fullImageUrl, String path) {
        this.id.set(id);
        this.thumbnailUrl.set(thumbnailUrl);
        this.fullImageUrl.set(fullImageUrl);
        this.thumbnailSize.set(120);
        this.groupPath.set(path);
    }

        public Long getId() {
            return id.get();
        }

        public LongProperty idProperty() {
            return id;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl.get();
        }

        public void setThumbnailUrl(String thumbnailUrl){
            this.thumbnailUrl.setValue(thumbnailUrl);
        }

        public StringProperty thumbnailUrlProperty() {
            return thumbnailUrl;
        }

        public String getFullImageUrl() {
            return fullImageUrl.get();
        }

        public StringProperty fullImageUrlProperty() {
            return fullImageUrl;
        }
        public void chSmallS(){
            thumbnailSize.set(100);
        }
        public void chMediumS(){
            thumbnailSize.set(120);
        }
        public void chLargeS(){
            thumbnailSize.set(140);
        }


    public int getThumbnailSize() {
        return thumbnailSize.get();
    }

    public String getGroupPath(){return groupPath.get();}
}
