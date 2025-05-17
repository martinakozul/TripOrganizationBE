package io.camunda.organizer.trip_organization.model;

public enum OfferType {
    TRANSPORT("transportOfferReceived"),
    ACCOMMODATION("accommodationOfferReceived");

    private final String messageName;

    OfferType(String messageName) {
        this.messageName = messageName;
    }

    public String getMessageName() {
        return messageName;
    }
}

