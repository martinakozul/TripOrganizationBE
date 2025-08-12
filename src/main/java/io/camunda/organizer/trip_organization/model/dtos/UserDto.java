package io.camunda.organizer.trip_organization.model.dtos;

import io.camunda.organizer.trip_organization.model.Role;

public class UserDto {

    private String id;
    private String username;
    private Role role;

    public UserDto(String id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

