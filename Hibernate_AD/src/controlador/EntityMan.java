/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Jon
 */
public final class EntityMan {
    
    private static EntityManagerFactory enf= Persistence.createEntityManagerFactory("Hibernate_ADPU");

    public EntityMan() {
    }
    
    public static EntityManagerFactory getInstance(){
        return enf;
    }
    
}
