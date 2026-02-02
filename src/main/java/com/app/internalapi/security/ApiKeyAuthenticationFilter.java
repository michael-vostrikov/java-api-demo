package com.app.internalapi;

import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final String apiKey;
    private final String apiKeyHeader;

    public ApiKeyAuthenticationFilter(String apiKey, String apiKeyHeader) {
        this.apiKey = apiKey;
        this.apiKeyHeader = apiKeyHeader;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {

        String apiKeyFromRequest = request.getHeader(apiKeyHeader);

        if (apiKey.equals(apiKeyFromRequest)) {
            var auth = new UsernamePasswordAuthenticationToken("api-client", null,
                List.of(new SimpleGrantedAuthority("ROLE_API"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
