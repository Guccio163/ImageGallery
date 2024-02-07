package agh.edu.pl.thumbnail.app.services.mqtt;


import agh.edu.pl.thumbnail.app.models.QueueItem;
import agh.edu.pl.thumbnail.app.models.Thumbnail;
import agh.edu.pl.thumbnail.app.services.mqtt.interfaces.IMqttPublishConfig;
import agh.edu.pl.thumbnail.app.services.mqtt.interfaces.IObserver;
import agh.edu.pl.thumbnail.app.services.mqtt.models.Notification;
import agh.edu.pl.thumbnail.app.services.mqtt.models.SuccessNotification;
import com.google.gson.Gson;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import com.hivemq.client.rx.FlowableWithSingle;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MqttNotifierService {
    static {
        instance = new MqttNotifierService(new MqttConfig());
    }

    private static MqttNotifierService instance;

    public static MqttNotifierService getInstance() {
        return instance;
    }

    public static final String SUCCESS_TOPIC = "success";
    public static final String FAIL_TOPIC = "fail";

    private final IMqttPublishConfig config;
//    private Mqtt5RxClient mqttClient;
    private Mqtt5AsyncClient mqttClient;


    private Gson gson;

    private MqttNotifierService(IMqttPublishConfig config) {
        this.config = config;
        this.mqttClient = Mqtt5Client
                .builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(config.getUrl())
                .serverPort(config.getPort())
                .sslWithDefaultConfig()
                .automaticReconnectWithDefaultConfig()
                .addConnectedListener(context -> System.out.println("Connected to MQTT Broker"))
                .addDisconnectedListener(context -> System.out.println("Disconnected from MQTT Broker"))
                .buildAsync();

        System.out.println("Notifier setting up");

        mqttClient
            .connectWith()
            .simpleAuth()
            .username(config.getPublishUsername())
            .password(config.getPublishPassword().getBytes())
            .applySimpleAuth()
            .send()
            .whenComplete((connAck, throwable) -> {
                if (throwable != null) {
                    System.out.println("Could not connect to HiveMQ: " + throwable.getMessage());
                } else {
                    System.out.println("Connected to HiveMQ: " + connAck.getReasonCode());
                }
            });

        mqttClient
            .subscribeWith()
            .topicFilter(SUCCESS_TOPIC)
            .qos(MqttQos.AT_LEAST_ONCE)
            .callback(message -> {
                try {
                    String payload = new String(message.getPayloadAsBytes());
                    Long imageId = Long.valueOf(payload);
                    System.out.println("Received success notification for: " + imageId);
                    MqttNotifierService.signal(imageId, true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            })
            .send();

        mqttClient
            .subscribeWith()
            .topicFilter(FAIL_TOPIC)
            .qos(MqttQos.AT_LEAST_ONCE)
            .callback(message -> {
                try {
                    String payload = new String(message.getPayloadAsBytes());
                    Long imageId = Long.valueOf(payload);
                    System.out.println("Received fail notification for: " + imageId);
                    MqttNotifierService.signal(imageId, false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            })
            .send();
    }

    public CompletableFuture<Void> notifySuccess(Thumbnail thumbnail, QueueItem item) {
        var topic = formatThumbnailTopic(thumbnail.getId());
        var message = SuccessNotification.notification(item, thumbnail.getThumbnail());
        var jsonMessage = gson.toJson(message);

        publishMessage(topic, jsonMessage, true);
        publishMessage(SUCCESS_TOPIC, thumbnail.getImage().toString(), false);

        return CompletableFuture.completedFuture(null);
    }

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

//        var result = mqttClient.publish(Flowable.just(options)).subscribe();

        return CompletableFuture.completedFuture(null);
    }

    private static String formatThumbnailTopic(Long id) {
        return "thumbnail/" + id;
    }

    private static final List<IObserver> observers = new LinkedList<>();

    public static void subscribe(IObserver observer) {
        observers.add(observer);
    }

    private static void signal(Long imageId, boolean success) {
        for (IObserver observer: observers) {
            observer.signal(imageId, success);
        }

        System.out.println(imageId + " " + success + " " + observers.size());
    }
}
