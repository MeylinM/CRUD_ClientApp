/*
 * To change this license header, choose License Headers in Project Properties.
 * To change tshis template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Olaia
 */

@XmlRootElement
public class Artist implements Serializable {

    private static final long serialVersionUID = 1L;

   
    private Integer artistId;

    private byte[] image;
   
   
    private Date debut;

    private String company;

   
    private String name;

    private String lastAlbum;

   
    private List<Concert> concertList;

   
    private Set<Product> getProducts;

   
    public Set<Product> getGetProducts() {
        return getProducts;
    }

    public void setGetProducts(Set<Product> getProducts) {
        this.getProducts = getProducts;
    }

    public Integer getArtistId() {
        return artistId;
    }

    public void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastAlbum() {
        return lastAlbum;
    }

    public void setLastAlbum(String lastAlbum) {
        this.lastAlbum = lastAlbum;
    }

   
    public List<Concert> getConcertList() {
        return concertList;
    }

    public void setConcertList(List<Concert> concertList) {
        this.concertList = concertList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (artistId != null ? artistId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set

        if (!(object instanceof Artist)) {
            return false;
        }
        Artist other = (Artist) object;
        if ((this.artistId == null && other.artistId != null) || (this.artistId != null && !this.artistId.equals(other.artistId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

}
