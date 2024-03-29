/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import entidades.Clientes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Cuentas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class ClientesJpaController implements Serializable {

    public ClientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) throws PreexistingEntityException, Exception {
        if (clientes.getCuentasCollection() == null) {
            clientes.setCuentasCollection(new ArrayList<Cuentas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Cuentas> attachedCuentasCollection = new ArrayList<Cuentas>();
            for (Cuentas cuentasCollectionCuentasToAttach : clientes.getCuentasCollection()) {
                cuentasCollectionCuentasToAttach = em.getReference(cuentasCollectionCuentasToAttach.getClass(), cuentasCollectionCuentasToAttach.getNumeroCuenta());
                attachedCuentasCollection.add(cuentasCollectionCuentasToAttach);
            }
            clientes.setCuentasCollection(attachedCuentasCollection);
            em.persist(clientes);
            for (Cuentas cuentasCollectionCuentas : clientes.getCuentasCollection()) {
                cuentasCollectionCuentas.getClientesCollection().add(clientes);
                cuentasCollectionCuentas = em.merge(cuentasCollectionCuentas);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClientes(clientes.getCodigoCliente()) != null) {
                throw new PreexistingEntityException("Clientes " + clientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clientes clientes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes persistentClientes = em.find(Clientes.class, clientes.getCodigoCliente());
            Collection<Cuentas> cuentasCollectionOld = persistentClientes.getCuentasCollection();
            Collection<Cuentas> cuentasCollectionNew = clientes.getCuentasCollection();
            Collection<Cuentas> attachedCuentasCollectionNew = new ArrayList<Cuentas>();
            for (Cuentas cuentasCollectionNewCuentasToAttach : cuentasCollectionNew) {
                cuentasCollectionNewCuentasToAttach = em.getReference(cuentasCollectionNewCuentasToAttach.getClass(), cuentasCollectionNewCuentasToAttach.getNumeroCuenta());
                attachedCuentasCollectionNew.add(cuentasCollectionNewCuentasToAttach);
            }
            cuentasCollectionNew = attachedCuentasCollectionNew;
            clientes.setCuentasCollection(cuentasCollectionNew);
            clientes = em.merge(clientes);
            for (Cuentas cuentasCollectionOldCuentas : cuentasCollectionOld) {
                if (!cuentasCollectionNew.contains(cuentasCollectionOldCuentas)) {
                    cuentasCollectionOldCuentas.getClientesCollection().remove(clientes);
                    cuentasCollectionOldCuentas = em.merge(cuentasCollectionOldCuentas);
                }
            }
            for (Cuentas cuentasCollectionNewCuentas : cuentasCollectionNew) {
                if (!cuentasCollectionOld.contains(cuentasCollectionNewCuentas)) {
                    cuentasCollectionNewCuentas.getClientesCollection().add(clientes);
                    cuentasCollectionNewCuentas = em.merge(cuentasCollectionNewCuentas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = clientes.getCodigoCliente();
                if (findClientes(id) == null) {
                    throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.");
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
            Clientes clientes;
            try {
                clientes = em.getReference(Clientes.class, id);
                clientes.getCodigoCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.", enfe);
            }
            Collection<Cuentas> cuentasCollection = clientes.getCuentasCollection();
            for (Cuentas cuentasCollectionCuentas : cuentasCollection) {
                cuentasCollectionCuentas.getClientesCollection().remove(clientes);
                cuentasCollectionCuentas = em.merge(cuentasCollectionCuentas);
            }
            em.remove(clientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clientes> findClientesEntities() {
        return findClientesEntities(true, -1, -1);
    }

    public List<Clientes> findClientesEntities(int maxResults, int firstResult) {
        return findClientesEntities(false, maxResults, firstResult);
    }

    private List<Clientes> findClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clientes.class));
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

    public Clientes findClientes(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clientes> rt = cq.from(Clientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
