package pl.edu.agh.to.thumbnails.server.mocks;

import pl.edu.agh.to.thumbnails.server.queue.QueueErrors;
import pl.edu.agh.to.thumbnails.server.queue.interfaces.IProcessingQueue;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.LinkedList;
import java.util.Queue;

public class ProcessingQueueMock implements IProcessingQueue {

    private final Queue<QueueItem> fifoQueue = new LinkedList<>();

    @Override
    public Result<Integer> addItem(QueueItem item) {
        return Result.success(fifoQueue.size());
    }

    @Override
    public Result<QueueItem> getItem() {

        if(fifoQueue.isEmpty())
            return Result.fail(QueueErrors.Empty);

        return Result.success(fifoQueue.poll());
    }
}
