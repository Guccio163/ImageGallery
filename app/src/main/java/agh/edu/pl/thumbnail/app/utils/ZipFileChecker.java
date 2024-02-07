package agh.edu.pl.thumbnail.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ZipFileChecker {

    public static boolean isZip(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] signature = new byte[4];
            fis.read(signature);

            // Check if the first 4 bytes match the ZIP file signature
            return isZipSignature(signature);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isZipSignature(byte[] signature) {
        // Check if the first 4 bytes match the ZIP file signature (PK\x03\x04 in ASCII)
        return (signature[0] == 80 && signature[1] == 75 && signature[2] == 3 && signature[3] == 4);
    }
}
