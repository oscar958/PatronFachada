/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author oscar
 */
@Entity
@Table(name = "MOVIMIENTOS")
@NamedQueries({
    @NamedQuery(name = "Movimientos.findAll", query = "SELECT m FROM Movimientos m")})
public class Movimientos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MovimientosPK movimientosPK;
    @Column(name = "TIPO")
    private String tipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VALOR")
    private BigDecimal valor;
    @JoinColumn(name = "NUMERO_CUENTA", referencedColumnName = "NUMERO_CUENTA", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cuentas cuentas;

    public Movimientos() {
    }

    public Movimientos(MovimientosPK movimientosPK) {
        this.movimientosPK = movimientosPK;
    }

    public Movimientos(short numeroCuenta, short numero) {
        this.movimientosPK = new MovimientosPK(numeroCuenta, numero);
    }

    public MovimientosPK getMovimientosPK() {
        return movimientosPK;
    }

    public void setMovimientosPK(MovimientosPK movimientosPK) {
        this.movimientosPK = movimientosPK;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Cuentas getCuentas() {
        return cuentas;
    }

    public void setCuentas(Cuentas cuentas) {
        this.cuentas = cuentas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movimientosPK != null ? movimientosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movimientos)) {
            return false;
        }
        Movimientos other = (Movimientos) object;
        if ((this.movimientosPK == null && other.movimientosPK != null) || (this.movimientosPK != null && !this.movimientosPK.equals(other.movimientosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Movimientos[ movimientosPK=" + movimientosPK + " ]";
    }
    
}
