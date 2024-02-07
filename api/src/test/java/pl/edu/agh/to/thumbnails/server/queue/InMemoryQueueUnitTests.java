package pl.edu.agh.to.thumbnails.server.queue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.agh.to.thumbnails.server.queue.interfaces.QueueItemRepository;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.utils.Result;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = InMemoryQueueUnitTests.class)
public class InMemoryQueueUnitTests {

    @Mock
    private QueueItemRepository queueItemRepository;

    @InjectMocks
    InMemoryQueue imq = new InMemoryQueue();

    @Test
    public void addItemTest() {
        InMemoryQueue imq = new InMemoryQueue();
        QueueItem qi = new QueueItem();
        Result<Integer> result = imq.addItem(qi);
        Assertions.assertEquals(1, result.getData());
        Assertions.assertTrue(result.succeded());
    }

    @Test
    public void queueOverflowTest() {
        InMemoryQueue imq = new InMemoryQueue();
        for (int i = 0; i < 100; i++) {
            QueueItem qi = new QueueItem();
            imq.addItem(qi);
        }
        Result<Integer> result = imq.addItem(new QueueItem());
        assertNull(result.getData());
        Assertions.assertTrue(result.failed());
        Assertions.assertNotEquals(result.getError(), null);
    }

    @Test
    public void getQueueItemTest() {
        QueueItem qi = new QueueItem();
        imq.addItem(qi);
        Result<QueueItem> result = this.imq.getItem();
        Assertions.assertEquals(qi, result.getData());
        Assertions.assertTrue(result.succeded());
    }

    @Test
    public void getQueueItemFromEmptyTest() {
        QueueItem qi = new QueueItem();
        this.imq.addItem(qi);
        this.imq.getItem();
        try {
            this.imq.getItem();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
        }
    }
}
