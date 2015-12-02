package net.bunselmeyer.tsmodels.models;

public class Address implements Model {

    private String streetAddress1;
    private String streetAddress2;
    private String streetAddress3;

    private String city;
    private String state;
    private String zipCode;
    private String country;

    public String getStreetAddress1() {
        return streetAddress1;
    }

    public Address setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
        return this;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public Address setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
        return this;
    }

    public String getStreetAddress3() {
        return streetAddress3;
    }

    public Address setStreetAddress3(String streetAddress3) {
        this.streetAddress3 = streetAddress3;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    public String getState() {
        return state;
    }

    public Address setState(String state) {
        this.state = state;
        return this;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Address setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Address setCountry(String country) {
        this.country = country;
        return this;
    }
}
