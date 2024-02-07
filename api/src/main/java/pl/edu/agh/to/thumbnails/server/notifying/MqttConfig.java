package pl.edu.agh.to.thumbnails.server.notifying;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.thumbnails.server.notifying.interfaces.IMqttPublishConfig;
import pl.edu.agh.to.thumbnails.server.notifying.interfaces.IMqttSubscribeConfig;

@Component
public class MqttConfig implements IMqttPublishConfig, IMqttSubscribeConfig
{
    @Value("${hivemq.url}")
    private String uri;
    @Value("${hivemq.port}")
    private Integer port;
    @Value("${hivemq.username_publish}")
    private String api_username;
    @Value("${hivemq.password_publish}")
    private String api_password;
    @Value("${hivemq.username_subscribe}")
    private String app_username;
    @Value("${hivemq.password_subscribe}")
    private String app_password;

    public String getUrl() {
        return uri;
    }

    public String getPublishUsername() {
        return api_username;
    }

    public String getPublishPassword() {
        return api_password;
    }

    public String getSubscribeUsername() {
        return app_username;
    }

    public String getSubscribePassword() {
        return app_password;
    }

    public Integer getPort() {
        return port;
    }
}
