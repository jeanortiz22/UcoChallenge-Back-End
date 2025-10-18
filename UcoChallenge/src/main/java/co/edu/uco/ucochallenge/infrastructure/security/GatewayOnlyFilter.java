package co.edu.uco.ucochallenge.infrastructure.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class GatewayOnlyFilter implements Filter {

    private static final String GATEWAY_HEADER = "X-Gateway-Request";
    private static final String GATEWAY_SECRET = "AdminUco2024";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String gatewayHeader = httpRequest.getHeader(GATEWAY_HEADER);
        
        if (gatewayHeader == null || !GATEWAY_SECRET.equals(gatewayHeader)) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\":\"Direct access not allowed. Use API Gateway.\"}");
            return;
        }
        
        chain.doFilter(request, response);
    }
}