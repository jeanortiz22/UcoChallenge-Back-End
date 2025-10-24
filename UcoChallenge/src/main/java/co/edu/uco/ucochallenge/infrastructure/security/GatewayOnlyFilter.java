package co.edu.uco.ucochallenge.infrastructure.security;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class GatewayOnlyFilter implements Filter {

    private final String gatewayHeader;
    private final String gatewaySecret;

    public GatewayOnlyFilter(
        @Value("${gateway.security.header:X-Gateway-Request}") final String gatewayHeader,
        @Value("${gateway.security.secret:}") final String gatewaySecret) {
        this.gatewayHeader = gatewayHeader;
        this.gatewaySecret = gatewaySecret;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws IOException, ServletException {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!requiresGatewayProtection(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        if (StringUtils.hasText(gatewaySecret) && gatewaySecret.equals(httpRequest.getHeader(gatewayHeader))) {
            chain.doFilter(request, response);
            return;
        }

        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write("{\"error\":\"Direct access not allowed. Use API Gateway.\"}");
        httpResponse.getWriter().flush();
        return;
    }

    private boolean requiresGatewayProtection(final HttpServletRequest httpRequest) {
        final String path = httpRequest.getRequestURI();
        return !path.startsWith("/actuator");
    }
}
