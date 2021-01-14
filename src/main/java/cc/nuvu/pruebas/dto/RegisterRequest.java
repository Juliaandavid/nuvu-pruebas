package cc.nuvu.pruebas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class RegisterRequest {

  @NotNull
  private final Long documentId;

  @NotNull
  @NotEmpty
  private final String firstName;

  @NotNull
  @NotEmpty
  private final String lastName;

  @NotNull
  @NotEmpty
  private final String password;

}
