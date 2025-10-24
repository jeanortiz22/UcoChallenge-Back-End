package co.edu.uco.ucochallenge.infrastructure.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class GatewayOnlyFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayOnlyFilter.class);

    private final String gatewayHeader;
    private final String gatewaySecret;
    private final ObjectMapper mapper = new ObjectMapper();

    public GatewayOnlyFilter(final String gatewayHeader, final String gatewaySecret) {
        this.gatewayHeader = gatewayHeader;
        this.gatewaySecret = gatewaySecret;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {

        if (!requiresGatewayProtection(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String headerValue = request.getHeader(gatewayHeader);

        if (StringUtils.hasText(gatewaySecret) && gatewaySecret.equals(headerValue)) {
            filterChain.doFilter(request, response);
            return;
        }

        logDeniedRequest(request, headerValue);
        writeJsonForbiddenResponse(response);
    }

    private boolean requiresGatewayProtection(final HttpServletRequest httpRequest) {
        final String path = httpRequest.getRequestURI();
        return !path.startsWith("/actuator");
    }

    private void logDeniedRequest(final HttpServletRequest request, final String headerValue) {
        LOGGER.warn("""
                🚫 GATEWAY BLOCKED REQUEST
                ├── Path: {}
                ├── Method: {}
                ├── From IP: {}
                └── Header Received: {}
                """,
                request.getRequestURI(),
                request.getMethod(),
                request.getRemoteAddr(),
                headerValue
        );
    }

    private void writeJsonForbiddenResponse(final HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json; charset=UTF-8");

        final Map<String, Object> errorBody = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 403,
                "error", "Forbidden",
                "message", "Acceso directo no permitido. Debe consumir la API a través del Gateway.",
                "path", "/"
        );

        response.getWriter().write(mapper.writeValueAsString(errorBody));
        response.getWriter().flush();
    }
}
