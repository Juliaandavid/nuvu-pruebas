package cc.nuvu.pruebas.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateBody {

  private String firstName;

  private String lastName;

  private String password;

}
