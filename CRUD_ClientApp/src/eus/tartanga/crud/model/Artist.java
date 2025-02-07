package eus.tartanga.crud.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents an artist entity in the system.
 * 
 * <p>This class stores information about an artist, including their name, 
 * debut date, record company, last album, and associated concerts and products.</p>
 * 
 * <p>The class implements {Serializable} to allow object serialization.</p>
 * 
 * @author Olaia
 */
@XmlRootElement
public class Artist implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique identifier for the artist. */
    private Integer artistId;

    /** Image representing the artist, stored as a byte array. */
    private byte[] image;

    /** The debut date of the artist. */
    private Date debut;

    /** The record company associated with the artist. */
    private String company;

    /** The name of the artist. */
    private String name;

    /** The most recent album released by the artist. */
    private String lastAlbum;

    /** List of concerts associated with the artist. */
    private List<Concert> concertList;

    /** Set of products related to the artist. */
    private Set<Product> getProducts;

    /**
     * Default constructor that initializes default values.
     */
    public Artist() {
        this.name = "Name of the artist";
        this.company = "Company of the artist";
        this.lastAlbum = "Last album of the artist";
        this.debut = new Date();
    }

    /**
     * Gets the set of products associated with the artist.
     *
     * @return A set of {@link Product} objects.
     */
    public Set<Product> getGetProducts() {
        return getProducts;
    }

    /**
     * Sets the products associated with the artist.
     *
     * @param getProducts A set of {@link Product} objects.
     */
    public void setGetProducts(Set<Product> getProducts) {
        this.getProducts = getProducts;
    }

    /**
     * Gets the artist's unique identifier.
     *
     * @return The artist ID.
     */
    public Integer getArtistId() {
        return artistId;
    }

    /**
     * Sets the artist's unique identifier.
     *
     * @param artistId The new artist ID.
     */
    public void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }

    /**
     * Gets the artist's image.
     *
     * @return The image as a byte array.
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * Sets the artist's image.
     *
     * @param image The image as a byte array.
     */
    public void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * Gets the artist's debut date.
     *
     * @return The debut date.
     */
    public Date getDebut() {
        return debut;
    }

    /**
     * Sets the artist's debut date.
     *
     * @param debut The new debut date.
     */
    public void setDebut(Date debut) {
        this.debut = debut;
    }

    /**
     * Gets the record company associated with the artist.
     *
     * @return The record company name.
     */
    public String getCompany() {
        return company;
    }

    /**
     * Sets the record company associated with the artist.
     *
     * @param company The new record company name.
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * Gets the artist's name.
     *
     * @return The artist's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the artist's name.
     *
     * @param name The new name of the artist.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the artist's last released album.
     *
     * @return The name of the last album.
     */
    public String getLastAlbum() {
        return lastAlbum;
    }

    /**
     * Sets the artist's last released album.
     *
     * @param lastAlbum The new last album name.
     */
    public void setLastAlbum(String lastAlbum) {
        this.lastAlbum = lastAlbum;
    }

    /**
     * Gets the list of concerts associated with the artist.
     *
     * @return A list of {@link Concert} objects.
     */
    public List<Concert> getConcertList() {
        return concertList;
    }

    /**
     * Sets the list of concerts associated with the artist.
     *
     * @param concertList A list of {@link Concert} objects.
     */
    public void setConcertList(List<Concert> concertList) {
        this.concertList = concertList;
    }

    /**
     * Generates a hash code for the artist based on the artist ID.
     *
     * @return The hash code of the artist.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (artistId != null ? artistId.hashCode() : 0);
        return hash;
    }

    /**
     * Compares two artists for equality based on their ID.
     *
     * @param object The object to compare with.
     * @return {@code true} if the artists have the same ID, otherwise {@code false}.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Artist)) {
            return false;
        }
        Artist other = (Artist) object;
        return (this.artistId != null || other.artistId == null) 
                && (this.artistId == null || this.artistId.equals(other.artistId));
    }

    /**
     * Returns a string representation of the artist.
     *
     * @return The artist's name.
     */
    @Override
    public String toString() {
        return name;
    }
}
