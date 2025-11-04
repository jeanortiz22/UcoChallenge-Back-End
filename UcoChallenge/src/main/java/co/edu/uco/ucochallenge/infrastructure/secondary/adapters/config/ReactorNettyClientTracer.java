package co.edu.uco.ucochallenge.infrastructure.secondary.adapters.config;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

/**
 * Consumer que inyecta un filtro de trazado para peticiones realizadas mediante {@link WebClient}.
 * <p>
 * Permite que el cliente reactive genere spans de tipo CLIENT reutilizando la configuración global
 * de OpenTelemetry y propagando la información hacia el exportador configurado (por ejemplo Datadog).
 */
public class ReactorNettyClientTracer implements Consumer<WebClient.Builder> {

    private final Tracer tracer;

    public ReactorNettyClientTracer() {
        this.tracer = GlobalOpenTelemetry.getTracer("co.edu.uco.ucochallenge.webclient");
    }

    @Override
    public void accept(WebClient.Builder builder) {
        builder.filter(this::traceExchange);
    }

    private Mono<ClientResponse> traceExchange(ClientRequest request, ExchangeFunction next) {
        URI uri = request.url();
        Span span = tracer.spanBuilder(request.method().name() + " " + uri.getPath())
                .setSpanKind(SpanKind.CLIENT)
                .startSpan();

        span.setAttribute("http.request.method", request.method().name());
        span.setAttribute("url.full", uri.toString());
        if (uri.getHost() != null) {
            span.setAttribute("server.address", uri.getHost());
        }
        if (uri.getPort() != -1) {
            span.setAttribute("server.port", uri.getPort());
        }

        try (Scope scope = span.makeCurrent()) {
            return next.exchange(request)
                    .doOnNext(response -> {
                        span.setAttribute("http.response.status_code", response.statusCode().value());
                        if (response.statusCode().isError()) {
                            span.setStatus(StatusCode.ERROR);
                        }
                    })
                    .doOnError(error -> {
                        span.recordException(error);
                        span.setStatus(StatusCode.ERROR);
                    })
                    .doFinally(signal -> span.end());
        } catch (RuntimeException ex) {
            span.recordException(ex);
            span.setStatus(StatusCode.ERROR);
            span.end();
            throw ex;
        }
    }
}