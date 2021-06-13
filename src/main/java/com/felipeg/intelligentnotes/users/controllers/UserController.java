package com.felipeg.intelligentnotes.users.controllers;

import com.felipeg.intelligentnotes.users.dtos.input.SignUpInput;
import com.felipeg.intelligentnotes.users.dtos.output.SignUpOutput;
import com.felipeg.intelligentnotes.users.models.User;
import com.felipeg.intelligentnotes.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;


@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("user")
    public ResponseEntity<SignUpOutput> signUp(@RequestBody @Valid SignUpInput signUpInput) {
        var user = new User();
        user.setUsername(signUpInput.getUsername());
        user.setEmail(signUpInput.getEmail());
        user.setPassword(passwordEncoder.encode(signUpInput.getPassword()));

        if (isUsernameAlreadyInUse(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already in use.");
        } else {
            user = userRepository.save(user);
            var signUpOutput = new SignUpOutput(user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(signUpOutput);
        }
    }

    private boolean isUsernameAlreadyInUse(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent();
    }
}
