package pl.edu.agh.to.thumbnails.server.thumbnails.model;

import jakarta.persistence.*;
import pl.edu.agh.to.thumbnails.server.images.models.Image;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.thumbnails.enums.ThumbnailSize;

import java.io.Serializable;

@Entity
public class Thumbnail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToOne(cascade = CascadeType.ALL)
    private QueueItem queueItem;

    @Lob
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

    public Image getImage() {
        return image;
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

    public ThumbnailSize getSize() {
        return ThumbnailSize.fromInteger(getHeight());
    }

    public QueueItem getQueueItem(){
        return this.queueItem;
    }
    public void setQueueItem(QueueItem queueItem){
        this.queueItem = queueItem;
    }

    @Override
    public String toString()
    {
        return "";
    }
}
