package cc.nuvu.pruebas.services.auth;

import cc.nuvu.pruebas.entities.User;
import cc.nuvu.pruebas.exceptions.UserNotFoundException;
import cc.nuvu.pruebas.repositories.UserRepository;
import cc.nuvu.pruebas.security.authentication.UserAuthentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String documentId) throws UsernameNotFoundException {
        Long id = Long.parseLong(documentId);
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        return new UserAuthentication(user.getDocumentId(), user.getPassword(), user.getRole());
    }

}
