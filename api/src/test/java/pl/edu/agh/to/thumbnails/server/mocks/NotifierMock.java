package pl.edu.agh.to.thumbnails.server.mocks;


import org.springframework.data.util.Pair;
import pl.edu.agh.to.thumbnails.server.notifying.interfaces.INotifier;
import pl.edu.agh.to.thumbnails.server.queue.models.ProcessingStatus;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NotifierMock implements INotifier {

    public final List<Long> successfulOperations = new ArrayList<>();
    public final List<Long> failOperations = new ArrayList<>();

    @Override
    public CompletableFuture<Void> notifySuccess(Thumbnail thumbnail, QueueItem item) {
        successfulOperations.add(thumbnail.getId());
        return null;
    }

    @Override
    public CompletableFuture<Void> notifyFail(Thumbnail thumbnail, QueueItem item) {
        failOperations.add(thumbnail.getId());
        return null;
    }
}
