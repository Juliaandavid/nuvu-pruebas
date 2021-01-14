package cc.nuvu.pruebas.controllers;

import cc.nuvu.pruebas.entities.User;
import cc.nuvu.pruebas.repositories.UserRepository;
import cc.nuvu.pruebas.security.authentication.UserAuthentication;
import cc.nuvu.pruebas.dto.AuthRequest;
import cc.nuvu.pruebas.dto.AuthResponse;
import cc.nuvu.pruebas.services.jwt.JWTProvider;
import cc.nuvu.pruebas.dto.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;

@RestController()
@RequestMapping("/auth")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;

  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;

  private final JWTProvider jwtProvider;

  public AuthenticationController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRepository userRepository, JWTProvider jwtProvider) {
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser (@Valid @RequestBody RegisterRequest payload, HttpServletResponse response) throws IOException {
    if (userRepository.existsByDocumentId(payload.getDocumentId())) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid email or password");
      return null;
    }

    User user = new User();
    user.setDocumentId(payload.getDocumentId());
    user.setFirstName(payload.getFirstName());
    user.setLastName(payload.getLastName());
    user.setPassword(passwordEncoder.encode(payload.getPassword()));
    userRepository.save(user);
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticate (@Valid @RequestBody AuthRequest request, HttpServletResponse response) throws IOException {
    try {
      Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserAuthentication userAuthentication = (UserAuthentication) authentication.getPrincipal();
      String token = jwtProvider.generateJWTToken(userAuthentication.getUsername(), new ArrayList<>(userAuthentication.getAuthorities()));
      return ResponseEntity.ok(new AuthResponse(token));
    } catch (BadCredentialsException exception) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid email or password");
    } catch (StackOverflowError | Exception e) {
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error");
    }
    return null;
  }

}
