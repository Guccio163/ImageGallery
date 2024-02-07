package agh.edu.pl.thumbnail.app.services.mqtt;

import agh.edu.pl.thumbnail.app.services.mqtt.interfaces.IMqttPublishConfig;
import agh.edu.pl.thumbnail.app.services.mqtt.interfaces.IMqttSubscribeConfig;

public class MqttConfig implements IMqttPublishConfig, IMqttSubscribeConfig {
    @Override
    public String getUrl() {
        return "ef57f832f11b4e89960ef452f56e6aa3.s2.eu.hivemq.cloud";
    }

    @Override
    public Integer getPort() {
        return 8883;
    }

    @Override
    public String getPublishUsername() {
        return "Test123";
    }

    @Override
    public String getPublishPassword() {
        return "Test1234";
    }

    @Override
    public String getSubscribeUsername() {
        return "Test123";
    }

    @Override
    public String getSubscribePassword() {
        return "Test1234";
    }
}
