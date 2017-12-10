/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hibernate_ad;

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
        lanzarInterface();        
    }
    
    public static void lanzarInterface(){
        
        vGestionProyectos = new VistaGestionProyectos(true);
        
        vGestionProyectos.setVisible(true);
        
    }
    
}
