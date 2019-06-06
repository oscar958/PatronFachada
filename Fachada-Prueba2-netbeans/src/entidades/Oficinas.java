/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author oscar
 */
@Entity
@Table(name = "OFICINAS")
@NamedQueries({
    @NamedQuery(name = "Oficinas.findAll", query = "SELECT o FROM Oficinas o")})
public class Oficinas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODIGO_OFICINA")
    private Short codigoOficina;
    @Column(name = "NOMBRE")
    private String nombre;
    @OneToMany(mappedBy = "codigoOficina")
    private Collection<Cuentas> cuentasCollection;

    public Oficinas() {
    }

    public Oficinas(Short codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    public Short getCodigoOficina() {
        return codigoOficina;
    }

    public void setCodigoOficina(Short codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<Cuentas> getCuentasCollection() {
        return cuentasCollection;
    }

    public void setCuentasCollection(Collection<Cuentas> cuentasCollection) {
        this.cuentasCollection = cuentasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoOficina != null ? codigoOficina.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Oficinas)) {
            return false;
        }
        Oficinas other = (Oficinas) object;
        if ((this.codigoOficina == null && other.codigoOficina != null) || (this.codigoOficina != null && !this.codigoOficina.equals(other.codigoOficina))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Oficinas[ codigoOficina=" + codigoOficina + " ]";
    }
    
}
