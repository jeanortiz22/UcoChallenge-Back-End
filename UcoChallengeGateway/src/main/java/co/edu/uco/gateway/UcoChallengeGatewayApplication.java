package co.edu.uco.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UcoChallengeGatewayApplication {

	static {
        if (System.getProperty("otel.java.global-autoconfigure.enabled") == null) {
            System.setProperty("otel.java.global-autoconfigure.enabled", "true");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(UcoChallengeGatewayApplication.class, args);
    }

}
