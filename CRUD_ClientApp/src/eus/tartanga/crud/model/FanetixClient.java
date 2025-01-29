/*
 * To change this license header,s choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Meylin and Irati
 */
@XmlRootElement
public class FanetixClient extends FanetixUser {

    private static final long serialVersionUID = 1L;
    private String fullName;
    private String street;
    private Integer zip;
    private String city;
    private Integer mobile;
    private List<Cart> products;

    public FanetixClient(String email, String passwrd) {
        super.setEmail(email);
        super.setPasswd(passwrd);
    }
    
    public FanetixClient(String email, String passwrd,String fullName, String street, Integer mobile, String city, Integer zip) {
        super.setEmail(email);
        super.setPasswd(passwrd);
        this.fullName=fullName;
        this.street=street;
        this.mobile=mobile;
        this.city=city;
        this.zip=zip;
    }

    public FanetixClient() {
    }
    
    @XmlElement
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @XmlElement
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @XmlElement
    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    @XmlElement
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @XmlElement
    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

}
