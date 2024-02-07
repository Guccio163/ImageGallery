package pl.edu.agh.to.thumbnails.server.queue.interfaces;

import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.utils.Result;

public interface IProcessingQueue {
    Result<Integer> addItem(QueueItem item);
    Result<QueueItem> getItem();
}
