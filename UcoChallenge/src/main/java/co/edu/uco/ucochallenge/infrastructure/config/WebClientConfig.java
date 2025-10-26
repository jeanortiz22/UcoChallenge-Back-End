// infrastructure/config/WebClientConfig.java
package co.edu.uco.ucochallenge.infrastructure.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
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

    @Bean(name = "messagesWebClient")
    public WebClient messagesWebClient(
            @Value("${messages.catalog.base-url}") String baseUrl,
            @Value("${gateway.security.header}") String gatewayHeaderName,
            @Value("${gateway.security.secret}") String gatewayHeaderValue,
            WebClient.Builder builder
    ) {
        System.out.println("### MessageCatalog baseUrl = " + baseUrl);

        // Netty timeouts razonables (ajusta si quieres)
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(3))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(3, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(3, TimeUnit.SECONDS)));

        return builder
                .baseUrl(baseUrl)
                // Header compartido para llamadas internas (¡clave!)
                .defaultHeader(gatewayHeaderName, gatewayHeaderValue)
                // (opcional) aumentar buffer si las respuestas fueran grandes
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(c -> c.defaultCodecs().maxInMemorySize(512 * 1024))
                        .build())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
