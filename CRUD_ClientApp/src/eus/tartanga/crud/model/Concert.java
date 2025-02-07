/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representa un concierto, con información sobre su identificación, artistas,
 * ubicación, fecha y hora. Esta clase se utiliza para almacenar y transferir
 * datos sobre conciertos en formato serializado.
 *
 * La clase también proporciona métodos para obtener los nombres de los artistas
 * asociados y para comparar y representar de manera adecuada los conciertos.
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

    /**
     * Constructor por defecto de la clase Concert. Inicializa los valores de
     * nombre, ubicación y ciudad con valores predeterminados, y la fecha y hora
     * del concierto con la fecha y hora actuales.
     */
    public Concert() {
        this.concertName = "Name of the concert or tour";
        this.location = "Concert location";
        this.city = "Concert location city";
        this.concertDate = new Date();
        this.concertTime = new Date();
    }

    /**
     * Obtiene el identificador único del concierto.
     *
     * @return El identificador del concierto.
     */
    public Integer getConcertId() {
        return concertId;
    }

    /**
     * Establece el identificador único del concierto.
     *
     * @param concertId El nuevo identificador del concierto.
     */
    public void setConcertId(Integer concertId) {
        this.concertId = concertId;
    }

    /**
     * Obtiene la cartelera del concierto, generalmente representada como una
     * imagen o archivo binario.
     *
     * @return La cartelera del concierto en formato de bytes.
     */
    public byte[] getBillboard() {
        return billboard;
    }

    /**
     * Establece la cartelera del concierto.
     *
     * @param billboard La cartelera en formato de bytes.
     */
    public void setBillboard(byte[] billboard) {
        this.billboard = billboard;
    }

    /**
     * Obtiene el nombre del concierto o la gira.
     *
     * @return El nombre del concierto.
     */
    public String getConcertName() {
        return concertName;
    }

    /**
     * Establece el nombre del concierto o la gira.
     *
     * @param concertName El nuevo nombre del concierto.
     */
    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    /**
     * Obtiene la lista de artistas que participarán en el concierto.
     *
     * @return La lista de artistas.
     */
    public List<Artist> getArtistList() {
        return artistList;
    }

    /**
     * Establece la lista de artistas que participarán en el concierto.
     *
     * @param artistList La lista de artistas.
     */
    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    /**
     * Obtiene la ubicación del concierto.
     *
     * @return La ubicación del concierto.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Establece la ubicación del concierto.
     *
     * @param location La nueva ubicación del concierto.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Obtiene la ciudad donde se llevará a cabo el concierto.
     *
     * @return La ciudad del concierto.
     */
    public String getCity() {
        return city;
    }

    /**
     * Establece la ciudad donde se llevará a cabo el concierto.
     *
     * @param city La nueva ciudad del concierto.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Obtiene la fecha en la que se llevará a cabo el concierto.
     *
     * @return La fecha del concierto.
     */
    public Date getConcertDate() {
        return concertDate;
    }

    /**
     * Establece la fecha en la que se llevará a cabo el concierto.
     *
     * @param concertDate La nueva fecha del concierto.
     */
    public void setConcertDate(Date concertDate) {
        this.concertDate = concertDate;
    }

    /**
     * Obtiene la hora en la que se llevará a cabo el concierto.
     *
     * @return La hora del concierto.
     */
    public Date getConcertTime() {
        return concertTime;
    }

    /**
     * Establece la hora en la que se llevará a cabo el concierto.
     *
     * @param concertTime La nueva hora del concierto.
     */
    public void setConcertTime(Date concertTime) {
        this.concertTime = concertTime;
    }

    /**
     * Genera el código hash para el concierto, basándose en el identificador
     * único.
     *
     * @return El código hash generado.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (concertId != null ? concertId.hashCode() : 0);
        return hash;
    }

    /**
     * Compara el concierto con otro objeto.
     *
     * @param object El objeto con el que se desea comparar.
     * @return true si el concierto tiene el mismo identificador que el objeto
     * comparado.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Concert)) {
            return false;
        }
        Concert other = (Concert) object;
        if ((this.concertId == null && other.concertId != null) || (this.concertId != null && !this.concertId.equals(other.concertId))) {
            return false;
        }
        return true;
    }

    /**
     * Devuelve una representación en forma de cadena del concierto.
     *
     * @return Una cadena que representa el concierto, con su identificador.
     */
    @Override
    public String toString() {
        return "eus.tartanga.crud.entities.Concert[ id=" + concertId + " ]";
    }

    /**
     * Obtiene una cadena con los nombres de los artistas que participan en el
     * concierto.
     *
     * @return Los nombres de los artistas, separados por comas.
     */
    public String getArtistNames() {
        return artistList != null && !artistList.isEmpty()
                ? artistList.stream()
                        .map(Artist::getName)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("No artists")
                : "No artists";
    }
}
