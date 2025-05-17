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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "coordinator", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TripInformation> coordinatedTrips = new HashSet<>();

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TripInformation> guidedTrips = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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


