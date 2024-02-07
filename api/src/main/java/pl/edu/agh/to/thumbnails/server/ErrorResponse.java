package pl.edu.agh.to.thumbnails.server;

public class ErrorResponse {
    private String message;
    private String exceptionName;

    public ErrorResponse(String exceptionName, String message) {
        this.message = message;
        this.exceptionName = exceptionName;
    }
    public ErrorResponse(Exception exception, String message) {
        this.message = message;
        this.exceptionName = exception.getClass().getSimpleName();
    }

    public String getMessage() {
        return message;
    }

    public String getExceptionName() {
        return exceptionName;
    }
}
