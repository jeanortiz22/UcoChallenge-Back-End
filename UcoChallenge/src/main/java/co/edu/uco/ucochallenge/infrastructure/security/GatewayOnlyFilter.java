package co.edu.uco.ucochallenge.infrastructure.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class GatewayOnlyFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayOnlyFilter.class);

    private final String gatewayHeader;
    private final String gatewaySecret;

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

        if (StringUtils.hasText(gatewaySecret) && gatewaySecret.equals(request.getHeader(gatewayHeader))) {
            filterChain.doFilter(request, response);
            return;
        }

        LOGGER.warn("Blocking direct access to protected endpoint: {}", request.getRequestURI());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso directo no permitido. Utilice el gateway.");
    }

    private boolean requiresGatewayProtection(final HttpServletRequest httpRequest) {
        final String path = httpRequest.getRequestURI();
        return !path.startsWith("/actuator");
    }
}
