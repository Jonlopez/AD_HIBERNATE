/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Jon
 */
@Embeddable
public class GestionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "CODPROVEEDOR")
    private String codproveedor;
    @Basic(optional = false)
    @Column(name = "CODPIEZA")
    private String codpieza;
    @Basic(optional = false)
    @Column(name = "CODPROYECTO")
    private String codproyecto;

    public GestionPK() {
    }

    public GestionPK(String codproveedor, String codpieza, String codproyecto) {
        this.codproveedor = codproveedor;
        this.codpieza = codpieza;
        this.codproyecto = codproyecto;
    }

    public String getCodproveedor() {
        return codproveedor;
    }

    public void setCodproveedor(String codproveedor) {
        this.codproveedor = codproveedor;
    }

    public String getCodpieza() {
        return codpieza;
    }

    public void setCodpieza(String codpieza) {
        this.codpieza = codpieza;
    }

    public String getCodproyecto() {
        return codproyecto;
    }

    public void setCodproyecto(String codproyecto) {
        this.codproyecto = codproyecto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codproveedor != null ? codproveedor.hashCode() : 0);
        hash += (codpieza != null ? codpieza.hashCode() : 0);
        hash += (codproyecto != null ? codproyecto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GestionPK)) {
            return false;
        }
        GestionPK other = (GestionPK) object;
        if ((this.codproveedor == null && other.codproveedor != null) || (this.codproveedor != null && !this.codproveedor.equals(other.codproveedor))) {
            return false;
        }
        if ((this.codpieza == null && other.codpieza != null) || (this.codpieza != null && !this.codpieza.equals(other.codpieza))) {
            return false;
        }
        if ((this.codproyecto == null && other.codproyecto != null) || (this.codproyecto != null && !this.codproyecto.equals(other.codproyecto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.GestionPK[ codproveedor=" + codproveedor + ", codpieza=" + codpieza + ", codproyecto=" + codproyecto + " ]";
    }
    
}
