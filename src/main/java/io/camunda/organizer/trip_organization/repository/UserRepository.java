package io.camunda.organizer.trip_organization.repository;

import io.camunda.organizer.trip_organization.model.Role;
import io.camunda.organizer.trip_organization.model.database.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    List<User> findByRole(Role role);
}
