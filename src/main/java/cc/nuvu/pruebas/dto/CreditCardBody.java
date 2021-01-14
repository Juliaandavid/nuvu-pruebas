package cc.nuvu.pruebas.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreditCardBody {

  @NotNull
  @Pattern(regexp = "^(?:(?<VISA>4[0-9]{12}(?:[0-9]{3})?)|" +
      "(?<MASTERCARD>5[1-5][0-9]{14})|" +
      "(?<DISCOVER>6(?:011|5[0-9]{2})[0-9]{12})|" +
      "(?<AMEX>3[47][0-9]{13})|" +
      "(?<DINERS>3(?:0[0-5]|[68][0-9])?[0-9]{11})|" +
      "(?<JCB>(?:2131|1800|35[0-9]{3})[0-9]{11}))$", message = "Invalid credit card")
  @Getter @Setter
  private String number;

  @NotNull
  @Size(min = 2, max = 2)
  @Getter @Setter
  private String monthExpiration;

  @NotNull
  @Size(min = 2, max = 2)
  @Getter @Setter
  private String yearExpiration;

  @NotNull
  @Getter @Setter
  private int cvc;

}
