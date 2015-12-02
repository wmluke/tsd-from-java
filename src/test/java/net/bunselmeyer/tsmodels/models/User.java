package net.bunselmeyer.tsmodels.models;


import org.joda.time.DateTime;

public class User implements Model {

    private String firstName;
    private String lastName;
    private Address address;
    private DateTime dateOfBirth;

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

    public Address getAddress() {
        return address;
    }

    public User setAddress(Address address) {
        this.address = address;
        return this;
    }

    public DateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public User setDateOfBirth(DateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }
}
