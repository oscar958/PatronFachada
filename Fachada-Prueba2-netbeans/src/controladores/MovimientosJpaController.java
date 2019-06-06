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
import entidades.Movimientos;
import entidades.MovimientosPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author oscar
 */
public class MovimientosJpaController implements Serializable {

    public MovimientosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movimientos movimientos) throws PreexistingEntityException, Exception {
        if (movimientos.getMovimientosPK() == null) {
            movimientos.setMovimientosPK(new MovimientosPK());
        }
        movimientos.getMovimientosPK().setNumeroCuenta(movimientos.getCuentas().getNumeroCuenta());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuentas cuentas = movimientos.getCuentas();
            if (cuentas != null) {
                cuentas = em.getReference(cuentas.getClass(), cuentas.getNumeroCuenta());
                movimientos.setCuentas(cuentas);
            }
            em.persist(movimientos);
            if (cuentas != null) {
                cuentas.getMovimientosCollection().add(movimientos);
                cuentas = em.merge(cuentas);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMovimientos(movimientos.getMovimientosPK()) != null) {
                throw new PreexistingEntityException("Movimientos " + movimientos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Movimientos movimientos) throws NonexistentEntityException, Exception {
        movimientos.getMovimientosPK().setNumeroCuenta(movimientos.getCuentas().getNumeroCuenta());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movimientos persistentMovimientos = em.find(Movimientos.class, movimientos.getMovimientosPK());
            Cuentas cuentasOld = persistentMovimientos.getCuentas();
            Cuentas cuentasNew = movimientos.getCuentas();
            if (cuentasNew != null) {
                cuentasNew = em.getReference(cuentasNew.getClass(), cuentasNew.getNumeroCuenta());
                movimientos.setCuentas(cuentasNew);
            }
            movimientos = em.merge(movimientos);
            if (cuentasOld != null && !cuentasOld.equals(cuentasNew)) {
                cuentasOld.getMovimientosCollection().remove(movimientos);
                cuentasOld = em.merge(cuentasOld);
            }
            if (cuentasNew != null && !cuentasNew.equals(cuentasOld)) {
                cuentasNew.getMovimientosCollection().add(movimientos);
                cuentasNew = em.merge(cuentasNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MovimientosPK id = movimientos.getMovimientosPK();
                if (findMovimientos(id) == null) {
                    throw new NonexistentEntityException("The movimientos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MovimientosPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movimientos movimientos;
            try {
                movimientos = em.getReference(Movimientos.class, id);
                movimientos.getMovimientosPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimientos with id " + id + " no longer exists.", enfe);
            }
            Cuentas cuentas = movimientos.getCuentas();
            if (cuentas != null) {
                cuentas.getMovimientosCollection().remove(movimientos);
                cuentas = em.merge(cuentas);
            }
            em.remove(movimientos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Movimientos> findMovimientosEntities() {
        return findMovimientosEntities(true, -1, -1);
    }

    public List<Movimientos> findMovimientosEntities(int maxResults, int firstResult) {
        return findMovimientosEntities(false, maxResults, firstResult);
    }

    private List<Movimientos> findMovimientosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Movimientos.class));
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

    public Movimientos findMovimientos(MovimientosPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movimientos.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimientosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Movimientos> rt = cq.from(Movimientos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
