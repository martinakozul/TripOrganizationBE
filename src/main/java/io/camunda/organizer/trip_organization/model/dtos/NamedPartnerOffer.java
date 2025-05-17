package io.camunda.organizer.trip_organization.model.dtos;

public class NamedPartnerOffer {
    private Long partnerId;
    private Long processKey;
    private String partnerName;
    private int pricePerPerson;

    public NamedPartnerOffer(Long partnerId, Long processKey, String partnerName, int pricePerPerson) {
        this.partnerId = partnerId;
        this.processKey = processKey;
        this.partnerName = partnerName;
        this.pricePerPerson = pricePerPerson;
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
}
