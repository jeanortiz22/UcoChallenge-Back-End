package co.edu.uco.ucochallenge.infrastructure.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private static final Logger log = LoggerFactory.getLogger(WebClientConfig.class);

    /**
     * 🧱 Método helper que construye un HttpClient reutilizable con timeouts
     * para todas las llamadas WebClient (mensajes, parámetros, etc).
     */
    private HttpClient buildHttpClient() {
        return HttpClient.create()
                // Tiempo máximo de respuesta
                .responseTimeout(Duration.ofSeconds(3))
                // Tiempo máximo de conexión
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                // Timeout de lectura y escritura
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(3, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(3, TimeUnit.SECONDS))
                );
    }

    /**
     * 📨 WebClient para conectarse al Catálogo de Mensajes
     */
    @Bean(name = "messagesWebClient")
    public WebClient messagesWebClient(
            @Value("${messages.catalog.base-url}") String baseUrl,
            @Value("${gateway.security.header}") String gatewayHeaderName,
            @Value("${gateway.security.secret}") String gatewayHeaderValue,
            WebClient.Builder builder
    ) {
        log.info("### MessageCatalog baseUrl = {}", baseUrl);

        return builder
                .baseUrl(baseUrl)
                .defaultHeader(gatewayHeaderName, gatewayHeaderValue)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(c -> c.defaultCodecs().maxInMemorySize(512 * 1024))
                        .build())
                .clientConnector(new ReactorClientHttpConnector(buildHttpClient()))
                // Log de salida
                .filter((request, next) -> {
                    String headerValue = request.headers().getFirst(gatewayHeaderName);
                    log.debug(">>> OUT to {} sending {}={}", request.url(), gatewayHeaderName, headerValue);
                    return next.exchange(request);
                })
                .build();
    }

    /**
     * ⚙️ WebClient para conectarse al Catálogo de Parámetros
     */
    @Bean
    @Qualifier("parametersWebClient")
    public WebClient parametersWebClient(
            WebClient.Builder builder,
            @Value("${parameters.catalog.base-url}") String baseUrl,
            @Value("${gateway.security.header}") String gatewayHeaderName,
            @Value("${gateway.security.secret}") String gatewayHeaderValue
    ) {
        log.info("### ParameterCatalog baseUrl = {}", baseUrl);

        return builder
                .baseUrl(baseUrl)
                .defaultHeader(gatewayHeaderName, gatewayHeaderValue)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(c -> c.defaultCodecs().maxInMemorySize(512 * 1024))
                        .build())
                .clientConnector(new ReactorClientHttpConnector(buildHttpClient()))
                .filter((request, next) -> {
                    String headerValue = request.headers().getFirst(gatewayHeaderName);
                    log.debug(">>> OUT to {} sending {}={}", request.url(), gatewayHeaderName, headerValue);
                    return next.exchange(request);
                })
                .build();
    }
}
