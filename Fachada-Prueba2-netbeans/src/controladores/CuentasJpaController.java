/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Oficinas;
import entidades.Clientes;
import entidades.Cuentas;
import java.util.ArrayList;
import java.util.Collection;
import entidades.Movimientos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class CuentasJpaController implements Serializable {

    public CuentasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cuentas cuentas) throws PreexistingEntityException, Exception {
        if (cuentas.getClientesCollection() == null) {
            cuentas.setClientesCollection(new ArrayList<Clientes>());
        }
        if (cuentas.getMovimientosCollection() == null) {
            cuentas.setMovimientosCollection(new ArrayList<Movimientos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Oficinas codigoOficina = cuentas.getCodigoOficina();
            if (codigoOficina != null) {
                codigoOficina = em.getReference(codigoOficina.getClass(), codigoOficina.getCodigoOficina());
                cuentas.setCodigoOficina(codigoOficina);
            }
            Collection<Clientes> attachedClientesCollection = new ArrayList<Clientes>();
            for (Clientes clientesCollectionClientesToAttach : cuentas.getClientesCollection()) {
                clientesCollectionClientesToAttach = em.getReference(clientesCollectionClientesToAttach.getClass(), clientesCollectionClientesToAttach.getCodigoCliente());
                attachedClientesCollection.add(clientesCollectionClientesToAttach);
            }
            cuentas.setClientesCollection(attachedClientesCollection);
            Collection<Movimientos> attachedMovimientosCollection = new ArrayList<Movimientos>();
            for (Movimientos movimientosCollectionMovimientosToAttach : cuentas.getMovimientosCollection()) {
                movimientosCollectionMovimientosToAttach = em.getReference(movimientosCollectionMovimientosToAttach.getClass(), movimientosCollectionMovimientosToAttach.getMovimientosPK());
                attachedMovimientosCollection.add(movimientosCollectionMovimientosToAttach);
            }
            cuentas.setMovimientosCollection(attachedMovimientosCollection);
            em.persist(cuentas);
            if (codigoOficina != null) {
                codigoOficina.getCuentasCollection().add(cuentas);
                codigoOficina = em.merge(codigoOficina);
            }
            for (Clientes clientesCollectionClientes : cuentas.getClientesCollection()) {
                clientesCollectionClientes.getCuentasCollection().add(cuentas);
                clientesCollectionClientes = em.merge(clientesCollectionClientes);
            }
            for (Movimientos movimientosCollectionMovimientos : cuentas.getMovimientosCollection()) {
                Cuentas oldCuentasOfMovimientosCollectionMovimientos = movimientosCollectionMovimientos.getCuentas();
                movimientosCollectionMovimientos.setCuentas(cuentas);
                movimientosCollectionMovimientos = em.merge(movimientosCollectionMovimientos);
                if (oldCuentasOfMovimientosCollectionMovimientos != null) {
                    oldCuentasOfMovimientosCollectionMovimientos.getMovimientosCollection().remove(movimientosCollectionMovimientos);
                    oldCuentasOfMovimientosCollectionMovimientos = em.merge(oldCuentasOfMovimientosCollectionMovimientos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCuentas(cuentas.getNumeroCuenta()) != null) {
                throw new PreexistingEntityException("Cuentas " + cuentas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cuentas cuentas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuentas persistentCuentas = em.find(Cuentas.class, cuentas.getNumeroCuenta());
            Oficinas codigoOficinaOld = persistentCuentas.getCodigoOficina();
            Oficinas codigoOficinaNew = cuentas.getCodigoOficina();
            Collection<Clientes> clientesCollectionOld = persistentCuentas.getClientesCollection();
            Collection<Clientes> clientesCollectionNew = cuentas.getClientesCollection();
            Collection<Movimientos> movimientosCollectionOld = persistentCuentas.getMovimientosCollection();
            Collection<Movimientos> movimientosCollectionNew = cuentas.getMovimientosCollection();
            List<String> illegalOrphanMessages = null;
            for (Movimientos movimientosCollectionOldMovimientos : movimientosCollectionOld) {
                if (!movimientosCollectionNew.contains(movimientosCollectionOldMovimientos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Movimientos " + movimientosCollectionOldMovimientos + " since its cuentas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codigoOficinaNew != null) {
                codigoOficinaNew = em.getReference(codigoOficinaNew.getClass(), codigoOficinaNew.getCodigoOficina());
                cuentas.setCodigoOficina(codigoOficinaNew);
            }
            Collection<Clientes> attachedClientesCollectionNew = new ArrayList<Clientes>();
            for (Clientes clientesCollectionNewClientesToAttach : clientesCollectionNew) {
                clientesCollectionNewClientesToAttach = em.getReference(clientesCollectionNewClientesToAttach.getClass(), clientesCollectionNewClientesToAttach.getCodigoCliente());
                attachedClientesCollectionNew.add(clientesCollectionNewClientesToAttach);
            }
            clientesCollectionNew = attachedClientesCollectionNew;
            cuentas.setClientesCollection(clientesCollectionNew);
            Collection<Movimientos> attachedMovimientosCollectionNew = new ArrayList<Movimientos>();
            for (Movimientos movimientosCollectionNewMovimientosToAttach : movimientosCollectionNew) {
                movimientosCollectionNewMovimientosToAttach = em.getReference(movimientosCollectionNewMovimientosToAttach.getClass(), movimientosCollectionNewMovimientosToAttach.getMovimientosPK());
                attachedMovimientosCollectionNew.add(movimientosCollectionNewMovimientosToAttach);
            }
            movimientosCollectionNew = attachedMovimientosCollectionNew;
            cuentas.setMovimientosCollection(movimientosCollectionNew);
            cuentas = em.merge(cuentas);
            if (codigoOficinaOld != null && !codigoOficinaOld.equals(codigoOficinaNew)) {
                codigoOficinaOld.getCuentasCollection().remove(cuentas);
                codigoOficinaOld = em.merge(codigoOficinaOld);
            }
            if (codigoOficinaNew != null && !codigoOficinaNew.equals(codigoOficinaOld)) {
                codigoOficinaNew.getCuentasCollection().add(cuentas);
                codigoOficinaNew = em.merge(codigoOficinaNew);
            }
            for (Clientes clientesCollectionOldClientes : clientesCollectionOld) {
                if (!clientesCollectionNew.contains(clientesCollectionOldClientes)) {
                    clientesCollectionOldClientes.getCuentasCollection().remove(cuentas);
                    clientesCollectionOldClientes = em.merge(clientesCollectionOldClientes);
                }
            }
            for (Clientes clientesCollectionNewClientes : clientesCollectionNew) {
                if (!clientesCollectionOld.contains(clientesCollectionNewClientes)) {
                    clientesCollectionNewClientes.getCuentasCollection().add(cuentas);
                    clientesCollectionNewClientes = em.merge(clientesCollectionNewClientes);
                }
            }
            for (Movimientos movimientosCollectionNewMovimientos : movimientosCollectionNew) {
                if (!movimientosCollectionOld.contains(movimientosCollectionNewMovimientos)) {
                    Cuentas oldCuentasOfMovimientosCollectionNewMovimientos = movimientosCollectionNewMovimientos.getCuentas();
                    movimientosCollectionNewMovimientos.setCuentas(cuentas);
                    movimientosCollectionNewMovimientos = em.merge(movimientosCollectionNewMovimientos);
                    if (oldCuentasOfMovimientosCollectionNewMovimientos != null && !oldCuentasOfMovimientosCollectionNewMovimientos.equals(cuentas)) {
                        oldCuentasOfMovimientosCollectionNewMovimientos.getMovimientosCollection().remove(movimientosCollectionNewMovimientos);
                        oldCuentasOfMovimientosCollectionNewMovimientos = em.merge(oldCuentasOfMovimientosCollectionNewMovimientos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = cuentas.getNumeroCuenta();
                if (findCuentas(id) == null) {
                    throw new NonexistentEntityException("The cuentas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuentas cuentas;
            try {
                cuentas = em.getReference(Cuentas.class, id);
                cuentas.getNumeroCuenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuentas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Movimientos> movimientosCollectionOrphanCheck = cuentas.getMovimientosCollection();
            for (Movimientos movimientosCollectionOrphanCheckMovimientos : movimientosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cuentas (" + cuentas + ") cannot be destroyed since the Movimientos " + movimientosCollectionOrphanCheckMovimientos + " in its movimientosCollection field has a non-nullable cuentas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Oficinas codigoOficina = cuentas.getCodigoOficina();
            if (codigoOficina != null) {
                codigoOficina.getCuentasCollection().remove(cuentas);
                codigoOficina = em.merge(codigoOficina);
            }
            Collection<Clientes> clientesCollection = cuentas.getClientesCollection();
            for (Clientes clientesCollectionClientes : clientesCollection) {
                clientesCollectionClientes.getCuentasCollection().remove(cuentas);
                clientesCollectionClientes = em.merge(clientesCollectionClientes);
            }
            em.remove(cuentas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cuentas> findCuentasEntities() {
        return findCuentasEntities(true, -1, -1);
    }

    public List<Cuentas> findCuentasEntities(int maxResults, int firstResult) {
        return findCuentasEntities(false, maxResults, firstResult);
    }

    private List<Cuentas> findCuentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuentas.class));
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

    public Cuentas findCuentas(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuentas.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuentas> rt = cq.from(Cuentas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
