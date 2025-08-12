package io.camunda.organizer.trip_organization.service;

import io.camunda.organizer.trip_organization.model.database.User;
import io.camunda.organizer.trip_organization.model.dtos.UserDto;
import io.camunda.organizer.trip_organization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Boolean saveNewUser(UserDto user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            User newUser = new User();
            newUser.setId(user.getId());
            newUser.setRole(user.getRole());
            newUser.setUsername(user.getUsername());
            userRepository.save(newUser);
            return true;
        }
        return false;
    }
}
