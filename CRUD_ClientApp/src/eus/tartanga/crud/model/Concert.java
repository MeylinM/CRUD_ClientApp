/*
 * To change this license header, choosse License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Irati
 */


@XmlRootElement
public class Concert implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer concertId;
    private byte[] billboard;
    private String concertName;
    private List<Artist> artistList;
    private String location;
    private String city;
    private Date concertDate;
    private Date concertTime;

    public Concert() {
        this.concertName = "Name of the concert or tour";
        this.location = "Concert location";
        this.city = "Concert location city";
        this.concertDate = new Date();
        this.concertTime =new Date();
    }

    
    public Integer getConcertId() {
        return concertId;
    }

    public void setConcertId(Integer concertId) {
        this.concertId = concertId;
    }

    public byte[] getBillboard() {
        return billboard;
    }

    public void setBillboard(byte[] billboard) {
        this.billboard = billboard;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getConcertDate() {
        return concertDate;
    }

    public void setConcertDate(Date concertDate) {
        this.concertDate = concertDate;
    }

    public Date getConcertTime() {
        return concertTime;
    }

    public void setConcertTime(Date concertTime) {
        this.concertTime = concertTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (concertId != null ? concertId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Concert)) {
            return false;
        }
        Concert other = (Concert) object;
        if ((this.concertId == null && other.concertId != null) || (this.concertId != null && !this.concertId.equals(other.concertId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eus.tartanga.crud.entities.Concert[ id=" + concertId + " ]";
    }
}
