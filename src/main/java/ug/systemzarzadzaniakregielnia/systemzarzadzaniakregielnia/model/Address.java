package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model;

import javax.persistence.Entity;

/**
 * Created by Lukasz on 2017-12-11.
 */

@Entity
public class Address extends AbstractEntity {
//    @NotNull(message = "Pole nie moze byc puste")
//    @Size(min = 3, max = 14)
    private String country;
//    @NotNull(message = "Pole nie moze byc puste")
//    @Size(min = 3, max = 14)
    private String city;
//    @NotNull(message = "Pole nie moze byc puste")
//    @Size(min = 3, max = 14)
    private String street;
    private String postalCode;

    public Address() {
    }

    public Address(String country, String city, String street, String postalCode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
