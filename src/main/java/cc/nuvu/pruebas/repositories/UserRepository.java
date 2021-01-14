package cc.nuvu.pruebas.repositories;

import cc.nuvu.pruebas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByDocumentId(Long documentId);

}
