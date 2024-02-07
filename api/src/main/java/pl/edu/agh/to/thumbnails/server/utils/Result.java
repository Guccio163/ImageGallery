package pl.edu.agh.to.thumbnails.server.utils;

import org.springframework.http.ResponseEntity;

public class Result<T> {

    private boolean succeeded;
    private T data;

    private Error error;

    private Result(T data, boolean succeded, Error error) {

        if(succeded && error != Error.None)
            throw new IllegalArgumentException("When result is successful there must be no error");

        if(!succeded && error == Error.None)
            throw new IllegalArgumentException("When result failed provide error.");

        this.data = data;
        this.succeeded = succeded;
        this.error = error;
    }


    public boolean succeded(){
        return succeeded;
    }

    public boolean failed(){
        return !succeeded;
    }

    public T getData() {
        return data;
    }
    public Error getError() {
        return error;
    }

    public static <T> Result<T> success() {
        return new Result<>(null, true, Error.None);
    }
    public static <T> Result<T> fail(Error error) {
        return new Result<>(null, false, error);
    }
    public static <T> Result<T> success(T data) {
        return new Result<>(data, true, Error.None);
    }
}
