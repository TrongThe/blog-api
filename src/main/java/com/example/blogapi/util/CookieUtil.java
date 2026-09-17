package com.example.blogapi.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private static final String REFRESH_TOKEN_COOKIE = "refreshToken";

    public void addRefreshTokenCookie(
            HttpServletResponse response,
            String refreshToken
    ){
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, refreshToken);

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/auth");
        cookie.setMaxAge(7 * 24 * 60 *60);

        response.addCookie(cookie);
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response){
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, "");

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/auth");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    public String getRefreshToken(HttpServletRequest request){
        if (request.getCookies() == null){
            return null;
        }

        for (Cookie cookie : request.getCookies()){
            if (REFRESH_TOKEN_COOKIE.equals(cookie.getName())){
                return cookie.getValue();
            }
        }

        return null;
    }
}
