package org.example.firstyearproject.AdressSearch;

import Helpers.Generated;

import java.io.Serializable;

public class Address implements Serializable {

    String street;
    String houseNumber;
    String postcode;
    String city;
    String country;
    @Generated
    public Address(String street, String houseNumber, String postcode, String city, String country) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
    }
    @Generated
    @Override
    public String toString() {
        return street + " " + houseNumber + ", " + postcode + " " + city + " " + country;
    }
}
