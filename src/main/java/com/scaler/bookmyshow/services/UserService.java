package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.exceptions.InvalidPasswordException;
import com.scaler.bookmyshow.exceptions.InvalidUserException;
import com.scaler.bookmyshow.models.User;
import com.scaler.bookmyshow.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Getter
@Setter
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    public User login(String email, String password) throws InvalidUserException, InvalidPasswordException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                throw new InvalidPasswordException("Invalid password");
            }
        } else {
            return signup(email, password);
        }
    }
    public User signup(String email, String password) throws InvalidUserException, InvalidPasswordException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            // User already exists, call the login method
            return login(email, password);
        } else {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            return userRepository.save(user);
        }
    }
}
