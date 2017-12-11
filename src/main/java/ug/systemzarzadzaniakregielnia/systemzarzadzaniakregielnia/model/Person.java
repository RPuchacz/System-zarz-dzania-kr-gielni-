package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model;


import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
/**
 * Created by Lukasz on 2017-12-11.
 */
@Entity
public class Person extends AbstractEntity {

    @Enumerated
    private Role role = Role.CLIENT;
    @NotNull(message = "Pole nie moze byc puste")
    @Size(min = 3, max = 14, message = "Min. 3")
    private String firstName;
    @NotNull(message = "Pole nie moze byc puste")
    @Size(min = 3, max = 20, message = "Min. 3")
    private String lastName;
    @Column(unique = true)
    private String mail;
    private String phoneNumber;
    @Column(unique = true)
    @NotNull(message = "Pole nie moze byc puste")
    @Size(min = 3, max = 30, message = "Min. 3")
    private String login;
    @NotNull(message = "Pole nie moze byc puste")
    @Size(min = 3, max = 100, message = "Min. 3")
    private String password;
    private Date dateOfBirth = null;
    private boolean newsletter = false;
    private boolean sms = false;
    @Lob
    private String detailedInformation;
    @OneToOne(optional = true, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Address address;
    private int loyalPoints = 0;

    public int getLoyalPoints() {
        return loyalPoints;
    }

    public void setLoyalPoints(int loyalPoints) {
        this.loyalPoints = loyalPoints;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(boolean newsletter) {
        this.newsletter = newsletter;
    }

    public boolean getSms() {
        return sms;
    }

    public void setSms(boolean sms) {
        this.sms = sms;
    }

}
