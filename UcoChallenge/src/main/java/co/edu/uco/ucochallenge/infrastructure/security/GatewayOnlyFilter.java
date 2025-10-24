package co.edu.uco.ucochallenge.infrastructure.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class GatewayOnlyFilter extends OncePerRequestFilter {

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

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Direct access not allowed. Use API Gateway.\"}");
        response.getWriter().flush();
    }

    private boolean requiresGatewayProtection(final HttpServletRequest httpRequest) {
        final String path = httpRequest.getRequestURI();
        return !path.startsWith("/actuator");
    }
}
