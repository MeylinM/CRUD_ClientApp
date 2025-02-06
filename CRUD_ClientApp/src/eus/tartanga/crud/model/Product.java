package eus.tartanga.crud.model;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Representa un producto con detalles como título, descripción, artista, fecha
 * de lanzamiento, precio, stock e imagen.
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

    /**
     * Constructor por defecto que inicializa un producto con valores
     * predeterminados.
     */
    public Product() {
        this.title = "Tittle of the product";
        this.description = "Description of the product";
        this.releaseDate = new Date();
        this.price = "0";
        this.stock = "1";
    }

    /**
     * Obtiene el ID del producto.
     *
     * @return El ID del producto.
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * Establece el ID del producto.
     *
     * @param productId El ID del producto.
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * Obtiene el título del producto.
     *
     * @return El título del producto.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título del producto.
     *
     * @param title El título del producto.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la descripción del producto.
     *
     * @return La descripción del producto.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del producto.
     *
     * @param description La descripción del producto.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene el artista asociado al producto.
     *
     * @return El artista del producto.
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * Establece el artista del producto.
     *
     * @param artist El artista del producto.
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    /**
     * Obtiene la fecha de lanzamiento del producto.
     *
     * @return La fecha de lanzamiento.
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * Establece la fecha de lanzamiento del producto.
     *
     * @param releaseDate La fecha de lanzamiento.
     */
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return El precio del producto.
     */
    public String getPrice() {
        return price;
    }

    /**
     * Establece el precio del producto.
     *
     * @param price El precio del producto.
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Obtiene el stock del producto.
     *
     * @return El stock del producto.
     */
    public String getStock() {
        return stock;
    }

    /**
     * Establece el stock del producto.
     *
     * @param stock El stock del producto.
     */
    public void setStock(String stock) {
        this.stock = stock;
    }

    /**
     * Obtiene la imagen del producto.
     *
     * @return La imagen del producto en un arreglo de bytes.
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * Establece la imagen del producto.
     *
     * @param image La imagen en un arreglo de bytes.
     */
    public void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * Genera un código hash para el objeto basado en el ID del producto.
     *
     * @return El código hash del producto.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    /**
     * Compara si este producto es igual a otro objeto.
     *
     * @param object El objeto a comparar.
     * @return true si los IDs de los productos son iguales, false en caso
     * contrario.
     */
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

    /**
     * Clona el objeto Product.
     *
     * @return Una copia del objeto Product.
     * @throws CloneNotSupportedException Si la clonación no es soportada.
     */
    public Product clone() throws CloneNotSupportedException {
        return (Product) super.clone();
    }

    /**
     * Representación en cadena del objeto Product.
     *
     * @return Una cadena con información del producto.
     */
    @Override
    public String toString() {
        return "eus.tartanga.crud.entities.Product[ id=" + productId + " ]";
    }

}
