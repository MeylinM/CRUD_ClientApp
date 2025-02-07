package eus.tartanga.crud.logic;

/**
 * Fábrica de objetos para la gestión de productos.
 * Esta clase proporciona un único punto de acceso para obtener una instancia de 
 * ProductManager. Si aún no se ha creado, se instancia un objeto de tipo 
 * ProductClientRest que implementa la interfaz ProductManager.
 * 
 * @author Elbire
 */
public class ProductFactory {

    // Instancia estática de ProductManager
    private static ProductManager productManager;

    /**
     * Obtiene la instancia de ProductManager.
     * Si la instancia no ha sido creada previamente, se crea una nueva instancia
     * de ProductClientRest.
     * 
     * @return La instancia de ProductManager.
     */
    public static ProductManager getProductManager() {
        if (productManager == null) {
            productManager = new ProductClientRest();
        }
        return productManager;
    }
}
