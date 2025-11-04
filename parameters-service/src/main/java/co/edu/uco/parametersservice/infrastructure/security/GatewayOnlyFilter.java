package co.edu.uco.parametersservice.infrastructure.security;

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

        // 1) Preflight CORS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2) No-op si no hay config (útil en local)
        if (!StringUtils.hasText(gatewayHeader) || !StringUtils.hasText(gatewaySecret)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3) Deja públicos actuator/swagger
        if (!requiresGatewayProtection(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4) Valida header
        final String headerValue = request.getHeader(gatewayHeader);
        if (gatewaySecret.equals(headerValue)) {
            filterChain.doFilter(request, response);
            return;
        }

        logDeniedRequest(request, headerValue);
        writeJsonDeniedResponse(response, request.getRequestURI());
    }

    private boolean requiresGatewayProtection(final HttpServletRequest httpRequest) {
        final String path = httpRequest.getRequestURI();
        return !(path.startsWith("/actuator")
              || path.startsWith("/v3/api-docs")
              || path.startsWith("/swagger-ui"));
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

    private void writeJsonDeniedResponse(final HttpServletResponse response, final String path) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        final Map<String, Object> errorBody = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 403,
                "error", "Forbidden",
                "message", "Acceso directo no permitido. Consume la API a través del Gateway.",
                "path", path
        );

        response.getWriter().write(mapper.writeValueAsString(errorBody));
        response.getWriter().flush();
    }
}
