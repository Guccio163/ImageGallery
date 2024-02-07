package pl.edu.agh.to.thumbnails.server.utils;

import java.util.Base64;

public class RestUtils {
    public static String encode(byte[] image) {
        if (image == null)
            return null;
        return Base64.getEncoder().encodeToString(image);
    }

    public static byte[] decode(String encoded) {
        if (encoded == null)
            return null;
        return Base64.getDecoder().decode(encoded);
    }
}
