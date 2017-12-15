/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hibernate_ad;

import com.sun.deploy.uitoolkit.impl.fx.ui.MixedCodeInSwing;
import controlador.PiezasJpaController;
import controlador.ProveedoresJpaController;
import controlador.ProyectosJpaController;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Piezas;
import modelo.Proveedores;
import modelo.Proyectos;
import vista.VistaGestionProyectos;

/**
 *
 * @author Jon
 */
public class Hibernate_AD {
    
    private static VistaGestionProyectos vGestionProyectos;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            
            lanzarInterface();
            
        }catch(java.lang.ExceptionInInitializerError ex){
            
            JOptionPane.showMessageDialog(null, "No se ha podido conectar a la base de datos");   
            
        }
        
    }
    
    public static void lanzarInterface(){
        
        vGestionProyectos = new VistaGestionProyectos(findAllProveedores(), findAllPiezas(), findAllProyectos());
        
        vGestionProyectos.setVisible(true);
        
    }
    
    public static List<Proveedores> findAllProveedores(){  
            
        ProveedoresJpaController c_prov = new ProveedoresJpaController();

        return c_prov.findAll();
        
    }
    
    public static List<Piezas> findAllPiezas(){  
            
        PiezasJpaController c_piezas = new PiezasJpaController();

        return c_piezas.findAll();
        
    }  
    
    public static List<Proyectos> findAllProyectos(){  
            
        ProyectosJpaController c_proyectos = new ProyectosJpaController();

        return c_proyectos.findAll();
        
    }  
    
    public static List<Proveedores> findProveedorByFiltro(int select,  String filtro){
    
         if(filtro.equals("")){
             
            return findAllProveedores();
            
        }else{
             
            ProveedoresJpaController c_prov = new ProveedoresJpaController();        
            
            switch(select){
                
                case 0:
                    
                      return c_prov.findByCodigoLike(filtro);
                    
                case 1:
                    
                      return c_prov.findByNombreLike(filtro);
                    
                case 2:
                    
                      return c_prov.findByDireccionLike(filtro);
                    
                default:
                    
                    return null;
                    
            }
            
        }
         
    }
    
    public static List<Piezas> findPiezaByFiltro(int select,  String filtro){
    
         if(filtro.equals("")){
             
            return findAllPiezas();
            
        }else{
             
            PiezasJpaController c_piezas = new PiezasJpaController();        
            
            switch(select){
                
                case 0:
                    
                      return c_piezas.findByCodigoLike(filtro);
                    
                case 1:
                    
                      return c_piezas.findByNombreLike(filtro);         
                    
                default:
                    
                    return null;
                    
            }
            
        }
         
    }
    
    public static List<Proyectos> findProyectoByFiltro(int select,  String filtro){
    
         if(filtro.equals("")){
             
            return findAllProyectos();
            
        }else{
             
            ProyectosJpaController c_proyectos = new ProyectosJpaController();        
            
            switch(select){
                
                case 0:
                    
                      return c_proyectos.findByCodigoLike(filtro);
                    
                case 1:
                    
                      return c_proyectos.findByNombreLike(filtro);  
                
                case 2:
                    
                      return c_proyectos.findByCiudadLike(filtro);   
                    
                default:
                    
                    return null;
                    
            }
            
        }
         
    }
    
    public static boolean insertOrUpdateProveedor(Proveedores p){
            
        ProveedoresJpaController c_prov = new ProveedoresJpaController();
        
        c_prov.createUpdate(p);
        
        return true;
        
    }
    
    public static boolean insertPieza(Piezas p) throws Exception{
            
        PiezasJpaController c_piezas = new PiezasJpaController();
        
        c_piezas.create(p);
        
        return true;
        
    }
    
    public static boolean updatePieza(Piezas p) throws Exception{
            
        PiezasJpaController c_piezas = new PiezasJpaController();
        
        c_piezas.edit(p);
        
        return true;
        
    }
    
    public static boolean insertOrUpdateProyecto(Proyectos p) throws Exception{
            
        ProyectosJpaController c_proyectos = new ProyectosJpaController();
        
        c_proyectos.create(p);
        
        return true;
        
    }
    
    public static boolean deleteProveedor(Proveedores p){
            
        ProveedoresJpaController c_prov = new ProveedoresJpaController();
        
        c_prov.delete(p);
        
        return true;
        
    }
    
    public static boolean deletePieza(Piezas p) throws IllegalOrphanException, NonexistentEntityException{
            
        PiezasJpaController c_pieza = new PiezasJpaController();
        
        c_pieza.destroy(p.getCodigo());
        
        return true;
        
    }
    
    public static boolean deleteProyecto(Proyectos p) throws IllegalOrphanException, NonexistentEntityException{
            
        ProyectosJpaController c_proyectos = new ProyectosJpaController();
        
        c_proyectos.destroy(p.getCodigo());
        
        return true;
        
    }
    
}
