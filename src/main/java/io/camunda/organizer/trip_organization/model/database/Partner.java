package io.camunda.organizer.trip_organization.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.camunda.organizer.trip_organization.model.OfferType;
import io.camunda.organizer.trip_organization.model.TransportationType;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private OfferType offerType;

    @Enumerated(EnumType.STRING)
    private TransportationType transportationType;


    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "partner_city",
            joinColumns = @JoinColumn(name = "partner_id"),
            inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private Set<City> cities;

    public Partner() {}

    public Partner(Long id, String name, String email, OfferType offerType, TransportationType transportationType, Set<City> cities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.offerType = offerType;
        this.transportationType = transportationType;
        this.cities = cities;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public OfferType getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }

    public TransportationType getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(TransportationType transportationType) {
        this.transportationType = transportationType;
    }

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }
}

