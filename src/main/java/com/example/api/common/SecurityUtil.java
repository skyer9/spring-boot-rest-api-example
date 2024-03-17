package com.example.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Optional;

public class SecurityUtil {

    private SecurityUtil() {}

    public static Optional<String> getCurrentUsername() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }

    public static void setResponse(HttpServletResponse response, Exception ex) throws RuntimeException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(mapper.writeValueAsString(ResponseDto.res(HttpStatus.BAD_REQUEST, ex.getMessage())));
    }

    public static String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            return request.getRemoteAddr();
        }
        return ipAddress.contains(",") ? ipAddress.split(",")[0] : ipAddress;
    }
}
