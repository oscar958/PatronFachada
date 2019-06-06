/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Cuentas;
import entidades.Oficinas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class OficinasJpaController implements Serializable {

    public OficinasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Oficinas oficinas) throws PreexistingEntityException, Exception {
        if (oficinas.getCuentasCollection() == null) {
            oficinas.setCuentasCollection(new ArrayList<Cuentas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Cuentas> attachedCuentasCollection = new ArrayList<Cuentas>();
            for (Cuentas cuentasCollectionCuentasToAttach : oficinas.getCuentasCollection()) {
                cuentasCollectionCuentasToAttach = em.getReference(cuentasCollectionCuentasToAttach.getClass(), cuentasCollectionCuentasToAttach.getNumeroCuenta());
                attachedCuentasCollection.add(cuentasCollectionCuentasToAttach);
            }
            oficinas.setCuentasCollection(attachedCuentasCollection);
            em.persist(oficinas);
            for (Cuentas cuentasCollectionCuentas : oficinas.getCuentasCollection()) {
                Oficinas oldCodigoOficinaOfCuentasCollectionCuentas = cuentasCollectionCuentas.getCodigoOficina();
                cuentasCollectionCuentas.setCodigoOficina(oficinas);
                cuentasCollectionCuentas = em.merge(cuentasCollectionCuentas);
                if (oldCodigoOficinaOfCuentasCollectionCuentas != null) {
                    oldCodigoOficinaOfCuentasCollectionCuentas.getCuentasCollection().remove(cuentasCollectionCuentas);
                    oldCodigoOficinaOfCuentasCollectionCuentas = em.merge(oldCodigoOficinaOfCuentasCollectionCuentas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOficinas(oficinas.getCodigoOficina()) != null) {
                throw new PreexistingEntityException("Oficinas " + oficinas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Oficinas oficinas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Oficinas persistentOficinas = em.find(Oficinas.class, oficinas.getCodigoOficina());
            Collection<Cuentas> cuentasCollectionOld = persistentOficinas.getCuentasCollection();
            Collection<Cuentas> cuentasCollectionNew = oficinas.getCuentasCollection();
            Collection<Cuentas> attachedCuentasCollectionNew = new ArrayList<Cuentas>();
            for (Cuentas cuentasCollectionNewCuentasToAttach : cuentasCollectionNew) {
                cuentasCollectionNewCuentasToAttach = em.getReference(cuentasCollectionNewCuentasToAttach.getClass(), cuentasCollectionNewCuentasToAttach.getNumeroCuenta());
                attachedCuentasCollectionNew.add(cuentasCollectionNewCuentasToAttach);
            }
            cuentasCollectionNew = attachedCuentasCollectionNew;
            oficinas.setCuentasCollection(cuentasCollectionNew);
            oficinas = em.merge(oficinas);
            for (Cuentas cuentasCollectionOldCuentas : cuentasCollectionOld) {
                if (!cuentasCollectionNew.contains(cuentasCollectionOldCuentas)) {
                    cuentasCollectionOldCuentas.setCodigoOficina(null);
                    cuentasCollectionOldCuentas = em.merge(cuentasCollectionOldCuentas);
                }
            }
            for (Cuentas cuentasCollectionNewCuentas : cuentasCollectionNew) {
                if (!cuentasCollectionOld.contains(cuentasCollectionNewCuentas)) {
                    Oficinas oldCodigoOficinaOfCuentasCollectionNewCuentas = cuentasCollectionNewCuentas.getCodigoOficina();
                    cuentasCollectionNewCuentas.setCodigoOficina(oficinas);
                    cuentasCollectionNewCuentas = em.merge(cuentasCollectionNewCuentas);
                    if (oldCodigoOficinaOfCuentasCollectionNewCuentas != null && !oldCodigoOficinaOfCuentasCollectionNewCuentas.equals(oficinas)) {
                        oldCodigoOficinaOfCuentasCollectionNewCuentas.getCuentasCollection().remove(cuentasCollectionNewCuentas);
                        oldCodigoOficinaOfCuentasCollectionNewCuentas = em.merge(oldCodigoOficinaOfCuentasCollectionNewCuentas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = oficinas.getCodigoOficina();
                if (findOficinas(id) == null) {
                    throw new NonexistentEntityException("The oficinas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Oficinas oficinas;
            try {
                oficinas = em.getReference(Oficinas.class, id);
                oficinas.getCodigoOficina();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The oficinas with id " + id + " no longer exists.", enfe);
            }
            Collection<Cuentas> cuentasCollection = oficinas.getCuentasCollection();
            for (Cuentas cuentasCollectionCuentas : cuentasCollection) {
                cuentasCollectionCuentas.setCodigoOficina(null);
                cuentasCollectionCuentas = em.merge(cuentasCollectionCuentas);
            }
            em.remove(oficinas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Oficinas> findOficinasEntities() {
        return findOficinasEntities(true, -1, -1);
    }

    public List<Oficinas> findOficinasEntities(int maxResults, int firstResult) {
        return findOficinasEntities(false, maxResults, firstResult);
    }

    private List<Oficinas> findOficinasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Oficinas.class));
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

    public Oficinas findOficinas(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Oficinas.class, id);
        } finally {
            em.close();
        }
    }

    public int getOficinasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Oficinas> rt = cq.from(Oficinas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
