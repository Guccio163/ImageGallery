package agh.edu.pl.thumbnail.app.utils;


public class Result<T> {

    private boolean succeeded;
    private T data;

    private Result(T data, boolean succeded) {
        this.data = data;
        this.succeeded = succeded;
    }

    public T getData() {
        return data;
    }
    public boolean succeded(){
        return succeeded;
    }

    public static <T> Result<T> success() {
        return new Result<>(null, true);
    }
    public static <T> Result<T> fail() {
        return new Result<>(null, false);
    }
    public static <T> Result<T> success(T data) {
        return new Result<>(data, true);
    }
    public static <T> Result<T> fail(T data) {
        return new Result<>(data, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return succeeded == result.succeeded;
    }
}
