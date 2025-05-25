package io.camunda.organizer.trip_organization.model.dtos;

public class NamedPartnerOffer {
    private Long id;
    private Long partnerId;
    private Long processKey;
    private String partnerName;
    private int pricePerPerson;
    private Long cityId;
    private String cityName;


    public NamedPartnerOffer(Long id, Long partnerId, Long processKey, String partnerName, int pricePerPerson, Long cityId, String cityName) {
        this.id = id;
        this.partnerId = partnerId;
        this.processKey = processKey;
        this.partnerName = partnerName;
        this.pricePerPerson = pricePerPerson;
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public Long getId() {
        return id;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public Long getProcessKey() {
        return processKey;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public int getPricePerPerson() {
        return pricePerPerson;
    }

    public Long getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }
}
