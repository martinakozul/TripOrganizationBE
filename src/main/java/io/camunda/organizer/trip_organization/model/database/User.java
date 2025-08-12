package io.camunda.organizer.trip_organization.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.camunda.organizer.trip_organization.model.Role;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "coordinator", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TripInformation> coordinatedTrips = new HashSet<>();

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TripInformation> guidedTrips = new HashSet<>();

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

    public Set<TripInformation> getGuidedTrips() {
        return guidedTrips;
    }

    public void setGuidedTrips(Set<TripInformation> guidedTrips) {
        this.guidedTrips = guidedTrips;
    }

    public Set<TripInformation> getCoordinatedTrips() {
        return coordinatedTrips;
    }

    public void setCoordinatedTrips(Set<TripInformation> coordinatedTrips) {
        this.coordinatedTrips = coordinatedTrips;
    }
}


