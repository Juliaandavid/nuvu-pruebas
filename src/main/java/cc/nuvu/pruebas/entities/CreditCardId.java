package cc.nuvu.pruebas.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class CreditCardId implements Serializable {

  private Long userId;
  private Long documentId;

  @Override
  public int hashCode() {
    return Objects.hash(userId, documentId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    CreditCardId composite = (CreditCardId) o;
    if (!documentId.equals(composite.documentId))
      return false;
    return userId.equals(composite.userId);
  }
}
