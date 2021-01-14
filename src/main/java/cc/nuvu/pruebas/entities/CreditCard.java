package cc.nuvu.pruebas.entities;

import cc.nuvu.pruebas.dto.CreditCardBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Entity
public class CreditCard implements Serializable {

  @Id
  @Column(name = "user_id")
  @Setter
  private Long userId;

  @OneToOne
  @JoinColumn(name = "user_id")
  @Setter
  private User user;

  @Setter
  private String token;

  @Getter @Setter
  private String lastFour;

  @Getter @Setter
  private String monthExpiration;

  @Getter @Setter
  private String yearExpiration;

  @Setter
  private int cvc;

  public static CreditCard fromBody(String token, CreditCardBody body, User user) {
    CreditCard creditCard = new CreditCard();
    creditCard.setUserId(user.getDocumentId());
    creditCard.setToken(token);
    creditCard.setLastFour(body.getNumber().substring(body.getNumber().length() - 4));
    creditCard.setMonthExpiration(body.getMonthExpiration());
    creditCard.setYearExpiration(body.getYearExpiration());
    creditCard.setCvc(body.getCvc());

    return creditCard;
  }

  @Override
  public int hashCode() {
    return Objects.hash(user.getDocumentId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    return ((CreditCard) o).user.equals(user);
  }

}
