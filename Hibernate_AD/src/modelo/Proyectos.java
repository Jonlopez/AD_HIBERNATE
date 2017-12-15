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
@Table(name = "proyectos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proyectos.findAll", query = "SELECT p FROM Proyectos p"),
    @NamedQuery(name = "Proyectos.findByCodigo", query = "SELECT p FROM Proyectos p WHERE p.codigo = :codigo"),
    @NamedQuery(name = "Proyectos.findByCodigoLike", query = "SELECT p FROM Proyectos p WHERE p.codigo like :codigo"),
    @NamedQuery(name = "Proyectos.findByNombre", query = "SELECT p FROM Proyectos p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Proyectos.findByNombreLike", query = "SELECT p FROM Proyectos p WHERE p.nombre like :nombre"),
    @NamedQuery(name = "Proyectos.findByCiudad", query = "SELECT p FROM Proyectos p WHERE p.ciudad = :ciudad"),
    @NamedQuery(name = "Proyectos.findByCiudadLike", query = "SELECT p FROM Proyectos p WHERE p.ciudad like :ciudad")
})

public class Proyectos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODIGO")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "CIUDAD")
    private String ciudad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectos")
    private List<Gestion> gestionList;

    public Proyectos() {
    }

    public Proyectos(String codigo) {
        this.codigo = codigo;
    }

    public Proyectos(String codigo, String nombre, String ciudad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ciudad = ciudad;
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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
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
        if (!(object instanceof Proyectos)) {
            return false;
        }
        Proyectos other = (Proyectos) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Proyectos[ codigo=" + codigo + " ]";
    }
    
}
