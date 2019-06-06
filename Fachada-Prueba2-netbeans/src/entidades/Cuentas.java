/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author oscar
 */
@Entity
@Table(name = "CUENTAS")
@NamedQueries({
    @NamedQuery(name = "Cuentas.findAll", query = "SELECT c FROM Cuentas c")})
public class Cuentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NUMERO_CUENTA")
    private Short numeroCuenta;
    @Column(name = "TIPO")
    private String tipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "SALDO")
    private BigDecimal saldo;
    @Column(name = "VALOR_APERTURA")
    private BigDecimal valorApertura;
    @ManyToMany(mappedBy = "cuentasCollection")
    private Collection<Clientes> clientesCollection;
    @JoinColumn(name = "CODIGO_OFICINA", referencedColumnName = "CODIGO_OFICINA")
    @ManyToOne
    private Oficinas codigoOficina;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuentas")
    private Collection<Movimientos> movimientosCollection;

    public Cuentas() {
    }

    public Cuentas(Short numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Short getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(Short numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getValorApertura() {
        return valorApertura;
    }

    public void setValorApertura(BigDecimal valorApertura) {
        this.valorApertura = valorApertura;
    }

    public Collection<Clientes> getClientesCollection() {
        return clientesCollection;
    }

    public void setClientesCollection(Collection<Clientes> clientesCollection) {
        this.clientesCollection = clientesCollection;
    }

    public Oficinas getCodigoOficina() {
        return codigoOficina;
    }

    public void setCodigoOficina(Oficinas codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    public Collection<Movimientos> getMovimientosCollection() {
        return movimientosCollection;
    }

    public void setMovimientosCollection(Collection<Movimientos> movimientosCollection) {
        this.movimientosCollection = movimientosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroCuenta != null ? numeroCuenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuentas)) {
            return false;
        }
        Cuentas other = (Cuentas) object;
        if ((this.numeroCuenta == null && other.numeroCuenta != null) || (this.numeroCuenta != null && !this.numeroCuenta.equals(other.numeroCuenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Cuentas[ numeroCuenta=" + numeroCuenta + " ]";
    }
    
}
