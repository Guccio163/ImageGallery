package pl.edu.agh.to.thumbnails.server.processing;

import com.hivemq.client.internal.mqtt.MqttBlockingClient;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import pl.edu.agh.to.thumbnails.server.TestsCommon;
import pl.edu.agh.to.thumbnails.server.notifying.MqttConfig;
import pl.edu.agh.to.thumbnails.server.processing.models.request.ProcessImageRequest;
import pl.edu.agh.to.thumbnails.server.processing.models.request.ProcessImagesRequest;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessImageResponse;
import pl.edu.agh.to.thumbnails.server.processing.models.response.ProcessImagesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.edu.agh.to.thumbnails.server.TestsCommon.*;
import static pl.edu.agh.to.thumbnails.server.processing.ImageProcessorController.MAX_ZIP_IMAGES;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProcessingIntegrationTests {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MqttConfig mqttConfig;


    private static final Logger log = LoggerFactory.getLogger(ProcessingIntegrationTests.class);

    private static Mqtt5BlockingClient client;

    private List<String> receivedMessages;

    @BeforeEach
    public void setUpMqtt() throws InterruptedException {

        receivedMessages = new ArrayList<>();

        if(client == null)
        {
            client = MqttClient.builder().identifier(UUID.randomUUID().toString())
                    .serverHost(mqttConfig.getUrl())
                    .serverPort(mqttConfig.getPort())
                    .sslWithDefaultConfig()
                    .automaticReconnectWithDefaultConfig()
                    .addConnectedListener(context -> log.info("Connected to MQTT Broker"))
                    .addDisconnectedListener(context -> log.info("Disconnected from MQTT Broker"))
                    .useMqttVersion5()
                    .buildBlocking();

            var status = client.connectWith()
                    .simpleAuth()
                    .username(mqttConfig.getPublishUsername())
                    .password(mqttConfig.getPublishPassword().getBytes())
                    .applySimpleAuth()
                    .send();

            assertTrue(client.getState().isConnected());

            try (final var publishes = client.publishes(MqttGlobalPublishFilter.ALL)) {

                client.subscribeWith().topicFilter("#").send();

                publishes.receive(2000, TimeUnit.MILLISECONDS).ifPresent(x -> {

                    log.info("Received retained message.");

                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            receivedMessages = new ArrayList<>();
        }
    }

    @Test
    public void pingIsWorking(){
        //Given

        //When
        var response = this.testRestTemplate.getForEntity("/process/ping", String.class);

        //Then
        assertEquals(SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(ImageProcessorController.PROCESSING_PING_MESSAGE, response.getBody());
    }

    private void waitForMessages(int receivedMessagesNumber) throws InterruptedException {
        for(int i = 0; i < 30; i++){

            if(receivedMessages.size() == receivedMessagesNumber)
                break;

            try (final var publishes = client.publishes(MqttGlobalPublishFilter.ALL)) {

                client.subscribeWith().topicFilter("#").send();

                publishes.receive(100, TimeUnit.MILLISECONDS).ifPresent(x -> {

                    receivedMessages.add(x.toString());

                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Thread.sleep(1000);
        assertEquals(receivedMessagesNumber, receivedMessages.size());
    }

    @Test
    public void successfulImageProcessing() throws InterruptedException {
        //Given
        var payload = new ProcessImageRequest(TestsCommon.VALID_IMAGE);

        //When
        var response = this.testRestTemplate.postForEntity("/process/image", payload, ProcessImageResponse.class);

        waitForMessages(1);

        //Then
        assertEquals(SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
    }

    @Test
    public void failingImageProcessing() throws InterruptedException {
        //Given
        var payload = new ProcessImageRequest(TestsCommon.INVALID_IMAGE);

        //When
        var response = this.testRestTemplate.postForEntity("/process/image", payload, ProcessImageResponse.class);
        waitForMessages(1);

        //Then
        assertEquals(SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());

    }

    @Test
    public void invalidImageProvided() throws InterruptedException {
        //Given
        var payload = new ProcessImageRequest(TestsCommon.INVALID_IMAGE_BYTES_FOR_BASE64);
        var jsonResponse = String.format("{\"message\":\"%s\",\"exceptionName\":\"%s\"}",
                "Invalid base64 string.", "IllegalArgumentException");
        //When
        var response = this.testRestTemplate.postForEntity("/process/image", payload, String.class);
        waitForMessages(1);
        //Then
        assertEquals(BAD_REQUEST_STATUS_CODE, response.getStatusCode().value());
        assertEquals(jsonResponse, response.getBody());
    }

    @Test
    public void successfulImageListProcessing() throws InterruptedException {
        //Given
        int imagesCount = 2;
        var images = new ArrayList<String>();
        for(int i = 0; i < imagesCount; i++)
        {
            images.add(TestsCommon.VALID_IMAGE);
        }
        var payload = new ProcessImagesRequest(images);

        //When
        var response = this.testRestTemplate.postForEntity("/process/images", payload, ProcessImagesResponse.class);
        waitForMessages(imagesCount);

        //Then
        assertEquals(SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(imagesCount, Objects.requireNonNull(response.getBody()).getProcessedImages().size());

    }

    @Test
    public void failingImageListProcessing() throws InterruptedException {
        //Given
        int imagesCount = 2;
        var images = new ArrayList<String>();
        for(int i = 0; i < imagesCount; i++)
        {
            images.add(TestsCommon.INVALID_IMAGE);
        }
        var payload = new ProcessImagesRequest(images);

        //When
        var response = this.testRestTemplate.postForEntity("/process/images", payload, ProcessImagesResponse.class);
        waitForMessages(imagesCount);

        //Then
        assertEquals(SUCCESSFUL_STATUS_CODE, response.getStatusCode().value());
        assertEquals(imagesCount, response.getBody().getProcessedImages().size());

    }

    @Test
    public void invalidImageListProvided() throws InterruptedException {
        //Given
        int imagesCount = 2;
        var images = new ArrayList<String>();
        for(int i = 0; i < 2 + imagesCount; i++)
        {
            images.add(TestsCommon.INVALID_IMAGE_BYTES_FOR_BASE64);
        }

        var payload = new ProcessImagesRequest(images);
        var jsonResponse = String.format("{\"message\":\"%s\",\"exceptionName\":\"%s\"}",
                "Invalid base64 string.", "IllegalArgumentException");
        //When
        var response = this.testRestTemplate.postForEntity("/process/images", payload, String.class);
        waitForMessages(imagesCount);
        //Then
        assertEquals(BAD_REQUEST_STATUS_CODE, response.getStatusCode().value());
        assertEquals(jsonResponse, response.getBody());
    }

    @Test
    public void tooMuchImagesProvided() throws InterruptedException {
        //Given

        var images = new ArrayList<String>();

        for(int i = 0; i < 2 + MAX_ZIP_IMAGES; i++)
        {
            images.add(TestsCommon.VALID_IMAGE);
        }

        var payload = new ProcessImagesRequest(images);
        var jsonResponse = String.format("{\"message\":\"%s\",\"exceptionName\":\"%s\"}",
                "To much images uploaded at once, max is"+MAX_ZIP_IMAGES, "TooMuchImages");
        //When
        var response = this.testRestTemplate.postForEntity("/process/images", payload, String.class);
        Thread.sleep(THREAD_DELAY);
        //Then
        assertEquals(BAD_REQUEST_STATUS_CODE, response.getStatusCode().value());
        assertEquals(jsonResponse, response.getBody());
    }
}
