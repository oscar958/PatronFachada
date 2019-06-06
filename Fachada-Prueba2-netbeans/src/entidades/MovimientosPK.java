/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author oscar
 */
@Embeddable
public class MovimientosPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "NUMERO_CUENTA")
    private short numeroCuenta;
    @Basic(optional = false)
    @Column(name = "NUMERO")
    private short numero;

    public MovimientosPK() {
    }

    public MovimientosPK(short numeroCuenta, short numero) {
        this.numeroCuenta = numeroCuenta;
        this.numero = numero;
    }

    public short getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(short numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public short getNumero() {
        return numero;
    }

    public void setNumero(short numero) {
        this.numero = numero;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numeroCuenta;
        hash += (int) numero;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimientosPK)) {
            return false;
        }
        MovimientosPK other = (MovimientosPK) object;
        if (this.numeroCuenta != other.numeroCuenta) {
            return false;
        }
        if (this.numero != other.numero) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.MovimientosPK[ numeroCuenta=" + numeroCuenta + ", numero=" + numero + " ]";
    }
    
}
