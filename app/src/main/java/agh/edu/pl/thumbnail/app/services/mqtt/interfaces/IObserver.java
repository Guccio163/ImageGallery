package agh.edu.pl.thumbnail.app.services.mqtt.interfaces;

public interface IObserver {
    void signal(Long id, boolean success);
}
