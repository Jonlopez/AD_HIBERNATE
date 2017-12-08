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

/**
 *
 * @author Jon
 */
public class PiezasJpaController implements Serializable {

    public PiezasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Piezas piezas) throws PreexistingEntityException, Exception {
        if (piezas.getGestionList() == null) {
            piezas.setGestionList(new ArrayList<Gestion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Gestion> attachedGestionList = new ArrayList<Gestion>();
            for (Gestion gestionListGestionToAttach : piezas.getGestionList()) {
                gestionListGestionToAttach = em.getReference(gestionListGestionToAttach.getClass(), gestionListGestionToAttach.getGestionPK());
                attachedGestionList.add(gestionListGestionToAttach);
            }
            piezas.setGestionList(attachedGestionList);
            em.persist(piezas);
            for (Gestion gestionListGestion : piezas.getGestionList()) {
                Piezas oldPiezasOfGestionListGestion = gestionListGestion.getPiezas();
                gestionListGestion.setPiezas(piezas);
                gestionListGestion = em.merge(gestionListGestion);
                if (oldPiezasOfGestionListGestion != null) {
                    oldPiezasOfGestionListGestion.getGestionList().remove(gestionListGestion);
                    oldPiezasOfGestionListGestion = em.merge(oldPiezasOfGestionListGestion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPiezas(piezas.getCodigo()) != null) {
                throw new PreexistingEntityException("Piezas " + piezas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Piezas piezas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Piezas persistentPiezas = em.find(Piezas.class, piezas.getCodigo());
            List<Gestion> gestionListOld = persistentPiezas.getGestionList();
            List<Gestion> gestionListNew = piezas.getGestionList();
            List<String> illegalOrphanMessages = null;
            for (Gestion gestionListOldGestion : gestionListOld) {
                if (!gestionListNew.contains(gestionListOldGestion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Gestion " + gestionListOldGestion + " since its piezas field is not nullable.");
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
            piezas.setGestionList(gestionListNew);
            piezas = em.merge(piezas);
            for (Gestion gestionListNewGestion : gestionListNew) {
                if (!gestionListOld.contains(gestionListNewGestion)) {
                    Piezas oldPiezasOfGestionListNewGestion = gestionListNewGestion.getPiezas();
                    gestionListNewGestion.setPiezas(piezas);
                    gestionListNewGestion = em.merge(gestionListNewGestion);
                    if (oldPiezasOfGestionListNewGestion != null && !oldPiezasOfGestionListNewGestion.equals(piezas)) {
                        oldPiezasOfGestionListNewGestion.getGestionList().remove(gestionListNewGestion);
                        oldPiezasOfGestionListNewGestion = em.merge(oldPiezasOfGestionListNewGestion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = piezas.getCodigo();
                if (findPiezas(id) == null) {
                    throw new NonexistentEntityException("The piezas with id " + id + " no longer exists.");
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
            Piezas piezas;
            try {
                piezas = em.getReference(Piezas.class, id);
                piezas.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The piezas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Gestion> gestionListOrphanCheck = piezas.getGestionList();
            for (Gestion gestionListOrphanCheckGestion : gestionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Piezas (" + piezas + ") cannot be destroyed since the Gestion " + gestionListOrphanCheckGestion + " in its gestionList field has a non-nullable piezas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(piezas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Piezas> findPiezasEntities() {
        return findPiezasEntities(true, -1, -1);
    }

    public List<Piezas> findPiezasEntities(int maxResults, int firstResult) {
        return findPiezasEntities(false, maxResults, firstResult);
    }

    private List<Piezas> findPiezasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Piezas.class));
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

    public Piezas findPiezas(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Piezas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPiezasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Piezas> rt = cq.from(Piezas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
