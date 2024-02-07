package agh.edu.pl.thumbnail.app.models;

import agh.edu.pl.thumbnail.app.enums.ThumbnailSize;

import java.io.Serializable;

public class Thumbnail implements Serializable {
    private Long id;

    private Image image;

    private QueueItem queueItem;

    private byte[] thumbnail;

    public Thumbnail() {
    }

    public Thumbnail(Image image) {
        this.image = image;
        this.image.addThumbnail(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] bytes){
        this.thumbnail = bytes;
    }

    public int getHeight() {
        return queueItem.getHeight();
    }
    public void setHeight(int height) {
        this.queueItem.setHeight(height);
    }

    public ThumbnailSize getSize() {
        return ThumbnailSize.fromInteger(getHeight());
    }


    public QueueItem getQueueItem(){
        return this.queueItem;
    }
    public void setQueueItem(QueueItem queueItem){
        this.queueItem = queueItem;
    }

}

