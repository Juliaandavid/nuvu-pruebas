package cc.nuvu.pruebas.entities;

import cc.nuvu.pruebas.enumerations.AppRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="app_user")
public class User {

    @Id
    @Setter @Getter
    private Long documentId;

    @Setter @Getter
    private String firstName;

    @Setter @Getter
    private String lastName;

    @Setter
    private String password;

    private String role = AppRole.USER;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @Setter
    private CreditCard creditCard;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public String getRole() {
        return role;
    }

    @JsonIgnore
    public CreditCard getCreditCard() {
        return creditCard;
    }

}
