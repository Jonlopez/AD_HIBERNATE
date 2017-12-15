/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Gestion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Piezas;
import modelo.Proyectos;

/**
 *
 * @author Jon
 */
public class ProyectosJpaController implements Serializable {

    public ProyectosJpaController() {
        this.em = (EntityManager) EntityMan.getEntityManager();
    }
    private EntityManager em = null;  

    public void create(Proyectos proyectos) throws PreexistingEntityException, Exception {
        if (proyectos.getGestionList() == null) {
            proyectos.setGestionList(new ArrayList<Gestion>());
        }
        EntityManager em = null;
        try {
           // em = getEntityManager();
            em.getTransaction().begin();
            List<Gestion> attachedGestionList = new ArrayList<Gestion>();
            for (Gestion gestionListGestionToAttach : proyectos.getGestionList()) {
                gestionListGestionToAttach = em.getReference(gestionListGestionToAttach.getClass(), gestionListGestionToAttach.getGestionPK());
                attachedGestionList.add(gestionListGestionToAttach);
            }
            proyectos.setGestionList(attachedGestionList);
            em.persist(proyectos);
            for (Gestion gestionListGestion : proyectos.getGestionList()) {
                Proyectos oldProyectosOfGestionListGestion = gestionListGestion.getProyectos();
                gestionListGestion.setProyectos(proyectos);
                gestionListGestion = em.merge(gestionListGestion);
                if (oldProyectosOfGestionListGestion != null) {
                    oldProyectosOfGestionListGestion.getGestionList().remove(gestionListGestion);
                    oldProyectosOfGestionListGestion = em.merge(oldProyectosOfGestionListGestion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProyectos(proyectos.getCodigo()) != null) {
                throw new PreexistingEntityException("Proyectos " + proyectos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proyectos proyectos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            //em = getEntityManager();
            em.getTransaction().begin();
            Proyectos persistentProyectos = em.find(Proyectos.class, proyectos.getCodigo());
            List<Gestion> gestionListOld = persistentProyectos.getGestionList();
            List<Gestion> gestionListNew = proyectos.getGestionList();
            List<String> illegalOrphanMessages = null;
            for (Gestion gestionListOldGestion : gestionListOld) {
                if (!gestionListNew.contains(gestionListOldGestion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Gestion " + gestionListOldGestion + " since its proyectos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Gestion> attachedGestionListNew = new ArrayList<Gestion>();
            for (Gestion gestionListNewGestionToAttach : gestionListNew) {
                gestionListNewGestionToAttach = em.getReference(gestionListNewGestionToAttach.getClass(), gestionListNewGestionToAttach.getGestionPK());
                attachedGestionListNew.add(gestionListNewGestionToAttach);
            }
            gestionListNew = attachedGestionListNew;
            proyectos.setGestionList(gestionListNew);
            proyectos = em.merge(proyectos);
            for (Gestion gestionListNewGestion : gestionListNew) {
                if (!gestionListOld.contains(gestionListNewGestion)) {
                    Proyectos oldProyectosOfGestionListNewGestion = gestionListNewGestion.getProyectos();
                    gestionListNewGestion.setProyectos(proyectos);
                    gestionListNewGestion = em.merge(gestionListNewGestion);
                    if (oldProyectosOfGestionListNewGestion != null && !oldProyectosOfGestionListNewGestion.equals(proyectos)) {
                        oldProyectosOfGestionListNewGestion.getGestionList().remove(gestionListNewGestion);
                        oldProyectosOfGestionListNewGestion = em.merge(oldProyectosOfGestionListNewGestion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = proyectos.getCodigo();
                if (findProyectos(id) == null) {
                    throw new NonexistentEntityException("The proyectos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            //em = getEntityManager();
            em.getTransaction().begin();
            Proyectos proyectos;
            try {
                proyectos = em.getReference(Proyectos.class, id);
                proyectos.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proyectos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Gestion> gestionListOrphanCheck = proyectos.getGestionList();
            for (Gestion gestionListOrphanCheckGestion : gestionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proyectos (" + proyectos + ") cannot be destroyed since the Gestion " + gestionListOrphanCheckGestion + " in its gestionList field has a non-nullable proyectos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(proyectos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proyectos> findProyectosEntities() {
        return findProyectosEntities(true, -1, -1);
    }

    public List<Proyectos> findProyectosEntities(int maxResults, int firstResult) {
        return findProyectosEntities(false, maxResults, firstResult);
    }

    private List<Proyectos> findProyectosEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proyectos.class));
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

    public Proyectos findProyectos(String id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(Proyectos.class, id);
        } finally {
            em.close();
        }
    }

    public int getProyectosCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proyectos> rt = cq.from(Proyectos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    private List<Proyectos> resultado(Query q){        
        List<Proyectos> lp = (List<Proyectos>) q.getResultList();
        em.close();
        return lp;
    }
    
    public List<Proyectos> findAll() {
        Query q = em.createNamedQuery("Piezas.findAll");
        return resultado(q);
    }
    
    public List<Proyectos> findByCodigo(String codigo) {
        Query q = em.createNamedQuery("Piezas.findByCodigo");
        q.setParameter("codigo", codigo);        
        return resultado(q);
    }
    
     public List<Proyectos> findByCodigoLike(String codigo_l) {
        Query q = em.createNamedQuery("Piezas.findByCodigoLike");
        q.setParameter("codigo", "%" + codigo_l + "%");
        return resultado(q);
    }
    
    public List<Proyectos> findByNombre(String nombre) {
        Query q = em.createNamedQuery("Piezas.findByNombre");
        q.setParameter("nombre", nombre);        
        return resultado(q);
    }
    
    public List<Proyectos> findByNombreLike(String nombre_l) {
        Query q = em.createNamedQuery("Piezas.findByNombreLike");
        q.setParameter("nombre", "%" + nombre_l + "%");        
        return resultado(q);
    }   
    
     public List<Proyectos> findByCiudad(String ciudad) {
        Query q = em.createNamedQuery("Piezas.findByCiudad");
        q.setParameter("ciudad", ciudad);        
        return resultado(q);
    }
    
    public List<Proyectos> findByCiudadLike(String ciudad_l) {
        Query q = em.createNamedQuery("Piezas.findByCiudadLike");
        q.setParameter("ciudad", "%" + ciudad_l + "%");        
        return resultado(q);
    }   
    
}
