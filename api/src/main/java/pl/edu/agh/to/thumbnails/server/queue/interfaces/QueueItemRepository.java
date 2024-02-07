package pl.edu.agh.to.thumbnails.server.queue.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;

import java.util.List;

@Transactional
public interface QueueItemRepository extends JpaRepository<QueueItem, Long> {

    @Query(value = "SELECT * FROM queue_item WHERE processing_status = 0 ORDER BY created_at ASC LIMIT :n", nativeQuery = true)
    List<QueueItem> getOldestQueueItems(@Param("n") int n);

}
