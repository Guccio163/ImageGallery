package pl.edu.agh.to.thumbnails.server.folders.responses;

import pl.edu.agh.to.thumbnails.server.folders.models.FolderDTO;

import java.util.List;

public class GetFoldersResponses {
    private List<FolderDTO> folders;

    public GetFoldersResponses(){
    }
    public GetFoldersResponses(List<FolderDTO> folders) {
        this.folders = folders;
    }

    public void setFolders(List<FolderDTO> folders) {
        this.folders = folders;
    }

    public List<FolderDTO> getFolders() {
        return folders;
    }
}
