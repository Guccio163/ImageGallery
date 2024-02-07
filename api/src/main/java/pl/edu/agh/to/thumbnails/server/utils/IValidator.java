package pl.edu.agh.to.thumbnails.server.utils;

public interface IValidator<T> {
    Result<T> validate(T item);
}
