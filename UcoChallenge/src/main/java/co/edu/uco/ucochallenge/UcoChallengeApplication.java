package co.edu.uco.ucochallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UcoChallengeApplication {

	static {
        if (System.getProperty("otel.java.global-autoconfigure.enabled") == null) {
            System.setProperty("otel.java.global-autoconfigure.enabled", "true");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(UcoChallengeApplication.class, args);
    }

}
