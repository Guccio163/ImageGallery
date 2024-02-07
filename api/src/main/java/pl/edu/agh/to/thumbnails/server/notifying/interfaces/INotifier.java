package pl.edu.agh.to.thumbnails.server.notifying.interfaces;

import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;

import java.util.concurrent.CompletableFuture;

public interface INotifier {

    CompletableFuture<Void> notifySuccess(Thumbnail thumbnail, QueueItem item);
    CompletableFuture<Void> notifyFail(Thumbnail thumbnailId, QueueItem item);
}
