/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
//import javax.persistence.EntityManagerFactory;
import modelo.Proveedores;

/**
 *
 * @author Jon
 */
public class ProveedoresJpaController implements Serializable {

    public ProveedoresJpaController() {
        this.em = (EntityManager) EntityMan.getEntityManager();
    }
    
    private EntityManager em = null;       

    public void createUpdate (Proveedores p){
        em.getTransaction().begin();
        em.merge(p);
        em.getTransaction().commit();
//        em.close();
    }
    
    public void delete(Proveedores p){
        em.getTransaction().begin();
        em.remove(em.merge(p));
        em.getTransaction().commit();
//        em.close();
    }    
    
    private List<Proveedores> resultado(Query q){        
        List<Proveedores> lp = (List<Proveedores>) q.getResultList();
        em.close();
        return lp;
    }
    
    public List<Proveedores> findAll() {
        Query q = em.createNamedQuery("Proveedores.findAll");
        return resultado(q);
    }
    
    public List<Proveedores> findByCodigo(String codigo) {
        Query q = em.createNamedQuery("Proveedores.findByCodigo");
        q.setParameter("codigo", codigo);        
        return resultado(q);
    }
    
    public List<Proveedores> findByNombre(String nombre) {
        Query q = em.createNamedQuery("Proveedores.findByNombre");
        q.setParameter("nombre", nombre);        
        return resultado(q);
    }
    
    public List<Proveedores> findByDireccion(String dir) {
        Query q = em.createNamedQuery("Proveedores.findByDireccion");
        q.setParameter("direccion", dir);        
        return resultado(q);
    }
    
}
