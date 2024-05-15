package br.dubidubibank.authentication.handlers;

import br.dubidubibank.records.AuthenticationSuccessRecord;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpCookie;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authentication)
      throws IOException, ServletException {
    if (authentication.getDetails() instanceof WebAuthenticationDetails details) {
      writeSuccess(details, response);
    } else {
      AuthenticationSuccessHandler.super.onAuthenticationSuccess(
          request, response, chain, authentication);
    }
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    if (authentication.getDetails() instanceof WebAuthenticationDetails details) {
      writeSuccess(details, response);
    }
  }

  private static void writeSuccess(WebAuthenticationDetails details, HttpServletResponse response)
      throws IOException {
    response.setCharacterEncoding("UTF-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    PrintWriter writer = response.getWriter();
    String sessionId = getSessionId(details, response);
    AuthenticationSuccessRecord success =
        new AuthenticationSuccessRecord("You are logged in.", sessionId);
    String successJson = new Gson().toJson(success);
    writer.print(successJson);
    writer.flush();
  }

  private static String getSessionId(
      WebAuthenticationDetails details, HttpServletResponse response) {
    String sessionId = details.getSessionId();
    if (sessionId == null) {
      String cookieHeader = response.getHeader("Set-Cookie");
      if (cookieHeader != null) {
        List<HttpCookie> cookies = HttpCookie.parse(cookieHeader);
        if (!cookies.isEmpty()) {
          sessionId = cookies.getFirst().getValue();
        }
      }
    }
    return sessionId;
  }
}
