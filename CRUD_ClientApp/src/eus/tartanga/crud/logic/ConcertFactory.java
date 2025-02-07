/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

/**
 * Fábrica para la creación y obtención de instancias de {@link ConcertManager}.
 *
 * La clase {@code ConcertFactory} es responsable de crear y proporcionar acceso
 * a una única instancia de {@code ConcertManager}. Utiliza el patrón de diseño
 * Singleton para asegurar que solo haya una instancia de {@code ConcertManager}
 * en toda la aplicación. Si la instancia aún no ha sido creada, se inicializa a
 * través de la clase {@code ConcertClientRest}.
 *
 * @author Irati
 */
public class ConcertFactory {

    private static ConcertManager concertManager;

    /**
     * Obtiene la instancia única de {@code ConcertManager}.
     *
     * Si no existe una instancia de {@code ConcertManager}, este método crea
     * una nueva instancia utilizando {@link ConcertClientRest}. Si ya existe
     * una instancia, simplemente la devuelve.
     *
     * @return Una instancia de {@code ConcertManager}, que puede ser utilizada
     * para interactuar con la API REST de conciertos.
     */
    public static ConcertManager getConcertManager() {
        if (concertManager == null) {
            concertManager = new ConcertClientRest();
        }
        return concertManager;
    }
}
