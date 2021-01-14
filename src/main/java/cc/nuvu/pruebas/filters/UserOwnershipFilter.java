package cc.nuvu.pruebas.filters;

import cc.nuvu.pruebas.security.authentication.UserAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserOwnershipFilter extends OncePerRequestFilter {

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return !new AntPathMatcher().match("/api/user/**", request.getServletPath());
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    List<String> parts = Arrays.asList(request.getRequestURI().split("/"));

    if (authentication == null || !authentication.getPrincipal().toString().equals(parts.get(3))) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "Permision denied");
    } else {
      chain.doFilter(request, response);
    }
  }

}
