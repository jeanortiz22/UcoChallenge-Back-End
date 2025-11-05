package co.edu.uco.parametersservice.infrastructure.observability;

import jakarta.annotation.PostConstruct;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration
public class OpenTelemetryExporterConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenTelemetryExporterConfiguration.class);

    private static final List<String> OTEL_PROPERTIES = List.of(
            "otel.java.global-autoconfigure.enabled",
            "otel.sdk.disabled",
            "otel.exporter.otlp.endpoint",
            "otel.exporter.otlp.protocol",
            "otel.exporter.otlp.headers",
            "otel.exporter.otlp.compression",
            "otel.exporter.otlp.timeout",
            "otel.metrics.exporter",
            "otel.traces.exporter",
            "otel.logs.exporter",
            "otel.resource.attributes",
            "otel.propagators"
    );

    private final Environment environment;

    public OpenTelemetryExporterConfiguration(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    void propagateOpenTelemetryProperties() {
        OTEL_PROPERTIES.forEach(this::propagatePropertyIfAbsent);
    }

    private void propagatePropertyIfAbsent(String propertyName) {
        String configuredValue = environment.getProperty(propertyName);
        String currentValue = System.getProperty(propertyName);
        if (!StringUtils.hasText(currentValue) && StringUtils.hasText(configuredValue)) {
            System.setProperty(propertyName, configuredValue);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Configured OpenTelemetry property '{}' from Spring environment", propertyName);
            }
        }
    }
}