package pl.edu.agh.to.thumbnails.server.folders.interfaqces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.thumbnails.server.folders.models.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findByPath(String path);
}
