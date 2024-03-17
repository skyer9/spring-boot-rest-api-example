package com.example.api.common;

import jakarta.servlet.http.HttpServletRequest;

public class Helper {
    public static String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            return request.getRemoteAddr();
        }
        return ipAddress.contains(",") ? ipAddress.split(",")[0] : ipAddress;
    }
}
