package pl.edu.agh.to.thumbnails.server.images.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.thumbnails.server.images.models.Image;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
    void deleteById(Long id);
    List<Image> findAll();
    List<Image> findAllByIdIn(List<Long> imageIds);
    Page<Image> findByFolderId(@Param("folder_id") Long folderId, Pageable pageable);
    @Query("SELECT i FROM Image i WHERE i.folder IS NULL")
    Page<Image> findNoFolderImages(Pageable pageable);
}
