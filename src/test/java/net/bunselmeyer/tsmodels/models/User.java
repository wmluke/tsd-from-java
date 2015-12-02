package net.bunselmeyer.tsmodels.models;


import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class User implements Model {

    private String firstName;
    private String lastName;
    private Address homeAddress;
    private Address workAddress;
    private LocalDate dateOfBirth;
    private BigDecimal favoriteNumber;
    private Map<String, Double> lookupFoo;
    private Map<Integer, String> lookupBar;
    private List<Address> additionalAddresses;

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public User setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
        return this;
    }

    public Address getWorkAddress() {
        return workAddress;
    }

    public User setWorkAddress(Address workAddress) {
        this.workAddress = workAddress;
        return this;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public User setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public BigDecimal getFavoriteNumber() {
        return favoriteNumber;
    }

    public User setFavoriteNumber(BigDecimal favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
        return this;
    }

    public Map<String, Double> getLookupFoo() {
        return lookupFoo;
    }

    public User setLookupFoo(Map<String, Double> lookupFoo) {
        this.lookupFoo = lookupFoo;
        return this;
    }

    public Map<Integer, String> getLookupBar() {
        return lookupBar;
    }

    public User setLookupBar(Map<Integer, String> lookupBar) {
        this.lookupBar = lookupBar;
        return this;
    }

    public List<Address> getAdditionalAddresses() {
        return additionalAddresses;
    }

    public User setAdditionalAddresses(List<Address> additionalAddresses) {
        this.additionalAddresses = additionalAddresses;
        return this;
    }
}
