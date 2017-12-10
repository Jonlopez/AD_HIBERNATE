/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jon
 */
@Entity
@Table(name = "gestion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gestion.findAll", query = "SELECT g FROM Gestion g"),
    @NamedQuery(name = "Gestion.findByCodproveedor", query = "SELECT g FROM Gestion g WHERE g.gestionPK.codproveedor = :codproveedor"),
    @NamedQuery(name = "Gestion.findByCodpieza", query = "SELECT g FROM Gestion g WHERE g.gestionPK.codpieza = :codpieza"),
    @NamedQuery(name = "Gestion.findByCodproyecto", query = "SELECT g FROM Gestion g WHERE g.gestionPK.codproyecto = :codproyecto"),
    @NamedQuery(name = "Gestion.findByCantidad", query = "SELECT g FROM Gestion g WHERE g.cantidad = :cantidad")})
public class Gestion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GestionPK gestionPK;
    @Basic(optional = false)
    @Column(name = "CANTIDAD")
    private float cantidad;
    @JoinColumn(name = "CODPIEZA", referencedColumnName = "CODIGO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Piezas piezas;
    @JoinColumn(name = "CODPROVEEDOR", referencedColumnName = "CODIGO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Proveedores proveedores;
    @JoinColumn(name = "CODPROYECTO", referencedColumnName = "CODIGO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Proyectos proyectos;

    public Gestion() {
    }

    public Gestion(GestionPK gestionPK) {
        this.gestionPK = gestionPK;
    }

    public Gestion(GestionPK gestionPK, float cantidad) {
        this.gestionPK = gestionPK;
        this.cantidad = cantidad;
    }

    public Gestion(String codproveedor, String codpieza, String codproyecto) {
        this.gestionPK = new GestionPK(codproveedor, codpieza, codproyecto);
    }

    public GestionPK getGestionPK() {
        return gestionPK;
    }

    public void setGestionPK(GestionPK gestionPK) {
        this.gestionPK = gestionPK;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public Piezas getPiezas() {
        return piezas;
    }

    public void setPiezas(Piezas piezas) {
        this.piezas = piezas;
    }

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    public Proyectos getProyectos() {
        return proyectos;
    }

    public void setProyectos(Proyectos proyectos) {
        this.proyectos = proyectos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gestionPK != null ? gestionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gestion)) {
            return false;
        }
        Gestion other = (Gestion) object;
        if ((this.gestionPK == null && other.gestionPK != null) || (this.gestionPK != null && !this.gestionPK.equals(other.gestionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Gestion[ gestionPK=" + gestionPK + " ]";
    }
    
}
