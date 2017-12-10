/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hibernate_ad;

import com.sun.deploy.uitoolkit.impl.fx.ui.MixedCodeInSwing;
import controlador.ProveedoresJpaController;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Proveedores;
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
        
        vGestionProyectos = new VistaGestionProyectos(findAllProveedores());
        
        vGestionProyectos.setVisible(true);
        
    }
    
    public static List<Proveedores> findAllProveedores(){  
            
        ProveedoresJpaController c_prov = new ProveedoresJpaController();

        return c_prov.findAll();
        
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
    
    public static boolean insertOrUpdateProveedor(Proveedores p){
            
        ProveedoresJpaController c_prov = new ProveedoresJpaController();
        
        c_prov.createUpdate(p);
        
        return true;
        
    }
    
    public static boolean deleteProveedor(Proveedores p){
            
        ProveedoresJpaController c_prov = new ProveedoresJpaController();
        
        c_prov.delete(p);
        
        return true;
        
    }
    
}
