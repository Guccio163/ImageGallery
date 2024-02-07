package agh.edu.pl.thumbnail.app.dtos;

import agh.edu.pl.thumbnail.app.enums.ProcessingStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FoldersListDTO {

    private List<String> folderIDs;

    private List<FolderDTO> folders = new ArrayList<>();

    public FoldersListDTO(){
        this.folderIDs = new ArrayList<>();
    }

    public List<FolderDTO> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderDTO> folders) {
        this.folders = folders;
    }

    public List<String> getFolderIDs() {
        return folderIDs;
    }
    public void setFolderIDs(List<String> imageIds) {
        this.folderIDs = imageIds;
    }
}
