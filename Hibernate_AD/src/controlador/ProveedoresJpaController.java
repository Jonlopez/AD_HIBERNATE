/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import modelo.Gestion;
import modelo.Piezas;
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
        em.close();
    }
    
    public void delete(String id) throws NonexistentEntityException, IllegalOrphanException{
        
        try {
            //em = getEntityManager();
            em.getTransaction().begin();
            Proveedores proveedores;
            try {
                proveedores = em.getReference(Proveedores.class, id);
                proveedores.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The piezas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Gestion> gestionListOrphanCheck = proveedores.getGestionList();
            for (Gestion gestionListOrphanCheckGestion : gestionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedores (" + proveedores + ") cannot be destroyed since the Gestion " + gestionListOrphanCheckGestion + " in its gestionList field has a non-nullable piezas field.");
            }
            if (illegalOrphanMessages != null) {
                
                JOptionPane.showMessageDialog(null, "No se puede eliminar el proveedor porque todavía hay gestiones relacionadas con el mismo");
                
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(proveedores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
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
    
     public List<Proveedores> findByCodigoLike(String codigo_l) {
        Query q = em.createNamedQuery("Proveedores.findByCodigoLike");
        q.setParameter("codigo", "%" + codigo_l + "%");
        return resultado(q);
    }
    
    public List<Proveedores> findByNombre(String nombre) {
        Query q = em.createNamedQuery("Proveedores.findByNombre");
        q.setParameter("nombre", nombre);        
        return resultado(q);
    }
    
    public List<Proveedores> findByNombreLike(String nombre_l) {
        Query q = em.createNamedQuery("Proveedores.findByNombreLike");
        q.setParameter("nombre", "%" + nombre_l + "%");        
        return resultado(q);
    }
    
    public List<Proveedores> findByDireccion(String dir) {
        Query q = em.createNamedQuery("Proveedores.findByDireccion");
        q.setParameter("direccion", dir);        
        return resultado(q);
    }
    
    public List<Proveedores> findByDireccionLike(String dir_l) {
        Query q = em.createNamedQuery("Proveedores.findByDireccionLike");
        q.setParameter("direccion", "%" + dir_l + "%");        
        return resultado(q);
    }
    
}
