package io.camunda.organizer.trip_organization.model.database;

import jakarta.persistence.*;

@Entity
@Table(name = "partner_offers")
public class PartnerOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_information_id", nullable = false)
    private TripInformation tripInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    private boolean isAccepted;

    private int pricePerPerson;

    public PartnerOffer() {
    }

    public PartnerOffer(long id, Partner partner, TripInformation tripInformation, City city, boolean isAccepted, int pricePerPerson) {
        this.id = id;
        this.partner = partner;
        this.tripInformation = tripInformation;
        this.city = city;
        this.isAccepted = isAccepted;
        this.pricePerPerson = pricePerPerson;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public TripInformation getTripInformation() {
        return tripInformation;
    }

    public void setTripInformation(TripInformation tripInformation) {
        this.tripInformation = tripInformation;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public int getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(int pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }
}
