package pl.edu.agh.to.thumbnails.server.notifying;


import com.google.gson.Gson;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.thumbnails.server.notifying.interfaces.IMqttPublishConfig;
import pl.edu.agh.to.thumbnails.server.notifying.interfaces.INotifier;
import pl.edu.agh.to.thumbnails.server.notifying.models.Notification;
import pl.edu.agh.to.thumbnails.server.notifying.models.SuccessNotification;
import pl.edu.agh.to.thumbnails.server.processing.ImageProcessorController;
import pl.edu.agh.to.thumbnails.server.queue.models.QueueItem;
import pl.edu.agh.to.thumbnails.server.thumbnails.model.Thumbnail;
import pl.edu.agh.to.thumbnails.server.utils.RestUtils;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class MqttNotifierService implements INotifier {

    public static final String SUCCESS_TOPIC = "success";
    public static final String FAIL_TOPIC = "fail";

    private static final Logger log = LoggerFactory.getLogger(ImageProcessorController.class);

    private final IMqttPublishConfig config;
    private Mqtt5RxClient mqttClient;

    @Autowired
    private Gson gson;

    public MqttNotifierService(IMqttPublishConfig config) {
        this.config = config;
        this.mqttClient = Mqtt5Client
                .builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(config.getUrl())
                .serverPort(config.getPort())
                .sslWithDefaultConfig()
                .automaticReconnectWithDefaultConfig()
                .addConnectedListener(context -> log.info("Connected to MQTT Broker"))
                .addDisconnectedListener(context -> log.info("Disconnected from MQTT Broker"))
                .buildRx();

        mqttClient.toBlocking()
                .connectWith()
                .simpleAuth()
                .username(config.getPublishUsername())
                .password(config.getPublishPassword().getBytes())
                .applySimpleAuth()
                .send();
    }

    @Override
    public CompletableFuture<Void> notifySuccess(Thumbnail thumbnail, QueueItem item) {
        var topic = formatThumbnailTopic(thumbnail.getId());
        var message = SuccessNotification.notification(item, thumbnail.getThumbnail());
        var jsonMessage = gson.toJson(message);

        publishMessage(topic, jsonMessage, true);
        publishMessage(SUCCESS_TOPIC, thumbnail.getImage().toString(), false);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> notifyFail(Thumbnail thumbnail, QueueItem item) {
        var topic = formatThumbnailTopic(thumbnail.getId());
        var message = Notification.failNotification(item);
        var jsonMessage = gson.toJson(message);

        publishMessage(topic, jsonMessage, true);
        publishMessage(FAIL_TOPIC, thumbnail.getImage().toString(), false);

        return CompletableFuture.completedFuture(null);
    }

    protected CompletableFuture<Void> publishMessage(String topic, String message, boolean retain)
    {
        var options = Mqtt5Publish.builder()
                .topic(topic)
                .payload(message.getBytes())
                .retain(retain)
                .build();

        var result = mqttClient.publish(Flowable.just(options)).subscribe();

        return CompletableFuture.completedFuture(null);
    }

    private static String formatThumbnailTopic(Long id) {
        return "thumbnail/" + id;
    }
}
