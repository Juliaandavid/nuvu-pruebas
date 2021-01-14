package cc.nuvu.pruebas.controllers;

import cc.nuvu.pruebas.dto.UserUpdateBody;
import cc.nuvu.pruebas.entities.User;
import cc.nuvu.pruebas.exceptions.UserNotFoundException;
import cc.nuvu.pruebas.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user/{id}")
    public User getUser (@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UserUpdateBody body) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (body.getFirstName() != null)
            user.setFirstName(body.getFirstName());
        if (body.getLastName() != null)
            user.setLastName(body.getLastName());
        if (body.getPassword() != null)
            user.setPassword(passwordEncoder.encode(body.getPassword()));

        return repository.save(user);
    }

}
