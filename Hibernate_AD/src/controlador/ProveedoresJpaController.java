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
import modelo.Proveedores;

/**
 *
 * @author Jon
 */
public class ProveedoresJpaController implements Serializable {

    public ProveedoresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedores proveedores) throws PreexistingEntityException, Exception {
        if (proveedores.getGestionList() == null) {
            proveedores.setGestionList(new ArrayList<Gestion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Gestion> attachedGestionList = new ArrayList<Gestion>();
            for (Gestion gestionListGestionToAttach : proveedores.getGestionList()) {
                gestionListGestionToAttach = em.getReference(gestionListGestionToAttach.getClass(), gestionListGestionToAttach.getGestionPK());
                attachedGestionList.add(gestionListGestionToAttach);
            }
            proveedores.setGestionList(attachedGestionList);
            em.persist(proveedores);
            for (Gestion gestionListGestion : proveedores.getGestionList()) {
                Proveedores oldProveedoresOfGestionListGestion = gestionListGestion.getProveedores();
                gestionListGestion.setProveedores(proveedores);
                gestionListGestion = em.merge(gestionListGestion);
                if (oldProveedoresOfGestionListGestion != null) {
                    oldProveedoresOfGestionListGestion.getGestionList().remove(gestionListGestion);
                    oldProveedoresOfGestionListGestion = em.merge(oldProveedoresOfGestionListGestion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProveedores(proveedores.getCodigo()) != null) {
                throw new PreexistingEntityException("Proveedores " + proveedores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedores proveedores) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedores persistentProveedores = em.find(Proveedores.class, proveedores.getCodigo());
            List<Gestion> gestionListOld = persistentProveedores.getGestionList();
            List<Gestion> gestionListNew = proveedores.getGestionList();
            List<String> illegalOrphanMessages = null;
            for (Gestion gestionListOldGestion : gestionListOld) {
                if (!gestionListNew.contains(gestionListOldGestion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Gestion " + gestionListOldGestion + " since its proveedores field is not nullable.");
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
            proveedores.setGestionList(gestionListNew);
            proveedores = em.merge(proveedores);
            for (Gestion gestionListNewGestion : gestionListNew) {
                if (!gestionListOld.contains(gestionListNewGestion)) {
                    Proveedores oldProveedoresOfGestionListNewGestion = gestionListNewGestion.getProveedores();
                    gestionListNewGestion.setProveedores(proveedores);
                    gestionListNewGestion = em.merge(gestionListNewGestion);
                    if (oldProveedoresOfGestionListNewGestion != null && !oldProveedoresOfGestionListNewGestion.equals(proveedores)) {
                        oldProveedoresOfGestionListNewGestion.getGestionList().remove(gestionListNewGestion);
                        oldProveedoresOfGestionListNewGestion = em.merge(oldProveedoresOfGestionListNewGestion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = proveedores.getCodigo();
                if (findProveedores(id) == null) {
                    throw new NonexistentEntityException("The proveedores with id " + id + " no longer exists.");
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
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedores proveedores;
            try {
                proveedores = em.getReference(Proveedores.class, id);
                proveedores.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedores with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Gestion> gestionListOrphanCheck = proveedores.getGestionList();
            for (Gestion gestionListOrphanCheckGestion : gestionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedores (" + proveedores + ") cannot be destroyed since the Gestion " + gestionListOrphanCheckGestion + " in its gestionList field has a non-nullable proveedores field.");
            }
            if (illegalOrphanMessages != null) {
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

    public List<Proveedores> findProveedoresEntities() {
        return findProveedoresEntities(true, -1, -1);
    }

    public List<Proveedores> findProveedoresEntities(int maxResults, int firstResult) {
        return findProveedoresEntities(false, maxResults, firstResult);
    }

    private List<Proveedores> findProveedoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedores.class));
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

    public Proveedores findProveedores(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedores.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedores> rt = cq.from(Proveedores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
