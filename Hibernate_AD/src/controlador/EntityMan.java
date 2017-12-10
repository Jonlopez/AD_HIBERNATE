/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Jon
 */
public class EntityMan {
    private static final EntityManagerFactory emf;
    
    static {
        emf = Persistence.createEntityManagerFactory("Hibernate_ADPU");
    }
    
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public static void close(){
        emf.close();
    }
}