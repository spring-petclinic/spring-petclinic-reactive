package org.springframework.samples.petclinic.owner;

import java.io.Serializable;


public class OwnerCreationDto implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = 470454541735381314L;
    
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;
    
    public OwnerCreationDto() {}
    
    public OwnerCreationDto(String firstName, String lastName, String address, String city, String telephone) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.telephone = telephone;
    }
    /**
     * Getter accessor for attribute 'firstName'.
     *
     * @return
     *       current value of 'firstName'
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Setter accessor for attribute 'firstName'.
     * @param firstName
     * 		new value for 'firstName '
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * Getter accessor for attribute 'lastName'.
     *
     * @return
     *       current value of 'lastName'
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Setter accessor for attribute 'lastName'.
     * @param lastName
     * 		new value for 'lastName '
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * Getter accessor for attribute 'address'.
     *
     * @return
     *       current value of 'address'
     */
    public String getAddress() {
        return address;
    }
    /**
     * Setter accessor for attribute 'address'.
     * @param address
     * 		new value for 'address '
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * Getter accessor for attribute 'city'.
     *
     * @return
     *       current value of 'city'
     */
    public String getCity() {
        return city;
    }
    /**
     * Setter accessor for attribute 'city'.
     * @param city
     * 		new value for 'city '
     */
    public void setCity(String city) {
        this.city = city;
    }
    /**
     * Getter accessor for attribute 'telephone'.
     *
     * @return
     *       current value of 'telephone'
     */
    public String getTelephone() {
        return telephone;
    }
    /**
     * Setter accessor for attribute 'telephone'.
     * @param telephone
     * 		new value for 'telephone '
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
