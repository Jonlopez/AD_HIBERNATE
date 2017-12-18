/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Gestion;
import modelo.GestionPK;
import modelo.Piezas;
import modelo.Proveedores;
import modelo.Proyectos;

/**
 *
 * @author Jon
 */
public class GestionJpaController implements Serializable {

    public GestionJpaController() {
        this.em = (EntityManager) EntityMan.getEntityManager();
    }
    private EntityManager em = null;

    public void create(Gestion gestion) throws PreexistingEntityException, Exception {
        if (gestion.getGestionPK() == null) {
            gestion.setGestionPK(new GestionPK());
            gestion.getGestionPK().setCodproveedor(gestion.getProveedores().getCodigo());
            gestion.getGestionPK().setCodpieza(gestion.getPiezas().getCodigo());
            gestion.getGestionPK().setCodproyecto(gestion.getProyectos().getCodigo());
        }
        
        //EntityManager em = null;
        try {
            //em = getEntityManager();
            em.getTransaction().begin();
            Piezas piezas = gestion.getPiezas();
            if (piezas != null) {
                piezas = em.getReference(piezas.getClass(), piezas.getCodigo());
                gestion.setPiezas(piezas);
            }
            Proveedores proveedores = gestion.getProveedores();
            if (proveedores != null) {
                proveedores = em.getReference(proveedores.getClass(), proveedores.getCodigo());
                gestion.setProveedores(proveedores);
            }
            Proyectos proyectos = gestion.getProyectos();
            if (proyectos != null) {
                proyectos = em.getReference(proyectos.getClass(), proyectos.getCodigo());
                gestion.setProyectos(proyectos);
            }
            em.persist(gestion);
            if (piezas != null) {
                piezas.getGestionList().add(gestion);
                piezas = em.merge(piezas);
            }
            if (proveedores != null) {
                proveedores.getGestionList().add(gestion);
                proveedores = em.merge(proveedores);
            }
            if (proyectos != null) {
                proyectos.getGestionList().add(gestion);
                proyectos = em.merge(proyectos);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGestion(gestion.getGestionPK()) != null) {
                throw new PreexistingEntityException("Gestion " + gestion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Gestion gestion) throws NonexistentEntityException, Exception {
        
        if (gestion.getGestionPK() == null) {
            gestion.setGestionPK(new GestionPK());
            gestion.getGestionPK().setCodproveedor(gestion.getProveedores().getCodigo());
            gestion.getGestionPK().setCodpieza(gestion.getPiezas().getCodigo());
            gestion.getGestionPK().setCodproyecto(gestion.getProyectos().getCodigo());
        }
        
        //EntityManager em = null;
        try {
          //  em = getEntityManager();
            em.getTransaction().begin();
            Gestion persistentGestion = em.find(Gestion.class, gestion.getGestionPK());
            Piezas piezasOld = persistentGestion.getPiezas();
            Piezas piezasNew = gestion.getPiezas();
            Proveedores proveedoresOld = persistentGestion.getProveedores();
            Proveedores proveedoresNew = gestion.getProveedores();
            Proyectos proyectosOld = persistentGestion.getProyectos();
            Proyectos proyectosNew = gestion.getProyectos();
            if (piezasNew != null) {
                piezasNew = em.getReference(piezasNew.getClass(), piezasNew.getCodigo());
                gestion.setPiezas(piezasNew);
            }
            if (proveedoresNew != null) {
                proveedoresNew = em.getReference(proveedoresNew.getClass(), proveedoresNew.getCodigo());
                gestion.setProveedores(proveedoresNew);
            }
            if (proyectosNew != null) {
                proyectosNew = em.getReference(proyectosNew.getClass(), proyectosNew.getCodigo());
                gestion.setProyectos(proyectosNew);
            }
            gestion = em.merge(gestion);
            if (piezasOld != null && !piezasOld.equals(piezasNew)) {
                piezasOld.getGestionList().remove(gestion);
                piezasOld = em.merge(piezasOld);
            }
            if (piezasNew != null && !piezasNew.equals(piezasOld)) {
                piezasNew.getGestionList().add(gestion);
                piezasNew = em.merge(piezasNew);
            }
            if (proveedoresOld != null && !proveedoresOld.equals(proveedoresNew)) {
                proveedoresOld.getGestionList().remove(gestion);
                proveedoresOld = em.merge(proveedoresOld);
            }
            if (proveedoresNew != null && !proveedoresNew.equals(proveedoresOld)) {
                proveedoresNew.getGestionList().add(gestion);
                proveedoresNew = em.merge(proveedoresNew);
            }
            if (proyectosOld != null && !proyectosOld.equals(proyectosNew)) {
                proyectosOld.getGestionList().remove(gestion);
                proyectosOld = em.merge(proyectosOld);
            }
            if (proyectosNew != null && !proyectosNew.equals(proyectosOld)) {
                proyectosNew.getGestionList().add(gestion);
                proyectosNew = em.merge(proyectosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                GestionPK id = gestion.getGestionPK();
                if (findGestion(id) == null) {
                    throw new NonexistentEntityException("The gestion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Gestion g) throws NonexistentEntityException {
        
        GestionPK id = g.getGestionPK();
        
        //EntityManager em = null;
        try {
           // em = getEntityManager();
            em.getTransaction().begin();
            Gestion gestion;
            try {
                gestion = em.getReference(Gestion.class, id);
                gestion.getGestionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gestion with id " + id + " no longer exists.", enfe);
            }
            
            em.remove(em.merge(g));
            em.getTransaction().commit(); 
           
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Gestion> findGestionEntities() {
        return findGestionEntities(true, -1, -1);
    }

    public List<Gestion> findGestionEntities(int maxResults, int firstResult) {
        return findGestionEntities(false, maxResults, firstResult);
    }

    private List<Gestion> findGestionEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Gestion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Gestion findGestion(GestionPK id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(Gestion.class, id);
        } finally {
            em.close();
        }
    }

    public int getGestionCount() {
       // EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Gestion> rt = cq.from(Gestion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
      private List<Gestion> resultado(Query q){        
        List<Gestion> lp = (List<Gestion>) q.getResultList();
        em.close();
        return lp;
    }
    
    public List<Gestion> findAll() {
        Query q = em.createNamedQuery("Gestion.findAll");
        return resultado(q);
    }
    
    
    public List<Gestion> findByGestionPK(GestionPK gestionPK) {
        Query q = em.createNamedQuery("Gestion.findByGestionPK");
        q.setParameter("codproveedor", gestionPK.getCodproveedor());
        q.setParameter("codpieza", gestionPK.getCodpieza());
        q.setParameter("codproyecto", gestionPK.getCodproyecto());
        return resultado(q);
    }
    
    
}
