package pl.edu.agh.to.thumbnails.server.notifying.interfaces;

public interface IMqttPublishConfig {
    String getUrl();
    Integer getPort();
    String getPublishUsername();
    String getPublishPassword();

}
