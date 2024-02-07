package agh.edu.pl.thumbnail.app.utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;

import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static <T> boolean isSuccessfullStatusCode(HttpResponse<T> response){
        return response.statusCode() >= 200 && response.statusCode() < 300;
    }

    public static byte[] fxImageToByteArray (Image image) {
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();

        byte[] buffer = new byte[height * width * 4];

        image.getPixelReader().getPixels(
                0, 0,
                width, height,
                PixelFormat.getByteBgraInstance(),
                buffer,
                0, width * 4
        );

        return buffer;
    }

    public static String fxImageToBase64(Image image) {
        return encode(fxImageToByteArray(image));
    }

    private static final Pattern base64Pattern = Pattern.compile(
        "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$\n"
    );
    public static boolean isBase64(String string) {
        Matcher matcher = base64Pattern.matcher(string);
        return matcher.find();
    }
}
