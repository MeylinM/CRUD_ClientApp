/*
 * To change this license header, choose License Headers in Project Properties.
 * To changse this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.model;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 *
 * @author Elbire
 */


@XmlRootElement
public class Product implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
   
    private Integer productId;

    private List<Cart> client;

    
    private String title;
    private String description;
    private Artist artist;
    private Date releaseDate;
    private String price;
    private String stock;
    private byte[] image;
    
    public Product() {
        this.title = "Tittle of the product";
        this.description = "Description of the product";
        this.releaseDate = new Date();
        this.price = "0";
        this.stock = "1";
    }
    
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the productId fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        return true;
    }
    
    public Product clone() throws CloneNotSupportedException{
        return (Product) super.clone();
    }

    @Override
    public String toString() {
        return "eus.tartanga.crud.entities.Product[ id=" + productId + " ]";
    }

}
