package com.pressing.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

  @Override
  public void commence(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AuthenticationException authException)
      throws IOException {

    // Authentication failed, send error response.
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");

    PrintWriter writer = response.getWriter();
    writer.println("HTTP Status 401 : " + authException.getMessage());
  }

  @Override
  public void afterPropertiesSet() {
    setRealmName("Spring Security Application");
    super.afterPropertiesSet();
  }
}
