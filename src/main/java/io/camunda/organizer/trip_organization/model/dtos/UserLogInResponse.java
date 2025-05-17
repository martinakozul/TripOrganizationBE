package io.camunda.organizer.trip_organization.model.dtos;

import io.camunda.organizer.trip_organization.model.Role;

public class UserLogInResponse {

    private Long id;
    private Role role;

    public UserLogInResponse(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

