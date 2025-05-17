package io.camunda.organizer.trip_organization.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "cities")
    @JsonIgnore
    private Set<Partner> partners;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TripCity> trips = new ArrayList<>();

    public City() {
    }

    public City(Long id, String name, Set<Partner> partners) {
        this.id = id;
        this.name = name;
        this.partners = partners;
    }

    public City(Long id, String name, Set<Partner> partners, List<TripCity> trips) {
        this.id = id;
        this.name = name;
        this.partners = partners;
        this.trips = trips;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Partner> getPartners() {
        return partners;
    }

    public void setPartners(Set<Partner> partners) {
        this.partners = partners;
    }

    public List<TripCity> getTrips() {
        return trips;
    }

    public void setTrips(List<TripCity> trips) {
        this.trips = trips;
    }
}
