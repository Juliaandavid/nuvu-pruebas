package cc.nuvu.pruebas.filters;

import cc.nuvu.pruebas.services.jwt.JWTProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTProvider jwt;

    public JwtAuthenticationFilter(JWTProvider jwt) {
        this.jwt = jwt;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !new AntPathMatcher().match("/api/**", request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String token = getTokenFromHeader(request);

            if (token != null) {
                Claims claims = jwt.getClaimsFromToken(token);

                if (claims.get("authorities") != null) {
                    @SuppressWarnings("unchecked")
                    List<String> authorities = (List<String>) claims.get("authorities");

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                        authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
        } catch (IllegalArgumentException | MalformedJwtException | UnsupportedJwtException | SignatureException | ExpiredJwtException exception) {
            Logger.getLogger(this.getClass().getName()).warning(exception.getMessage());
        }

        chain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(jwt.header);
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.replace("Bearer ","") : null;
    }
}
