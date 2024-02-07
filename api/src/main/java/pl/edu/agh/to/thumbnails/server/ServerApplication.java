package pl.edu.agh.to.thumbnails.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.edu.agh.to.thumbnails.server.utils.LocalDateTimeTypeAdapter;

import java.time.LocalDateTime;

@SpringBootApplication(scanBasePackages = "pl.edu.agh.to.thumbnails.server")
@EnableScheduling
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	public Gson gson() {
		return new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
				.create();
	}
}
