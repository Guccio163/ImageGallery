package agh.edu.pl.thumbnail.app.services.mqtt.interfaces;

import agh.edu.pl.thumbnail.app.models.QueueItem;
import agh.edu.pl.thumbnail.app.models.Thumbnail;

import java.util.concurrent.CompletableFuture;

public interface INotifier {

    CompletableFuture<Void> notifySuccess(Thumbnail thumbnail, QueueItem item);
    CompletableFuture<Void> notifyFail(Thumbnail thumbnailId, QueueItem item);
}
