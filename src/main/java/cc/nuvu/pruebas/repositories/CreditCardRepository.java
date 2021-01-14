package cc.nuvu.pruebas.repositories;

import cc.nuvu.pruebas.entities.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

}
