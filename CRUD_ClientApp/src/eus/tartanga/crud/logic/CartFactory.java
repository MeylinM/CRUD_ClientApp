package eus.tartanga.crud.logic;

/**
 * Factory class responsible for creating and providing an instance of
 * CartManager.
 * <p>
 * This class follows the Singleton design pattern to ensure that only one
 * instance of CartManager is created and reused throughout the application.
 * </p>
 * <p>
 * If no instance of CartManager exists, a new CartClientRest instance will be
 * created and returned.
 * </p>
 *
 * @author Meylin
 */
public class CartFactory {

    private static CartManager cartManager;

    /**
     * Retrieves the instance of CartManager.
     * <p>
     * If no instance exists, a new CartClientRest is created and returned.
     * </p>
     *
     * @return The instance of CartManager.
     */
    public static CartManager getCartManager() {

        if (cartManager == null) {
            cartManager = new CartClientRest();
        }
        return cartManager;
    }
}
