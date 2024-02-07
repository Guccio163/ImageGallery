package pl.edu.agh.to.thumbnails.server.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.thumbnails.server.queue.interfaces.IProcessingQueue;
import pl.edu.agh.to.thumbnails.server.queue.interfaces.QueueItemRepository;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.utils.Result;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class InMemoryQueue implements IProcessingQueue {
    private static final int MAX_QUEUED_PHOTOS = 100;
    private final Queue<QueueItem> fifoQueue = new LinkedList<>();

    @Autowired
    private QueueItemRepository queueItemRepository;

    @Override
    public Result<Integer> addItem(QueueItem item) {

        if(fifoQueue.size() >= MAX_QUEUED_PHOTOS){
            return Result.fail(QueueErrors.Full);
        }

        fifoQueue.offer(item);
        return Result.success(fifoQueue.size());
    }

    @Override
    public Result<QueueItem> getItem() {

        if(fifoQueue.isEmpty()){
            RefineQueue();
        }

        if(fifoQueue.isEmpty()){
            return Result.fail(QueueErrors.Empty);
        }

        var item = fifoQueue.poll();

        item.processingStarted();
        queueItemRepository.save(item);

        return Result.success(item);
    }

    private void RefineQueue() {

            var queueItems = queueItemRepository.getOldestQueueItems(MAX_QUEUED_PHOTOS);

            for(var item : queueItems)
            {
                var result = addItem(item);
                if(!result.succeded())
                    break;
            }

    }
}
