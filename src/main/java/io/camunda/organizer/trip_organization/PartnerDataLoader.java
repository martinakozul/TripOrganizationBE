package io.camunda.organizer.trip_organization;

import io.camunda.organizer.trip_organization.model.OfferType;
import io.camunda.organizer.trip_organization.model.Role;
import io.camunda.organizer.trip_organization.model.TransportationType;
import io.camunda.organizer.trip_organization.model.database.City;
import io.camunda.organizer.trip_organization.model.database.Partner;
import io.camunda.organizer.trip_organization.model.database.User;
import io.camunda.organizer.trip_organization.repository.CityRepository;
import io.camunda.organizer.trip_organization.repository.PartnerRepository;
import io.camunda.organizer.trip_organization.repository.UserRepository;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class PartnerDataLoader {

//    @Bean
//    public CommandLineRunner cleanUpData(UserRepository userRepository, TripInformationRepository tripInformationRepository) {
//        return args -> {
//            // Clean up all data from the User table
//            userRepository.deleteAll();
//            System.out.println("All users have been deleted.");
//
//            // Clean up all data from the TripInformation table
//            tripInformationRepository.deleteAll();
//            System.out.println("All trips have been deleted.");
//        };
//    }

//    @Bean
//    public CommandLineRunner insertSamplePartners(PartnerRepository partnerRepository, CityRepository cityRepository) {
//        // === Cities ===
//        return args -> {
//            City zagreb = new City();
//            zagreb.setName("Zagreb");
//            City ljubljana = new City();
//            ljubljana.setName("Ljubljana");
//            City dubrovnik = new City();
//            dubrovnik.setName("Dubrovnik");
//            City rome = new City();
//            rome.setName("Rome");
//            City pisa = new City();
//            pisa.setName("Pisa");
//            City barcelona = new City();
//            barcelona.setName("Barcelona");
//            City paris = new City();
//            paris.setName("Paris");
//            City milano = new City();
//            milano.setName("Milano");
//            City london = new City();
//            london.setName("London");
//
//            Set<City> allCities = Set.of(zagreb, ljubljana, dubrovnik, rome, pisa, barcelona, paris, milano, london);
//
//// === Shared Accommodation Partners ===
//            Partner hilton = new Partner();
//            hilton.setName("Hilton");
//            hilton.setEmail("contact@hilton.com");
//            hilton.setOfferType(OfferType.ACCOMMODATION);
//            hilton.setCities(Set.of(zagreb, paris, london, milano));
//
//            Partner ibis = new Partner();
//            ibis.setName("Ibis");
//            ibis.setEmail("info@ibis.com");
//            ibis.setOfferType(OfferType.ACCOMMODATION);
//            ibis.setCities(Set.of(rome, barcelona, pisa, zagreb));
//
//            Partner marriott = new Partner();
//            marriott.setName("Marriott");
//            marriott.setEmail("book@marriott.com");
//            marriott.setOfferType(OfferType.ACCOMMODATION);
//            marriott.setCities(Set.of(dubrovnik, ljubljana, london));
//
//// === Bus transport partners ===
//            Partner flixTravel = new Partner();
//            flixTravel.setName("FlixTravel");
//            flixTravel.setEmail("support@flixtravel.com");
//            flixTravel.setOfferType(OfferType.TRANSPORT);
//            flixTravel.setTransportationType(TransportationType.BUS);
//            flixTravel.setCities(Set.of(zagreb, ljubljana, milano));
//
//            Partner euroBus = new Partner();
//            euroBus.setName("EuroBus");
//            euroBus.setEmail("info@eurobus.com");
//            euroBus.setOfferType(OfferType.TRANSPORT);
//            euroBus.setTransportationType(TransportationType.BUS);
//            euroBus.setCities(Set.of(dubrovnik, rome, pisa));
//
//            Partner croExpress = new Partner();
//            croExpress.setName("CroExpress");
//            croExpress.setEmail("help@croexpress.hr");
//            croExpress.setOfferType(OfferType.TRANSPORT);
//            croExpress.setTransportationType(TransportationType.BUS);
//            croExpress.setCities(Set.of(zagreb, paris, barcelona));
//
//
//// === Avio Partners (Zagreb -> all other cities) ===
//            List<Partner> planePartners = new ArrayList<>();
//            for (City dest : allCities) {
//                if (!dest.getName().equals("Zagreb")) {
//                    Partner p = new Partner();
//                    p.setName("ZagAir to " + dest.getName());
//                    p.setEmail("flights@" + dest.getName().toLowerCase() + ".zagair.com");
//                    p.setOfferType(OfferType.TRANSPORT);
//                    p.setTransportationType(TransportationType.PLANE);
//                    p.setCities(Set.of(zagreb, dest));
//                    planePartners.add(p);
//                }
//            }
//
//// === Assign Partners to Cities ===
//            for (City city : allCities) {
//                Set<Partner> cityPartners = new HashSet<>();
//
//                // Add any accommodation partners that include this city
//                for (Partner partner : List.of(hilton, ibis, marriott)) {
//                    if (partner.getCities().contains(city)) {
//                        cityPartners.add(partner);
//                    }
//                }
//
//                // Add any bus partners that include this city
//                for (Partner partner : List.of(flixTravel, euroBus, croExpress)) {
//                    if (partner.getCities().contains(city)) {
//                        cityPartners.add(partner);
//                    }
//                }
//
//                // Add plane partners that include this city
//                for (Partner partner : planePartners) {
//                    if (partner.getCities().contains(city)) {
//                        cityPartners.add(partner);
//                    }
//                }
//
//                city.setPartners(cityPartners);
//            }
//
//// === Save all ===
//            Set<Partner> allPartners = new HashSet<>(List.of(
//                    hilton, ibis, marriott,
//                    flixTravel, euroBus, croExpress
//            ));
//            allPartners.addAll(planePartners);
//
//            cityRepository.saveAll(allCities);
//            partnerRepository.saveAll(allPartners);
//
//            System.out.println("Sample data has been loaded.");
//
//        };
//    }

//    @Bean
//    public CommandLineRunner insertSamplePartners(PartnerRepository partnerRepository, CityRepository cityRepository) {
//        return args -> {
//            City zagreb = new City();
//            zagreb.setName("Zagreb");
//            City ljubljana = new City();
//            ljubljana.setName("Ljubljana");
//            City dubrovnik = new City();
//            dubrovnik.setName("Dubrovnik");
//            City rome = new City();
//            rome.setName("Rome");
//            City pisa = new City();
//            pisa.setName("Pisa");
//            City barcelona = new City();
//            barcelona.setName("Barcelona");
//            City paris = new City();
//            paris.setName("Paris");
//            City milano = new City();
//            milano.setName("Milano");
//            City london = new City();
//            london.setName("London");
//
//// Accommodation partners
//            Partner accLjubljana1 = new Partner();
//            accLjubljana1.setName("Ljubljana Stay");
//            accLjubljana1.setOfferType(OfferType.ACCOMMODATION);
//            Partner accLjubljana2 = new Partner();
//            accLjubljana2.setName("Green Residence");
//            accLjubljana2.setOfferType(OfferType.ACCOMMODATION);
//
//            Partner accDubrovnik1 = new Partner();
//            accDubrovnik1.setName("Dubrovnik Suites");
//            accDubrovnik1.setOfferType(OfferType.ACCOMMODATION);
//            Partner accDubrovnik2 = new Partner();
//            accDubrovnik2.setName("Adriatic Inn");
//            accDubrovnik2.setOfferType(OfferType.ACCOMMODATION);
//
//            Partner accRome1 = new Partner();
//            accRome1.setName("Roman Holiday");
//            accRome1.setOfferType(OfferType.ACCOMMODATION);
//            Partner accRome2 = new Partner();
//            accRome2.setName("Colosseum Stay");
//            accRome2.setOfferType(OfferType.ACCOMMODATION);
//            Partner accRome3 = new Partner();
//            accRome3.setName("Vatican Rooms");
//            accRome3.setOfferType(OfferType.ACCOMMODATION);
//
//            Partner accPisa1 = new Partner();
//            accPisa1.setName("Leaning Lodge");
//            accPisa1.setOfferType(OfferType.ACCOMMODATION);
//            Partner accPisa2 = new Partner();
//            accPisa2.setName("Pisa Plaza Hotel");
//            accPisa2.setOfferType(OfferType.ACCOMMODATION);
//
//            Partner accBarcelona1 = new Partner();
//            accBarcelona1.setName("Barcelona Central");
//            accBarcelona1.setOfferType(OfferType.ACCOMMODATION);
//            Partner accBarcelona2 = new Partner();
//            accBarcelona2.setName("Gaudi Hostel");
//            accBarcelona2.setOfferType(OfferType.ACCOMMODATION);
//            Partner accBarcelona3 = new Partner();
//            accBarcelona3.setName("Beachfront Barcelona");
//            accBarcelona3.setOfferType(OfferType.ACCOMMODATION);
//
//            Partner accParis1 = new Partner();
//            accParis1.setName("Eiffel Stays");
//            accParis1.setOfferType(OfferType.ACCOMMODATION);
//            Partner accParis2 = new Partner();
//            accParis2.setName("Parisian Comfort");
//            accParis2.setOfferType(OfferType.ACCOMMODATION);
//
//            Partner accMilano1 = new Partner();
//            accMilano1.setName("Milano Modern");
//            accMilano1.setOfferType(OfferType.ACCOMMODATION);
//            Partner accMilano2 = new Partner();
//            accMilano2.setName("Fashion Flat");
//            accMilano2.setOfferType(OfferType.ACCOMMODATION);
//
//            Partner accLondon1 = new Partner();
//            accLondon1.setName("London Lodge");
//            accLondon1.setOfferType(OfferType.ACCOMMODATION);
//            Partner accLondon2 = new Partner();
//            accLondon2.setName("Thames View Inn");
//            accLondon2.setOfferType(OfferType.ACCOMMODATION);
//            Partner accLondon3 = new Partner();
//            accLondon3.setName("Royal Stay");
//            accLondon3.setOfferType(OfferType.ACCOMMODATION);
//
//            Partner bus1 = new Partner();
//            bus1.setName("Flixbus");
//            bus1.setOfferType(OfferType.TRANSPORT);
//            bus1.setTransportationType(TransportationType.BUS);
//            Partner bus2 = new Partner();
//            bus2.setName("Cazmatrans");
//            bus2.setOfferType(OfferType.TRANSPORT);
//            bus2.setTransportationType(TransportationType.BUS);
//            Partner bus3 = new Partner();
//            bus3.setName("Arriva");
//            bus3.setOfferType(OfferType.TRANSPORT);
//            bus3.setTransportationType(TransportationType.BUS);
//
//            List<City> destinations = List.of(ljubljana, dubrovnik, rome, pisa, barcelona, paris, milano, london);
//            List<Partner> avioPartners = new ArrayList<>();
//
//            for (City dest : destinations) {
//                Partner planePartner = new Partner();
//                planePartner.setName("ZagAir to " + dest.getName());
//                planePartner.setOfferType(OfferType.TRANSPORT);
//                planePartner.setTransportationType(TransportationType.PLANE);
//                planePartner.setCities(Set.of(zagreb, dest));
//                avioPartners.add(planePartner);
//            }
//
//            accLjubljana1.setCities(Set.of(ljubljana));
//            accLjubljana2.setCities(Set.of(ljubljana));
//            accDubrovnik1.setCities(Set.of(dubrovnik));
//            accDubrovnik2.setCities(Set.of(dubrovnik));
//            accRome1.setCities(Set.of(rome));
//            accRome2.setCities(Set.of(rome));
//            accRome3.setCities(Set.of(rome));
//            accPisa1.setCities(Set.of(pisa));
//            accPisa2.setCities(Set.of(pisa));
//            accBarcelona1.setCities(Set.of(barcelona));
//            accBarcelona2.setCities(Set.of(barcelona));
//            accBarcelona3.setCities(Set.of(barcelona));
//            accParis1.setCities(Set.of(paris));
//            accParis2.setCities(Set.of(paris));
//            accMilano1.setCities(Set.of(milano));
//            accMilano2.setCities(Set.of(milano));
//            accLondon1.setCities(Set.of(london));
//            accLondon2.setCities(Set.of(london));
//            accLondon3.setCities(Set.of(london));
//
//            ljubljana.setPartners(Set.of(accLjubljana1, accLjubljana2));
//            dubrovnik.setPartners(Set.of(accDubrovnik1, accDubrovnik2));
//            rome.setPartners(Set.of(accRome1, accRome2, accRome3));
//            pisa.setPartners(Set.of(accPisa1, accPisa2));
//            barcelona.setPartners(Set.of(accBarcelona1, accBarcelona2, accBarcelona3));
//            paris.setPartners(Set.of(accParis1, accParis2));
//            milano.setPartners(Set.of(accMilano1, accMilano2));
//            london.setPartners(Set.of(accLondon1, accLondon2, accLondon3));
//
//            zagreb.setPartners(new HashSet<>(avioPartners));
//            for (int i = 0; i < destinations.size(); i++) {
//                City dest = destinations.get(i);
//                Partner avioPartner = avioPartners.get(i);
//
//                Set<Partner> existingPartners = new HashSet<>();
//                if (dest.getPartners() != null) {
//                    existingPartners.addAll(dest.getPartners());
//                }
//                existingPartners.add(avioPartner);
//                dest.setPartners(existingPartners);
//            }
//
//            Set<City> allCities = Set.of(zagreb, ljubljana, dubrovnik, rome, pisa, barcelona, paris, milano, london);
//            Set<Partner> allPartners = new HashSet<>(List.of(
//                    accLjubljana1, accLjubljana2, accDubrovnik1, accDubrovnik2, accRome1, accRome2, accRome3,
//                    accPisa1, accPisa2, accBarcelona1, accBarcelona2, accBarcelona3, accParis1, accParis2,
//                    accMilano1, accMilano2, accLondon1, accLondon2, accLondon3,
//                    bus1, bus2, bus3
//            ));
//            allPartners.addAll(avioPartners);
//
//            cityRepository.saveAll(allCities);
//            partnerRepository.saveAll(allPartners);
//
//            System.out.println("Sample data has been loaded.");
//
//        };
//    }
//
//    @Bean
//    public CommandLineRunner insertSampleUsers(UserRepository userRepository) {
//        return args -> {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//            String encryptedPassword = "password123";
//
//            User guide1 = new User();
//            guide1.setUsername("guide4@example.com");
//            guide1.setPassword(encryptedPassword);
//            guide1.setRole(Role.GUIDE);
//
//            User guide2 = new User();
//            guide2.setUsername("guide5@example.com");
//            guide2.setPassword(encryptedPassword);
//            guide2.setRole(Role.GUIDE);
//
//            User guide3 = new User();
//            guide3.setUsername("guide6@example.com");
//            guide3.setPassword(encryptedPassword);
//            guide3.setRole(Role.GUIDE);
//
//            User coordinator1 = new User();
//            coordinator1.setUsername("coord3@example.com");
//            coordinator1.setPassword(encryptedPassword);
//            coordinator1.setRole(Role.COORDINATOR);
//
//            User coordinator2 = new User();
//            coordinator2.setUsername("coord4@example.com");
//            coordinator2.setPassword(encryptedPassword);
//            coordinator2.setRole(Role.COORDINATOR);
//
//            userRepository.saveAll(Set.of(guide1, guide2, guide3, coordinator1, coordinator2));
//        };
//    }

}
