/*
 * To change this license header, choose License Headers in Project Properties.
 * To change sthis template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.model;

import java.io.Serializable;


/**
 *
 * @author Meylin
 */
public class FanetixUser implements Serializable {

    private static final long serialVersionUID = 1L;
  
    private String email;
    private String passwd;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (email != null ? email.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the email fields are not set
        if (!(object instanceof FanetixUser)) {
            return false;
        }
        FanetixUser other = (FanetixUser) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eus.tartanga.crud.entities.FanetixUser[ id=" + email + " ]";
    }

}
