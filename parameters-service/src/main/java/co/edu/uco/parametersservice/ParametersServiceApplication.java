package co.edu.uco.parametersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParametersServiceApplication {

	static {
        if (System.getProperty("otel.java.global-autoconfigure.enabled") == null) {
            System.setProperty("otel.java.global-autoconfigure.enabled", "true");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ParametersServiceApplication.class, args);
    }
}
