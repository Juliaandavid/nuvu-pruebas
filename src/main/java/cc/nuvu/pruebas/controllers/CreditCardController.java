package cc.nuvu.pruebas.controllers;

import cc.nuvu.pruebas.entities.CreditCard;
import cc.nuvu.pruebas.entities.User;
import cc.nuvu.pruebas.exceptions.UserNotFoundException;
import cc.nuvu.pruebas.repositories.CreditCardRepository;
import cc.nuvu.pruebas.repositories.UserRepository;
import cc.nuvu.pruebas.services.jwt.JWTProvider;
import cc.nuvu.pruebas.dto.CreditCardBody;
import cc.nuvu.pruebas.utils.security.AESEncryptionDecryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController()
@RequestMapping("/api/user/{id}")
public class CreditCardController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CreditCardRepository creditCardRepository;

  @Autowired
  private JWTProvider jwtProvider;

  private User getUserFromAuthentication () {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long id = Long.parseLong(authentication.getPrincipal().toString());
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
  }

  @GetMapping("/credit-card")
  public ResponseEntity<?> getCreditCard () {
    User user = getUserFromAuthentication();
    return ResponseEntity.ok(user.getCreditCard());
  }

  @PostMapping("/credit-card")
  public ResponseEntity<?> addCreditCard (@Valid @RequestBody CreditCardBody body) {
    User user = getUserFromAuthentication();

    if (user.getCreditCard() != null) {
      return new ResponseEntity<>("User already has a card", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    AESEncryptionDecryption aes = new AESEncryptionDecryption(jwtProvider.secretKey);
    CreditCard card = CreditCard.fromBody(aes.encrypt(body.getNumber()), body, user);
    creditCardRepository.save(card);
    return new ResponseEntity<>(card, HttpStatus.CREATED);
  }

  @PutMapping("/credit-card")
  public ResponseEntity<?> updateCreditCard (@Valid @RequestBody CreditCardBody body, HttpServletResponse response) throws IOException {
    User user = getUserFromAuthentication();

    if (user.getCreditCard() == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "User does not have a credit card");
      return null;
    }

    AESEncryptionDecryption aes = new AESEncryptionDecryption(jwtProvider.secretKey);
    CreditCard card = CreditCard.fromBody(aes.encrypt(body.getNumber()), body, user);
    creditCardRepository.save(card);
    return ResponseEntity.ok(card);
  }

  @DeleteMapping("/credit-card")
  public ResponseEntity<?> deleteCreditCard (HttpServletResponse response) throws IOException {
    User user = getUserFromAuthentication();

    if (user.getCreditCard() == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "User does not have a credit card");
      return null;
    }

    creditCardRepository.deleteById(user.getDocumentId());
    return ResponseEntity.ok(user.getCreditCard());
  }

}
