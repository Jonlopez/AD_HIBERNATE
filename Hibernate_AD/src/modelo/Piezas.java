/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jon
 */
@Entity
@Table(name = "piezas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Piezas.findAll", query = "SELECT p FROM Piezas p"),
    @NamedQuery(name = "Piezas.findByCodigo", query = "SELECT p FROM Piezas p WHERE p.codigo = :codigo"),
    @NamedQuery(name = "Piezas.findByCodigoLike", query = "SELECT p FROM Piezas p WHERE p.codigo like :codigo"),
    @NamedQuery(name = "Piezas.findByNombre", query = "SELECT p FROM Piezas p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Piezas.findByNombreLike", query = "SELECT p FROM Piezas p WHERE p.nombre like :nombre"),
    @NamedQuery(name = "Piezas.findByPrecio", query = "SELECT p FROM Piezas p WHERE p.precio = :precio"),
    @NamedQuery(name = "Piezas.findByPrecioLike", query = "SELECT p FROM Piezas p WHERE p.precio like :precio")
})
public class Piezas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODIGO")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "PRECIO")
    private float precio;
    @Lob
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "piezas")
    private List<Gestion> gestionList;

    public Piezas() {
    }

    public Piezas(String codigo) {
        this.codigo = codigo;
    }

    public Piezas(String codigo, String nombre, float precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Gestion> getGestionList() {
        return gestionList;
    }

    public void setGestionList(List<Gestion> gestionList) {
        this.gestionList = gestionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Piezas)) {
            return false;
        }
        Piezas other = (Piezas) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Piezas[ codigo=" + codigo + " ]";
    }
    
}
