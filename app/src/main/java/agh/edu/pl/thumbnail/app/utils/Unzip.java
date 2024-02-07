package agh.edu.pl.thumbnail.app.utils;
import agh.edu.pl.thumbnail.app.models.AddingImage;
import agh.edu.pl.thumbnail.app.models.Photo;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// IMPORTANT NOTES: if you upload zip, every file has a name, the shortest is the root folder
//                  if name ends with / then it is a dir, if not then file
public class Unzip {

    public static List<AddingImage> getUnzippedImages(File zipFile) {
//        String zipFile = "/Users/wiktorgut/Desktop/TOP/mi-pt-1500-drop_database/app/src/main/resources/dups.zip";
        List<AddingImage> images = new ArrayList<>();

        try {
            unzipImages(zipFile, images);
            System.out.println("Unzipping images completed.");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return images;
    }

    public static void unzipImages(File zipFile, List<AddingImage> images) throws IOException {
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = zipIn.getNextEntry();
            var counter = 0;

            while (entry != null) {
                String entryName = entry.getName();
                int index = entryName.lastIndexOf('/');

                if (!entry.isDirectory()) {
                    if(entryName.charAt(index + 1) != '.'){
                        counter++;
                        System.out.println(counter + "  " + entry.getName());
                        byte[] imageData = extractImageData(zipIn);
                        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                        Image dupa = new Image(bis);
                        images.add(new AddingImage(dupa, imageData, entryName, entryName));
                    }
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private static byte[] extractImageData(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }
}