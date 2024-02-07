package agh.edu.pl.thumbnail.app.services.mqtt.interfaces;

public interface IMqttPublishConfig {
    String getUrl();
    Integer getPort();
    String getPublishUsername();
    String getPublishPassword();

}
