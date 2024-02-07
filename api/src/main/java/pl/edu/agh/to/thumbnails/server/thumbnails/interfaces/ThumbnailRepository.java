package pl.edu.agh.to.thumbnails.server.thumbnails.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;

import java.util.Collection;
import java.util.List;

@Repository
public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
    @Query("SELECT t FROM Thumbnail t " +
            "JOIN t.queueItem q " +
            "WHERE t.image.id = :imageId AND q.height = :height")
    Thumbnail getByImageIdAndHeight(
        @Param("imageId") Long imageId,
        @Param("height") int height
    );

    @Query("SELECT t FROM Thumbnail t JOIN t.queueItem q WHERE q.height = :height")
    List<Thumbnail> getAllByHeight(
        @Param("height") int height
    );

    @Query(value = "SELECT * FROM Thumbnail WHERE image_id = :imageId", nativeQuery = true)
    List<Thumbnail> findByImageId(Long imageId);
    
}
