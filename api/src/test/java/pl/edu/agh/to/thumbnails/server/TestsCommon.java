package pl.edu.agh.to.thumbnails.server;

public class TestsCommon {
    public static final int SUCCESSFUL_STATUS_CODE = 200;
    public static final int BAD_REQUEST_STATUS_CODE = 400;
    public static final int THREAD_DELAY = 1000;
    public static final String VALID_IMAGE = "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";
    public static final String INVALID_IMAGE = "VGhpcyBpcyBhIHRlc3Qgd2hlbiB5b3U=";
    public static final String INVALID_IMAGE_BYTES_FOR_BASE64 = "aSD!@#$%^";
    public static final short NO_CONTENT = 204;
    public static final short CONFLICT = 409;
}