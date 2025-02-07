/*
 * To change this license header, choose License Headers in Project Properties.
 * To change thsis template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.model;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase que representa un Administrador en el sistema Fanetix.
 * @author Meylin and Irati
 */


@XmlRootElement
public class Administrator extends FanetixUser {

    private static final long serialVersionUID = 1L;
    
    private Date incorporationDate;

    public Date getIncorporationDate() {
        return incorporationDate;
    }

    public void setIncorporationDate(Date incorporationDate) {
        this.incorporationDate = incorporationDate;
    }
}
