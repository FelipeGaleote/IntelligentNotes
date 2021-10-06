package com.felipeg.intelligentnotes.users.services;

import com.felipeg.intelligentnotes.error_handling.exceptions.UsernameAlreadyInUseException;
import com.felipeg.intelligentnotes.users.models.User;
import com.felipeg.intelligentnotes.users.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticateUser(String username, String password) throws BadCredentialsException {
        var authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return (User) authenticate.getPrincipal();
    }

    public User signUp(String username, String email, String password) throws UsernameAlreadyInUseException {
        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        if (isUsernameAlreadyInUse(username)) {
            throw new UsernameAlreadyInUseException();
        } else {
            user = userRepository.save(user);
            return user;
        }
    }

    private boolean isUsernameAlreadyInUse(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent();
    }

}
