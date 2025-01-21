/*
 * To change this liscense header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.model;

import java.io.Serializable;


/**
 *
 * @author Meylin
 */

public class CartId implements Serializable {

    private Integer productId;
    private String email;

    public CartId() {

    }

    public CartId(Integer productId, String email) {
        this.productId = productId;
        this.email = email;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CartId)) {
            return false;
        }
        CartId other = (CartId) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eus.tartanga.crud.entities.CartId[ id=" + productId + " ]";
    }

}
