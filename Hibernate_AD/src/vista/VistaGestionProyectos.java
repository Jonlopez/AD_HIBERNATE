/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import controlador.ProveedoresJpaController;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import hibernate_ad.Hibernate_AD;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import modelo.Gestion;
import modelo.GestionPK;
import modelo.Piezas;
import modelo.Proveedores;
import modelo.Proyectos;

/**
 *
 * @author Jon
 */
public class VistaGestionProyectos extends javax.swing.JFrame {

    /**
     * Creates new form vistaGestionProyectos
     */
    public VistaGestionProyectos() {
        initComponents();
    }
    
    /**
     * Variables globales para control de proveedores
     */
    private DefaultTableModel dtm_prov; //Modelo de la tabla de proveedores
    private List<Proveedores>arr_proveedores = null; //Proveedores
    private int nav_prov = 0; //Indice de (arr_proveedores), para saber con que proveedor vamos a trabajar
    private Proveedores mod_prov = null; //Proveedor encontrado a la hora de insertar uno nuevo, posibilidad de modificarlo o eliminarlo
    private final int MAX_PROV_COD = 6;
    private final int MAX_PROV_NOMBRE = 20;
    private final int MAX_PROV_APELLIDO = 30;
    private final int MAX_PROV_DIR = 40;
    
    /**
    * Variables globales para control de piezas
    */
    private DefaultTableModel dtm_piezas; //Modelo de la tabla de piezas
    private List<Piezas>arr_piezas = null; //Piezas
    private int nav_pieza = 0; //Indice de (arr_piezas), para saber con que pieza vamos a trabajar
    private Piezas mod_pieza = null; //Pieza encontrada a la hora de insertar una nueva, posibilidad de modificarla o eliminarla
    private final int MAX_PIEZA_COD = 6;
    private final int MAX_PIEZA_NOMBRE = 20;
    private final int MAX_PIEZA_PRECIO = 30;
    private final int MAX_PIEZA_DESC = 40;
    
    /**
    * Variables globales para control de proyectos
    */
    private DefaultTableModel dtm_proyectos; //Modelo de la tabla de proyectos
    private List<Proyectos>arr_proyectos = null; //proyectos
    private int nav_proyecto = 0; //Indice de (arr_proyectos), para saber con que proyecto vamos a trabajar
    private Proyectos mod_proyecto = null; //Proyecto encontrado a la hora de insertar uno nuevo, posibilidad de modificarlo o eliminarlo
    private final int MAX_PROYECTO_COD = 6;
    private final int MAX_PROYECTO_NOMBRE = 20;
    private final int MAX_PROYECTO_CIUDAD = 30;   
    
    /**
    * Variables globales para control de gestion global
    */
    
    private DefaultTableModel dtm_gestion; //Modelo de la tabla de gestiones globales
    private List<Gestion>arr_gestiones = null; //gestiones
    private Gestion mod_gestion = null; //Proyecto encontrado a la hora de insertar uno nuevo, posibilidad de modificarlo o eliminarlo
    private final int MAX_GG_CANTIDAD = 6;
    
    
    public VistaGestionProyectos
        (
                List<Proveedores>  arr_proveedores,
                List<Piezas>  arr_piezas,
                List<Proyectos>  arr_proyectos
        ) {
        
        initComponents();
        
        this.setLocationRelativeTo(this); 
        
        /*PROVEEDORES*/
        this.arr_proveedores = arr_proveedores;
        
        dtm_prov = (DefaultTableModel)jTable_prov.getModel();
        
        jTable_prov.setModel(dtm_prov);
        
        /*PIEZAS*/
        this.arr_piezas = arr_piezas;
        
        dtm_piezas = (DefaultTableModel)jTable_pieza.getModel();
        
        jTable_pieza.setModel(dtm_piezas);
        
        /*PROYECTOS*/
        this.arr_proyectos = arr_proyectos;
        
        dtm_proyectos = (DefaultTableModel)jTable_proyecto.getModel();
        
        jTable_proyecto.setModel(dtm_proyectos);
        
        /*GESTIONES GLOBALES*/        
        
        dtm_gestion = (DefaultTableModel)jTable_gG.getModel();
        
        jTable_gG.setModel(dtm_gestion);
        
    }
    
    /**
     * Metodos personalizados para proveedores
    */
    private void pintarProveedorListado(Proveedores cur_prov){
        
        jT_prov_cod_02.setText(cur_prov.getCodigo());
        
        jT_prov_nombre_02.setText(cur_prov.getNombre());
        
        jT_prov_apellidos_02.setText(cur_prov.getApellidos());
        
        jT_prov_dir_02.setText(cur_prov.getDireccion());
        
    }
    
    private void pintarProveedorTabla(Proveedores cur_prov){
        
        String fila[] = new String [4];
        
            fila [0] = (cur_prov.getCodigo());

            fila [1] = (cur_prov.getNombre());

            fila [2] = (cur_prov.getApellidos());

            fila [3] = (cur_prov.getDireccion()); 
        
        dtm_prov.addRow(fila);
        
    }
    
    private void limpiarProveedor(){
        
        jT_prov_cod_01.setText("");
        
        jT_prov_nombre_01.setText("");
        
        jT_prov_apellidos_01.setText("");
        
        jT_prov_dir_01.setText("");
        
        jT_prov_sms_error.setText("");
        
        jB_prov_insertar.setEnabled(false);
        
        jB_prov_modificar.setEnabled(false);
        
        jB_prov_eliminar_01.setEnabled(false);
        
    }
    
    
    private boolean validacionesProveedor(){
        
        boolean valido = false;        
        
        if(jT_prov_cod_01.getText().equals("")
                || jT_prov_nombre_01.getText().equals("")
                || jT_prov_apellidos_01.getText().equals("")
                || jT_prov_dir_01.getText().equals("")){
            
            jT_prov_sms_error.setText(" Revisa los campos, todos son obligatorios");                    
            
        }else
            valido = true;
        
        
        return valido;
        
    }
    
    private Proveedores rellenarProveedor(String cod_mod){
        
        if(validacionesProveedor()){
            
            String codigo = (cod_mod != null)?cod_mod:((jT_prov_cod_01.getText().length() > MAX_PROV_COD)?jT_prov_cod_01.getText().substring(0, MAX_PROV_COD):jT_prov_cod_01.getText()).toUpperCase();
        
            String nombre = ((jT_prov_nombre_01.getText().length() > MAX_PROV_NOMBRE)?jT_prov_nombre_01.getText().substring(0, MAX_PROV_NOMBRE):jT_prov_nombre_01.getText());

            String apellidos = ((jT_prov_apellidos_01.getText().length() > MAX_PROV_APELLIDO)?jT_prov_apellidos_01.getText().substring(0, MAX_PROV_APELLIDO):jT_prov_apellidos_01.getText());

            String direccion = ((jT_prov_dir_01.getText().length() > MAX_PROV_DIR)?jT_prov_dir_01.getText().substring(0, MAX_PROV_DIR):jT_prov_dir_01.getText());

            return new Proveedores(codigo, nombre, apellidos, direccion);  
            
        }else
            
            return null;
        
    }
    
    /**
     * Metodos personalizados para piezas
     */
    
    private void pintarPiezaListado(Piezas cur_pieza){
        
        jT_pieza_cod_02.setText(cur_pieza.getCodigo());
        
        jT_pieza_nombre_02.setText(cur_pieza.getNombre());        
        
        jT_pieza_precio_02.setText(((Float) cur_pieza.getPrecio()).toString());
        
        jT_pieza_desc_02.setText(cur_pieza.getDescripcion());
        
    }
    
    private void pintarPiezaTabla(Piezas cur_pieza){
        
        String fila[] = new String [4];
        
            fila [0] = (cur_pieza.getCodigo());

            fila [1] = (cur_pieza.getNombre());

            fila [2] = (((Float) cur_pieza.getPrecio()).toString());

            fila [3] = (cur_pieza.getDescripcion()); 
        
        dtm_piezas.addRow(fila);
        
    }
    
    private void limpiarPieza(){
        
        jT_pieza_cod_01.setText("");
        
        jT_pieza_nombre_01.setText("");
        
        jT_pieza_precio_01.setText("");
        
        jT_pieza_desc_01.setText("");
        
        jT_pieza_sms_error.setText("");
        
        jB_pieza_insertar.setEnabled(false);
        
        jB_pieza_modificar.setEnabled(false);
        
        jB_pieza_eliminar_01.setEnabled(false);
        
    }
    
    
    private boolean validacionesPieza(){
        
        boolean valido = false;        
        
        try{
        
            if(jT_pieza_cod_01.getText().equals("")
                || jT_pieza_nombre_01.getText().equals("")
                || jT_pieza_precio_01.getText().equals("")
                || jT_pieza_desc_01.getText().equals("")){

                jT_pieza_sms_error.setText(" Revisa los campos, todos son obligatorios");                    

            }else
                if(Float.parseFloat(jT_pieza_precio_01.getText()) > 0)
                
                    valido = true;
                
        
        }catch(NumberFormatException ex){                
                
            JOptionPane.showMessageDialog(null, "El precio no es valido");    
                
        }
        
        return valido;
        
    }
    
    private Piezas rellenarPieza(String cod_mod){
        
        if(validacionesPieza()){
                 
            String codigo = (cod_mod != null)?cod_mod:((jT_pieza_cod_01.getText().length() > MAX_PROV_COD)?jT_pieza_cod_01.getText().substring(0, MAX_PROV_COD):jT_pieza_cod_01.getText()).toUpperCase();

            String nombre = ((jT_pieza_nombre_01.getText().length() > MAX_PROV_NOMBRE)?jT_pieza_nombre_01.getText().substring(0, MAX_PROV_NOMBRE):jT_pieza_nombre_01.getText());

            Float precio = (Float) Float.parseFloat((jT_pieza_precio_01.getText().length() > MAX_PROV_APELLIDO)?jT_pieza_precio_01.getText().substring(0, MAX_PROV_APELLIDO):jT_pieza_precio_01.getText());                

            String descripcion = ((jT_pieza_desc_01.getText().length() > MAX_PROV_DIR)?jT_pieza_desc_01.getText().substring(0, MAX_PROV_DIR):jT_pieza_desc_01.getText());

            Piezas pieza = new Piezas(codigo, nombre, precio);

            pieza.setDescripcion(descripcion);

            return pieza;  
            
        }else
            
            return null;
        
    }    
   
     /**
     * Metodos personalizados para proyectos
     */
    
    private void pintarProyectoListado(Proyectos cur_proyecto){
        
        jT_proyecto_cod_02.setText(cur_proyecto.getCodigo());
        
        jT_proyecto_nombre_02.setText(cur_proyecto.getNombre());        
        
        jT_proyecto_ciudad_02.setText(cur_proyecto.getCiudad());
        
    }
    
    private void pintarProyectoTabla(Proyectos cur_proyecto){
        
        String fila[] = new String [3];
        
            fila [0] = (cur_proyecto.getCodigo());

            fila [1] = (cur_proyecto.getNombre());           

            fila [2] = (cur_proyecto.getCiudad()); 
        
        dtm_proyectos.addRow(fila);
        
    }
    
    private void limpiarProyecto(){
        
        jT_proyecto_cod_01.setText("");
        
        jT_proyecto_nombre_01.setText("");
        
        jT_proyecto_ciudad_01.setText("");
        
        jT_proyecto_sms_error.setText("");
        
        jB_proyecto_insertar.setEnabled(false);
        
        jB_proyecto_modificar.setEnabled(false);
        
        jB_proyecto_eliminar_01.setEnabled(false);
        
    }    
    
    private boolean validacionesProyecto(){
        
        boolean valido = false;        
        
        if(jT_proyecto_cod_01.getText().equals("")
            || jT_proyecto_nombre_01.getText().equals("")
            || jT_proyecto_ciudad_01.getText().equals("")){
            
            jT_proyecto_sms_error.setText(" Revisa los campos, todos son obligatorios");                    
            
        }else
            valido = true;
        
        return valido;
        
    }
    
    private Proyectos rellenarProyecto(String cod_mod){
        
        if(validacionesProyecto()){
                 
            String codigo = (cod_mod != null)?cod_mod:((jT_proyecto_cod_01.getText().length() > MAX_PROYECTO_COD)?jT_proyecto_cod_01.getText().substring(0, MAX_PROV_COD):jT_proyecto_cod_01.getText()).toUpperCase();

            String nombre = ((jT_proyecto_nombre_01.getText().length() > MAX_PROYECTO_NOMBRE)?jT_proyecto_nombre_01.getText().substring(0, MAX_PROYECTO_NOMBRE):jT_proyecto_nombre_01.getText());

            String ciudad = ((jT_proyecto_ciudad_01.getText().length() > MAX_PROYECTO_CIUDAD)?jT_proyecto_ciudad_01.getText().substring(0, MAX_PROYECTO_CIUDAD):jT_proyecto_ciudad_01.getText());

            Proyectos proyecto = new Proyectos(codigo, nombre, ciudad);          

            return proyecto;  
            
        }else
            
            return null;
        
    }
    
    /**
     * Metodos personalizados para gestion global
     */    
       
    
    private void pintarGestionTabla(Gestion cur_gestion){
        
        String fila[] = new String [4];
        
            fila [0] = (cur_gestion.getGestionPK().getCodproveedor().toString());

            fila [1] = (cur_gestion.getGestionPK().getCodpieza().toString());            

            fila [2] = (cur_gestion.getGestionPK().getCodproyecto().toString()); 
            
            fila [3] = (((Float) cur_gestion.getCantidad()).toString().toString());
        
        dtm_gestion.addRow(fila);
        
    }
    
    private void limpiarGestion(){
        
        jCombo_gG_cod_prov.setSelectedIndex(0);
        
        jCombo_gG_cod_pieza.setSelectedIndex(0);
        
        jCombo_gG_cod_proyecto.setSelectedIndex(0);
                        
        jT_gG_proveedor.setText("");
        
        jT_gG_pieza.setText("");
        
        jT_gG_proyecto.setText("");
        
        jT_gG_cantidad.setText("");
        
        jT_gG_sms_error1.setText("");
        
        jB_gG_insertar1.setEnabled(false);
        
        jB_gG_modificar1.setEnabled(false);
        
        jB_gG_eliminar_2.setEnabled(false);
        
    }    
    
    private boolean validacionesGestion(){
        
        boolean valido = false;   
        
        try{
            
            if(jT_gG_cantidad.getText().equals("")
            || jT_gG_cantidad.getText().equals("")
            || jT_gG_cantidad.getText().equals("")
            || jT_gG_cantidad.getText().equals("")){
            
            jT_gG_sms_error1.setText(" Revisa los campos, todos son obligatorios");                    
            
            }else 
                if(Float.parseFloat(jT_gG_cantidad.getText()) > 0)
                
                    valido = true;
            
        }catch(NumberFormatException ex){                
                
            JOptionPane.showMessageDialog(null, "La cantidad no es valida");    

        }
        
        return valido;
        
    }
        
    private Gestion rellenarGestion(GestionPK gestionPk){
        
        if(validacionesGestion()){
            
            if(gestionPk == null){
                
                String cod_prov = jCombo_gG_cod_prov.getSelectedItem().toString();

                String cod_pieza = jCombo_gG_cod_pieza.getSelectedItem().toString();

                String cod_proyecto = jCombo_gG_cod_proyecto.getSelectedItem().toString();                

                gestionPk = new GestionPK(cod_prov, cod_pieza, cod_proyecto);
                
            }
            
            Float cantidad = (Float) Float.parseFloat(jT_gG_cantidad.getText());    

            Gestion gestion = new Gestion(gestionPk, cantidad);    

            return gestion;  
            
        }else
            
            return null;
        
    }
    
    private void exiteGestion(){     
               
        if(jCombo_gG_cod_prov.getSelectedIndex() > 0
            && jCombo_gG_cod_pieza.getSelectedIndex() > 0
            && jCombo_gG_cod_proyecto.getSelectedIndex() > 0){
            
            GestionPK gestionPK = 
                    new GestionPK(
                            jCombo_gG_cod_prov.getSelectedItem().toString(),
                            jCombo_gG_cod_pieza.getSelectedItem().toString(),
                            jCombo_gG_cod_proyecto.getSelectedItem().toString()
                    );
            
            arr_gestiones = Hibernate_AD.findGestionByFiltro(0, gestionPK);   
            
            if(arr_gestiones != null && arr_gestiones.size() > 0){
          
              jB_gG_insertar1.setEnabled(false);
              
              jB_gG_modificar1.setEnabled(true);
              
              jB_gG_eliminar_2.setEnabled(true);
              
              jT_gG_cantidad.setText(((Float) arr_gestiones.get(0).getCantidad()).toString());
              
              mod_gestion = arr_gestiones.get(0);
                
            }else{        
                
                jB_gG_insertar1.setEnabled(true);
                
                jB_gG_modificar1.setEnabled(false);
                
                jB_gG_eliminar_2.setEnabled(false);
                
                mod_gestion = null;
                
            }
            
        }else{
            
            mod_gestion = null;
            
            jB_gG_insertar1.setEnabled(false);
              
            jB_gG_modificar1.setEnabled(false);
              
            jB_gG_eliminar_2.setEnabled(false);
              
            jT_gG_cantidad.setText("");
            
        }
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        JT_pManager = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jT_proveedores = new javax.swing.JTabbedPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        label2 = new java.awt.Label();
        label3 = new java.awt.Label();
        label4 = new java.awt.Label();
        label5 = new java.awt.Label();
        jT_prov_cod_01 = new javax.swing.JTextField();
        jT_prov_nombre_01 = new javax.swing.JTextField();
        jT_prov_apellidos_01 = new javax.swing.JTextField();
        jT_prov_dir_01 = new javax.swing.JTextField();
        jB_prov_limpiar = new javax.swing.JButton();
        jB_prov_eliminar_01 = new javax.swing.JButton();
        jB_prov_modificar = new javax.swing.JButton();
        jB_prov_insertar = new javax.swing.JButton();
        jL_prov_nombre_err = new javax.swing.JLabel();
        jL_prov_cod_err = new javax.swing.JLabel();
        jL_prov_apellidos_err = new javax.swing.JLabel();
        jL_prov_dir_err = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jT_prov_sms_error = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        label6 = new java.awt.Label();
        label7 = new java.awt.Label();
        label8 = new java.awt.Label();
        label9 = new java.awt.Label();
        jB_prov_eliminar_02 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jB_prov_ant = new javax.swing.JButton();
        jB_prov_sigui = new javax.swing.JButton();
        jB_prov_final = new javax.swing.JButton();
        jB_prov_inicio = new javax.swing.JButton();
        jB_prov_eject_consulta = new javax.swing.JButton();
        jT_prov_nombre_02 = new java.awt.Label();
        jT_prov_cod_02 = new java.awt.Label();
        jT_prov_dir_02 = new java.awt.Label();
        jT_prov_apellidos_02 = new java.awt.Label();
        jT_prov_min = new java.awt.Label();
        jT_prov_max = new javax.swing.JLabel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jComboBox_prov_filtro = new javax.swing.JComboBox();
        jT_prov_filtro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_prov = new javax.swing.JTable();
        label1 = new java.awt.Label();
        jB_prov_filtro_ejecutar = new javax.swing.JButton();
        jT_piezas = new javax.swing.JTabbedPane();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        label26 = new java.awt.Label();
        label27 = new java.awt.Label();
        label28 = new java.awt.Label();
        label29 = new java.awt.Label();
        jT_pieza_cod_01 = new javax.swing.JTextField();
        jT_pieza_nombre_01 = new javax.swing.JTextField();
        jT_pieza_precio_01 = new javax.swing.JTextField();
        jT_pieza_desc_01 = new javax.swing.JTextField();
        jB_pieza_limpiar1 = new javax.swing.JButton();
        jB_pieza_eliminar_01 = new javax.swing.JButton();
        jB_pieza_modificar = new javax.swing.JButton();
        jB_pieza_insertar = new javax.swing.JButton();
        jL_prov_nombre_err1 = new javax.swing.JLabel();
        jL_prov_cod_err1 = new javax.swing.JLabel();
        jL_prov_apellidos_err1 = new javax.swing.JLabel();
        jL_prov_dir_err1 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jT_pieza_sms_error = new javax.swing.JTextArea();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        label30 = new java.awt.Label();
        label31 = new java.awt.Label();
        label32 = new java.awt.Label();
        label33 = new java.awt.Label();
        jB_pieza_eliminar_02 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jB_pieza_ant = new javax.swing.JButton();
        jB_pieza_sigui = new javax.swing.JButton();
        jB_pieza_final = new javax.swing.JButton();
        jB_pieza_inicio = new javax.swing.JButton();
        jB_pieza_eject_consulta = new javax.swing.JButton();
        jT_pieza_nombre_02 = new java.awt.Label();
        jT_pieza_cod_02 = new java.awt.Label();
        jT_pieza_desc_02 = new java.awt.Label();
        jT_pieza_precio_02 = new java.awt.Label();
        jT_pieza_min = new java.awt.Label();
        jT_pieza_max = new javax.swing.JLabel();
        jTabbedPane6 = new javax.swing.JTabbedPane();
        jPanel24 = new javax.swing.JPanel();
        jComboBox_pieza_filtro = new javax.swing.JComboBox();
        jT_pieza_filtro = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable_pieza = new javax.swing.JTable();
        label42 = new java.awt.Label();
        jB_pieza_filtro_ejecutar1 = new javax.swing.JButton();
        jT_proyectos = new javax.swing.JTabbedPane();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel19 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        label34 = new java.awt.Label();
        label35 = new java.awt.Label();
        label36 = new java.awt.Label();
        jT_proyecto_cod_01 = new javax.swing.JTextField();
        jT_proyecto_nombre_01 = new javax.swing.JTextField();
        jT_proyecto_ciudad_01 = new javax.swing.JTextField();
        jB_proyecto_limpiar = new javax.swing.JButton();
        jB_proyecto_eliminar_01 = new javax.swing.JButton();
        jB_proyecto_modificar = new javax.swing.JButton();
        jB_proyecto_insertar = new javax.swing.JButton();
        jL_prov_nombre_err2 = new javax.swing.JLabel();
        jL_prov_cod_err2 = new javax.swing.JLabel();
        jL_prov_apellidos_err2 = new javax.swing.JLabel();
        jL_prov_dir_err2 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jT_proyecto_sms_error = new javax.swing.JTextArea();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        label38 = new java.awt.Label();
        label39 = new java.awt.Label();
        label40 = new java.awt.Label();
        jB_proyecto_eliminar_02 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jB_proyecto_ant = new javax.swing.JButton();
        jB_proyecto_sigui = new javax.swing.JButton();
        jB_proyecto_final = new javax.swing.JButton();
        jB_proyecto_inicio = new javax.swing.JButton();
        jB_proyecto_eject_consulta = new javax.swing.JButton();
        jT_proyecto_nombre_02 = new java.awt.Label();
        jT_proyecto_cod_02 = new java.awt.Label();
        jT_proyecto_ciudad_02 = new java.awt.Label();
        jT_proyecto_min = new java.awt.Label();
        jT_proyecto_max = new javax.swing.JLabel();
        jTabbedPane7 = new javax.swing.JTabbedPane();
        jPanel25 = new javax.swing.JPanel();
        jComboBox_proyecto_filtro = new javax.swing.JComboBox();
        jT_proyecto_filtro = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable_proyecto = new javax.swing.JTable();
        label43 = new java.awt.Label();
        jB_proyecto_filtro_ejecutar = new javax.swing.JButton();
        jT_gestionGlobal = new javax.swing.JTabbedPane();
        jTabbedPane8 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        label37 = new java.awt.Label();
        label41 = new java.awt.Label();
        label44 = new java.awt.Label();
        jB_gG_limpiar1 = new javax.swing.JButton();
        jB_gG_eliminar_2 = new javax.swing.JButton();
        jB_gG_modificar1 = new javax.swing.JButton();
        jB_gG_insertar1 = new javax.swing.JButton();
        jL_prov_dir_err3 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jT_gG_sms_error1 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jT_gG_cantidad = new javax.swing.JTextField();
        jCombo_gG_cod_prov = new javax.swing.JComboBox<String>();
        jCombo_gG_cod_pieza = new javax.swing.JComboBox<String>();
        jCombo_gG_cod_proyecto = new javax.swing.JComboBox<String>();
        jB_gG_recarga = new javax.swing.JButton();
        jT_gG_proveedor = new java.awt.Label();
        jT_gG_pieza = new java.awt.Label();
        jT_gG_proyecto = new java.awt.Label();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable_gG = new javax.swing.JTable();
        jB_proyecto_gG_ejecutar1 = new javax.swing.JButton();
        jTabbedPane9 = new javax.swing.JTabbedPane();
        jTabbedPane10 = new javax.swing.JTabbedPane();
        jTabbedPane11 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestión de Proyectos");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/images/fondo.PNG"))); // NOI18N

        javax.swing.GroupLayout JT_pManagerLayout = new javax.swing.GroupLayout(JT_pManager);
        JT_pManager.setLayout(JT_pManagerLayout);
        JT_pManagerLayout.setHorizontalGroup(
            JT_pManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
        );
        JT_pManagerLayout.setVerticalGroup(
            JT_pManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("GestioGram", JT_pManager);

        jT_proveedores.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jLabel1.setText("ALTAS BAJAS Y MODIFICACIONES");

        label2.setText("Código de proveedor");

        label3.setText("Nombre");

        label4.setText("Apellidos");

        label5.setText("Dirección");

        jT_prov_cod_01.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jT_prov_cod_01KeyReleased(evt);
            }
        });

        jB_prov_limpiar.setText("Limpiar");
        jB_prov_limpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_prov_limpiarMouseClicked(evt);
            }
        });

        jB_prov_eliminar_01.setText("Eliminar");
        jB_prov_eliminar_01.setEnabled(false);
        jB_prov_eliminar_01.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_prov_eliminar_01MouseClicked(evt);
            }
        });

        jB_prov_modificar.setText("Modificar");
        jB_prov_modificar.setEnabled(false);
        jB_prov_modificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_prov_modificarMouseClicked(evt);
            }
        });

        jB_prov_insertar.setText("Insertar");
        jB_prov_insertar.setEnabled(false);
        jB_prov_insertar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_prov_insertarMouseClicked(evt);
            }
        });

        jL_prov_nombre_err.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_nombre_err.setText("*");

        jL_prov_cod_err.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_cod_err.setText("*");

        jL_prov_apellidos_err.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_apellidos_err.setText("*");

        jL_prov_dir_err.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_dir_err.setText("*");

        jT_prov_sms_error.setEditable(false);
        jT_prov_sms_error.setBackground(new java.awt.Color(240, 240, 240));
        jT_prov_sms_error.setColumns(20);
        jT_prov_sms_error.setForeground(new java.awt.Color(204, 0, 51));
        jT_prov_sms_error.setRows(5);
        jT_prov_sms_error.setBorder(null);
        jScrollPane4.setViewportView(jT_prov_sms_error);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(114, 114, 114)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jB_prov_insertar)
                                    .addGap(18, 18, 18)
                                    .addComponent(jB_prov_modificar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jB_prov_eliminar_01))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(49, 49, 49)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jT_prov_cod_01)
                                            .addComponent(jT_prov_apellidos_01)
                                            .addComponent(jT_prov_dir_01, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jT_prov_nombre_01, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jB_prov_limpiar, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jL_prov_nombre_err)
                                .addComponent(jL_prov_cod_err)
                                .addComponent(jL_prov_apellidos_err)
                                .addComponent(jL_prov_dir_err)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addComponent(jLabel1))))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addComponent(jB_prov_limpiar)
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jT_prov_dir_01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jT_prov_cod_01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jL_prov_cod_err)
                                        .addGap(26, 26, 26)
                                        .addComponent(jL_prov_nombre_err)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jL_prov_apellidos_err))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jT_prov_nombre_01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jT_prov_apellidos_01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jL_prov_dir_err)))))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jB_prov_eliminar_01)
                    .addComponent(jB_prov_modificar)
                    .addComponent(jB_prov_insertar))
                .addGap(63, 63, 63))
        );

        jTabbedPane2.addTab("Gestion", jPanel2);

        jLabel2.setText("LISTADO DE PROVEEDORES");

        label6.setText("Código de proveedor");

        label7.setText("Nombre");

        label8.setText("Apellidos");

        label9.setText("Dirección");

        jB_prov_eliminar_02.setText("Eliminar");
        jB_prov_eliminar_02.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_prov_eliminar_02MouseClicked(evt);
            }
        });

        jLabel3.setText("//");

        jB_prov_ant.setText("<<");
        jB_prov_ant.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_prov_antMouseClicked(evt);
            }
        });

        jB_prov_sigui.setText(">>");
        jB_prov_sigui.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_prov_siguiMouseClicked(evt);
            }
        });

        jB_prov_final.setText(">>|");
        jB_prov_final.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_prov_finalMouseClicked(evt);
            }
        });

        jB_prov_inicio.setText("|<<");
        jB_prov_inicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_prov_inicioMouseClicked(evt);
            }
        });

        jB_prov_eject_consulta.setText("Ejecutar consulta");
        jB_prov_eject_consulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_prov_eject_consultaMouseClicked(evt);
            }
        });

        jT_prov_nombre_02.setBackground(new java.awt.Color(255, 255, 255));

        jT_prov_cod_02.setBackground(new java.awt.Color(255, 255, 255));

        jT_prov_dir_02.setBackground(new java.awt.Color(255, 255, 255));

        jT_prov_apellidos_02.setBackground(new java.awt.Color(255, 255, 255));

        jT_prov_min.setText("0");

        jT_prov_max.setText("000");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jB_prov_eject_consulta))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jB_prov_inicio)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jB_prov_ant)
                                        .addGap(24, 24, 24)
                                        .addComponent(jT_prov_min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jT_prov_max, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jB_prov_sigui)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jB_prov_final)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                                        .addComponent(jB_prov_eliminar_02))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(49, 49, 49)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jT_prov_nombre_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jT_prov_cod_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jT_prov_dir_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jT_prov_apellidos_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))))
                .addGap(168, 168, 168))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addGap(23, 23, 23)
                .addComponent(jB_prov_eject_consulta)
                .addGap(52, 52, 52)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jT_prov_nombre_02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jT_prov_apellidos_02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jT_prov_cod_02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jT_prov_dir_02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jT_prov_min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jB_prov_eliminar_02)
                        .addComponent(jB_prov_ant)
                        .addComponent(jB_prov_sigui)
                        .addComponent(jB_prov_final)
                        .addComponent(jB_prov_inicio)
                        .addComponent(jLabel3)
                        .addComponent(jT_prov_max)))
                .addGap(53, 53, 53))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Listado", jPanel4);

        jT_proveedores.addTab("Gestión de proveedores", jTabbedPane2);

        jComboBox_prov_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Por código", "Por nombre", "Por dirección" }));
        jComboBox_prov_filtro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_prov_filtroItemStateChanged(evt);
            }
        });

        jT_prov_filtro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jT_prov_filtroKeyReleased(evt);
            }
        });

        jTable_prov.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Apellidos", "Dirección"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable_prov);

        label1.setForeground(new java.awt.Color(204, 0, 51));
        label1.setText("*");

        jB_prov_filtro_ejecutar.setText("Ejecutar");
        jB_prov_filtro_ejecutar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_prov_filtro_ejecutarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jComboBox_prov_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jT_prov_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jB_prov_filtro_ejecutar)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jB_prov_filtro_ejecutar)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox_prov_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jT_prov_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Filtro de proveedores", jPanel8);

        jT_proveedores.addTab("Consulta de proveedores", jTabbedPane3);

        jTabbedPane1.addTab("Proveedores", jT_proveedores);

        jT_piezas.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jLabel11.setText("ALTAS BAJAS Y MODIFICACIONES");

        label26.setText("Código de pieza");

        label27.setText("Nombre");

        label28.setText("Precio");

        label29.setText("Descripción");

        jT_pieza_cod_01.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jT_pieza_cod_01KeyReleased(evt);
            }
        });

        jB_pieza_limpiar1.setText("Limpiar");
        jB_pieza_limpiar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_pieza_limpiar1MouseClicked(evt);
            }
        });

        jB_pieza_eliminar_01.setText("Eliminar");
        jB_pieza_eliminar_01.setEnabled(false);
        jB_pieza_eliminar_01.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_pieza_eliminar_01MouseClicked(evt);
            }
        });

        jB_pieza_modificar.setText("Modificar");
        jB_pieza_modificar.setEnabled(false);
        jB_pieza_modificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_pieza_modificarMouseClicked(evt);
            }
        });

        jB_pieza_insertar.setText("Insertar");
        jB_pieza_insertar.setEnabled(false);
        jB_pieza_insertar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_pieza_insertarMouseClicked(evt);
            }
        });

        jL_prov_nombre_err1.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_nombre_err1.setText("*");

        jL_prov_cod_err1.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_cod_err1.setText("*");

        jL_prov_apellidos_err1.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_apellidos_err1.setText("*");

        jL_prov_dir_err1.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_dir_err1.setText("*");

        jT_pieza_sms_error.setEditable(false);
        jT_pieza_sms_error.setBackground(new java.awt.Color(240, 240, 240));
        jT_pieza_sms_error.setColumns(20);
        jT_pieza_sms_error.setForeground(new java.awt.Color(204, 0, 51));
        jT_pieza_sms_error.setRows(5);
        jT_pieza_sms_error.setBorder(null);
        jScrollPane5.setViewportView(jT_pieza_sms_error);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                            .addGap(114, 114, 114)
                            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel16Layout.createSequentialGroup()
                                    .addComponent(jB_pieza_insertar)
                                    .addGap(18, 18, 18)
                                    .addComponent(jB_pieza_modificar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jB_pieza_eliminar_01))
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(label26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(49, 49, 49)
                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jT_pieza_cod_01)
                                            .addComponent(jT_pieza_precio_01)
                                            .addComponent(jT_pieza_desc_01, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jT_pieza_nombre_01, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jB_pieza_limpiar1, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jL_prov_nombre_err1)
                                .addComponent(jL_prov_cod_err1)
                                .addComponent(jL_prov_apellidos_err1)
                                .addComponent(jL_prov_dir_err1)))
                        .addGroup(jPanel16Layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addComponent(jLabel11))))
                .addContainerGap(209, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel11)
                .addGap(28, 28, 28)
                .addComponent(jB_pieza_limpiar1)
                .addGap(41, 41, 41)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jT_pieza_desc_01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(label26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jT_pieza_cod_01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(label27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addComponent(jL_prov_cod_err1)
                                        .addGap(26, 26, 26)
                                        .addComponent(jL_prov_nombre_err1)))
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(label28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jL_prov_apellidos_err1))))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jT_pieza_nombre_01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jT_pieza_precio_01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(label29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jL_prov_dir_err1)))))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jB_pieza_eliminar_01)
                    .addComponent(jB_pieza_modificar)
                    .addComponent(jB_pieza_insertar))
                .addGap(63, 63, 63))
        );

        jTabbedPane4.addTab("Gestion", jPanel16);

        jLabel12.setText("LISTADO DE PIEZAS");

        label30.setText("Código de pieza");

        label31.setText("Nombre");

        label32.setText("Precio");

        label33.setText("Descripción");

        jB_pieza_eliminar_02.setText("Eliminar");
        jB_pieza_eliminar_02.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_pieza_eliminar_02MouseClicked(evt);
            }
        });

        jLabel13.setText("//");

        jB_pieza_ant.setText("<<");
        jB_pieza_ant.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_pieza_antMouseClicked(evt);
            }
        });

        jB_pieza_sigui.setText(">>");
        jB_pieza_sigui.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_pieza_siguiMouseClicked(evt);
            }
        });

        jB_pieza_final.setText(">>|");
        jB_pieza_final.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_pieza_finalMouseClicked(evt);
            }
        });

        jB_pieza_inicio.setText("|<<");
        jB_pieza_inicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_pieza_inicioMouseClicked(evt);
            }
        });

        jB_pieza_eject_consulta.setText("Ejecutar consulta");
        jB_pieza_eject_consulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_pieza_eject_consultaMouseClicked(evt);
            }
        });

        jT_pieza_nombre_02.setBackground(new java.awt.Color(255, 255, 255));

        jT_pieza_cod_02.setBackground(new java.awt.Color(255, 255, 255));

        jT_pieza_desc_02.setBackground(new java.awt.Color(255, 255, 255));

        jT_pieza_precio_02.setBackground(new java.awt.Color(255, 255, 255));
        jT_pieza_precio_02.setText("0.0");

        jT_pieza_min.setText("0");

        jT_pieza_max.setText("000");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jB_pieza_eject_consulta))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addComponent(jB_pieza_inicio)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jB_pieza_ant)
                                        .addGap(24, 24, 24)
                                        .addComponent(jT_pieza_min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jT_pieza_max, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jB_pieza_sigui)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jB_pieza_final)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                                        .addComponent(jB_pieza_eliminar_02))
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(label30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(49, 49, 49)
                                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jT_pieza_nombre_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jT_pieza_cod_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jT_pieza_desc_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jT_pieza_precio_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))))
                .addGap(168, 168, 168))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel12)
                .addGap(23, 23, 23)
                .addComponent(jB_pieza_eject_consulta)
                .addGap(52, 52, 52)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(label30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(label31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addComponent(label32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(jT_pieza_nombre_02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jT_pieza_precio_02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jT_pieza_cod_02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jT_pieza_desc_02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jT_pieza_min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jB_pieza_eliminar_02)
                        .addComponent(jB_pieza_ant)
                        .addComponent(jB_pieza_sigui)
                        .addComponent(jB_pieza_final)
                        .addComponent(jB_pieza_inicio)
                        .addComponent(jLabel13)
                        .addComponent(jT_pieza_max)))
                .addGap(53, 53, 53))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("Listado", jPanel17);

        jT_piezas.addTab("Gestión de piezas", jTabbedPane4);

        jComboBox_pieza_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Por código", "Por nombre" }));
        jComboBox_pieza_filtro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_pieza_filtroItemStateChanged(evt);
            }
        });

        jT_pieza_filtro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jT_pieza_filtroKeyReleased(evt);
            }
        });

        jTable_pieza.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Precio", "Descripción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable_pieza);

        label42.setForeground(new java.awt.Color(204, 0, 51));
        label42.setText("*");

        jB_pieza_filtro_ejecutar1.setText("Ejecutar");
        jB_pieza_filtro_ejecutar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_pieza_filtro_ejecutar1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jComboBox_pieza_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jT_pieza_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jB_pieza_filtro_ejecutar1)))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jB_pieza_filtro_ejecutar1)
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox_pieza_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jT_pieza_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(label42, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane6.addTab("Filtro de piezas", jPanel24);

        jT_piezas.addTab("Consulta de piezas", jTabbedPane6);

        jTabbedPane1.addTab("Piezas", jT_piezas);

        jT_proyectos.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jLabel14.setText("ALTAS BAJAS Y MODIFICACIONES");

        label34.setText("Código de proyecto");

        label35.setText("Nombre");

        label36.setText("Ciudad");

        jT_proyecto_cod_01.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jT_proyecto_cod_01KeyReleased(evt);
            }
        });

        jB_proyecto_limpiar.setText("Limpiar");
        jB_proyecto_limpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_limpiarMouseClicked(evt);
            }
        });

        jB_proyecto_eliminar_01.setText("Eliminar");
        jB_proyecto_eliminar_01.setEnabled(false);
        jB_proyecto_eliminar_01.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_eliminar_01MouseClicked(evt);
            }
        });

        jB_proyecto_modificar.setText("Modificar");
        jB_proyecto_modificar.setEnabled(false);
        jB_proyecto_modificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_modificarMouseClicked(evt);
            }
        });

        jB_proyecto_insertar.setText("Insertar");
        jB_proyecto_insertar.setEnabled(false);
        jB_proyecto_insertar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_insertarMouseClicked(evt);
            }
        });

        jL_prov_nombre_err2.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_nombre_err2.setText("*");

        jL_prov_cod_err2.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_cod_err2.setText("*");

        jL_prov_apellidos_err2.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_apellidos_err2.setText("*");

        jL_prov_dir_err2.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_dir_err2.setText("*");

        jT_proyecto_sms_error.setEditable(false);
        jT_proyecto_sms_error.setBackground(new java.awt.Color(240, 240, 240));
        jT_proyecto_sms_error.setColumns(20);
        jT_proyecto_sms_error.setForeground(new java.awt.Color(204, 0, 51));
        jT_proyecto_sms_error.setRows(5);
        jT_proyecto_sms_error.setBorder(null);
        jScrollPane6.setViewportView(jT_proyecto_sms_error);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                            .addGap(114, 114, 114)
                            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel19Layout.createSequentialGroup()
                                    .addComponent(jB_proyecto_insertar)
                                    .addGap(18, 18, 18)
                                    .addComponent(jB_proyecto_modificar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 245, Short.MAX_VALUE)
                                    .addComponent(jB_proyecto_eliminar_01))
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel19Layout.createSequentialGroup()
                                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(label34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(49, 49, 49)
                                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jT_proyecto_cod_01, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                                            .addComponent(jT_proyecto_ciudad_01)))
                                    .addComponent(jT_proyecto_nombre_01, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jB_proyecto_limpiar, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jL_prov_nombre_err2)
                                .addComponent(jL_prov_cod_err2)
                                .addComponent(jL_prov_apellidos_err2)
                                .addComponent(jL_prov_dir_err2)))
                        .addGroup(jPanel19Layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addComponent(jLabel14))))
                .addContainerGap(174, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel14)
                .addGap(28, 28, 28)
                .addComponent(jB_proyecto_limpiar)
                .addGap(41, 41, 41)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jT_proyecto_cod_01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(label35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addComponent(jL_prov_cod_err2)
                                .addGap(26, 26, 26)
                                .addComponent(jL_prov_nombre_err2)))
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(label36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jL_prov_apellidos_err2))))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jT_proyecto_nombre_01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jT_proyecto_ciudad_01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jL_prov_dir_err2)
                .addGap(31, 31, 31)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jB_proyecto_eliminar_01)
                    .addComponent(jB_proyecto_modificar)
                    .addComponent(jB_proyecto_insertar))
                .addGap(63, 63, 63))
        );

        jTabbedPane5.addTab("Gestion", jPanel19);

        jLabel15.setText("LISTADO DE PROYECTO");

        label38.setText("Código de proyecto");

        label39.setText("Nombre");

        label40.setText("Ciudad");

        jB_proyecto_eliminar_02.setText("Eliminar");
        jB_proyecto_eliminar_02.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_eliminar_02MouseClicked(evt);
            }
        });

        jLabel16.setText("//");

        jB_proyecto_ant.setText("<<");
        jB_proyecto_ant.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_antMouseClicked(evt);
            }
        });

        jB_proyecto_sigui.setText(">>");
        jB_proyecto_sigui.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_siguiMouseClicked(evt);
            }
        });

        jB_proyecto_final.setText(">>|");
        jB_proyecto_final.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_finalMouseClicked(evt);
            }
        });

        jB_proyecto_inicio.setText("|<<");
        jB_proyecto_inicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_inicioMouseClicked(evt);
            }
        });

        jB_proyecto_eject_consulta.setText("Ejecutar consulta");
        jB_proyecto_eject_consulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_eject_consultaMouseClicked(evt);
            }
        });

        jT_proyecto_nombre_02.setBackground(new java.awt.Color(255, 255, 255));

        jT_proyecto_cod_02.setBackground(new java.awt.Color(255, 255, 255));

        jT_proyecto_ciudad_02.setBackground(new java.awt.Color(255, 255, 255));

        jT_proyecto_min.setText("0");

        jT_proyecto_max.setText("000");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jB_proyecto_eject_consulta))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel23Layout.createSequentialGroup()
                                        .addComponent(jB_proyecto_inicio)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jB_proyecto_ant)
                                        .addGap(24, 24, 24)
                                        .addComponent(jT_proyecto_min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jT_proyecto_max, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jB_proyecto_sigui)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jB_proyecto_final)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                                        .addComponent(jB_proyecto_eliminar_02))
                                    .addGroup(jPanel23Layout.createSequentialGroup()
                                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(label38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(49, 49, 49)
                                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jT_proyecto_nombre_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jT_proyecto_cod_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jT_proyecto_ciudad_02, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))))
                .addGap(168, 168, 168))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel15)
                .addGap(23, 23, 23)
                .addComponent(jB_proyecto_eject_consulta)
                .addGap(52, 52, 52)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(label38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addComponent(label39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addComponent(label40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addComponent(jT_proyecto_nombre_02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jT_proyecto_ciudad_02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jT_proyecto_cod_02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jT_proyecto_min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jB_proyecto_eliminar_02)
                        .addComponent(jB_proyecto_ant)
                        .addComponent(jB_proyecto_sigui)
                        .addComponent(jB_proyecto_final)
                        .addComponent(jB_proyecto_inicio)
                        .addComponent(jLabel16)
                        .addComponent(jT_proyecto_max)))
                .addGap(53, 53, 53))
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane5.addTab("Listado", jPanel22);

        jT_proyectos.addTab("Gestión de proyectos", jTabbedPane5);

        jComboBox_proyecto_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Por código", "Por nombre", "Por ciudad" }));
        jComboBox_proyecto_filtro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_proyecto_filtroItemStateChanged(evt);
            }
        });

        jT_proyecto_filtro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jT_proyecto_filtroKeyReleased(evt);
            }
        });

        jTable_proyecto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Ciudad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(jTable_proyecto);

        label43.setForeground(new java.awt.Color(204, 0, 51));
        label43.setText("*");

        jB_proyecto_filtro_ejecutar.setText("Ejecutar");
        jB_proyecto_filtro_ejecutar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_filtro_ejecutarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jComboBox_proyecto_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jT_proyecto_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jB_proyecto_filtro_ejecutar)))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jB_proyecto_filtro_ejecutar)
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel25Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox_proyecto_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jT_proyecto_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(label43, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane7.addTab("Filtro de proyectos", jPanel25);

        jT_proyectos.addTab("Consulta de proyectos", jTabbedPane7);

        jTabbedPane1.addTab("Proyectos", jT_proyectos);

        jT_gestionGlobal.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jLabel17.setText("ALTAS BAJAS Y MODIFICACIONES - Relaciones entre Piezas, proveedores y proyectos");

        label37.setText("Código de proveedor");

        label41.setText("Código de pieza");

        label44.setText("Código de proyecto");

        jB_gG_limpiar1.setText("Limpiar");
        jB_gG_limpiar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_gG_limpiar1MouseClicked(evt);
            }
        });

        jB_gG_eliminar_2.setText("Eliminar");
        jB_gG_eliminar_2.setEnabled(false);
        jB_gG_eliminar_2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_gG_eliminar_2MouseClicked(evt);
            }
        });

        jB_gG_modificar1.setText("Modificar");
        jB_gG_modificar1.setEnabled(false);
        jB_gG_modificar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_gG_modificar1MouseClicked(evt);
            }
        });

        jB_gG_insertar1.setText("Insertar");
        jB_gG_insertar1.setEnabled(false);
        jB_gG_insertar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_gG_insertar1MouseClicked(evt);
            }
        });

        jL_prov_dir_err3.setForeground(new java.awt.Color(204, 0, 51));
        jL_prov_dir_err3.setText("*");

        jT_gG_sms_error1.setEditable(false);
        jT_gG_sms_error1.setBackground(new java.awt.Color(240, 240, 240));
        jT_gG_sms_error1.setColumns(20);
        jT_gG_sms_error1.setForeground(new java.awt.Color(204, 0, 51));
        jT_gG_sms_error1.setRows(5);
        jT_gG_sms_error1.setBorder(null);
        jScrollPane9.setViewportView(jT_gG_sms_error1);

        jLabel4.setText("Cantidad");

        jCombo_gG_cod_prov.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----------" }));
        jCombo_gG_cod_prov.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCombo_gG_cod_provItemStateChanged(evt);
            }
        });

        jCombo_gG_cod_pieza.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----------" }));
        jCombo_gG_cod_pieza.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCombo_gG_cod_piezaItemStateChanged(evt);
            }
        });

        jCombo_gG_cod_proyecto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----------" }));
        jCombo_gG_cod_proyecto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCombo_gG_cod_proyectoItemStateChanged(evt);
            }
        });

        jB_gG_recarga.setText("Recargar");
        jB_gG_recarga.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_gG_recargaMouseClicked(evt);
            }
        });

        jT_gG_proveedor.setBackground(new java.awt.Color(255, 255, 255));

        jT_gG_pieza.setBackground(new java.awt.Color(255, 255, 255));

        jT_gG_proyecto.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel17))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jB_gG_insertar1)
                        .addGap(18, 18, 18)
                        .addComponent(jB_gG_modificar1)
                        .addGap(282, 282, 282)
                        .addComponent(jB_gG_eliminar_2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jB_gG_recarga)
                        .addGap(457, 457, 457)
                        .addComponent(jB_gG_limpiar1))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(label37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(label41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(label44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))
                            .addGap(27, 27, 27)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jT_gG_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jL_prov_dir_err3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jCombo_gG_cod_prov, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCombo_gG_cod_pieza, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCombo_gG_cod_proyecto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jT_gG_proveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jT_gG_pieza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jT_gG_proyecto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(106, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel17)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jB_gG_recarga)
                    .addComponent(jB_gG_limpiar1))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(label41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCombo_gG_cod_prov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jT_gG_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jCombo_gG_cod_pieza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jT_gG_pieza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCombo_gG_cod_proyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jT_gG_proyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jT_gG_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jL_prov_dir_err3)))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jB_gG_eliminar_2)
                    .addComponent(jB_gG_modificar1)
                    .addComponent(jB_gG_insertar1))
                .addGap(63, 63, 63))
        );

        jTabbedPane8.addTab("Gestión Global", jPanel1);

        jTable_gG.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "C_proveedor", "C_pieza", "C_proyecto", "Cantidad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(jTable_gG);

        jB_proyecto_gG_ejecutar1.setText("Ejecutar");
        jB_proyecto_gG_ejecutar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jB_proyecto_gG_ejecutar1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jB_proyecto_gG_ejecutar1)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jB_proyecto_gG_ejecutar1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane8.addTab("Listado Global", jPanel6);

        jT_gestionGlobal.addTab("Piezas, Proveedores y Proyectos", jTabbedPane8);
        jT_gestionGlobal.addTab("Suministros por proveedor", jTabbedPane9);
        jT_gestionGlobal.addTab("Suministros por piezas", jTabbedPane10);
        jT_gestionGlobal.addTab("Estadísticas", jTabbedPane11);

        jTabbedPane1.addTab("Gestión Global", jT_gestionGlobal);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Autor: Jon López Garrido 9FDAM-DA\nProduct Version: NetBeans IDE 8.0.2 (Build 201411181905)\nUpdates: NetBeans IDE is updated to version NetBeans 8.0.2 Patch 2\nJava: 1.8.0_25; Java HotSpot(TM) 64-Bit Server VM 25.25-b02\nRuntime: Java(TM) SE Runtime Environment 1.8.0_25-b18\nSystem: Windows 8 version 6.2 running on amd64; Cp1252; es_ES (nb)\nUser directory: C:\\Users\\Pepe\\AppData\\Roaming\\NetBeans\\8.0.2\nCache directory: C:\\Users\\Pepe\\AppData\\Local\\NetBeans\\Cache\\8.0.2");
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Ayuda", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jB_prov_insertarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_prov_insertarMouseClicked
        // TODO add your handling code here:    
                  
        try{  
            
            if(jB_prov_insertar.isEnabled()){
                
                Proveedores p = rellenarProveedor(null); 

                if(p != null){

                    if(Hibernate_AD.insertOrUpdateProveedor(p)){

                        JOptionPane.showMessageDialog(null, "Datos de proveedor agregados correctamente");            

                        limpiarProveedor();

                    }else

                        JOptionPane.showMessageDialog(null, "No se ha podido grabar el nuevo proveedor");      
                }

            }
            
        }
        catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        
    }//GEN-LAST:event_jB_prov_insertarMouseClicked
    
    private void jB_prov_eject_consultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_prov_eject_consultaMouseClicked
        // TODO add your handling code here:       
         
        if(jB_prov_eject_consulta.isEnabled()){
                    
            this.arr_proveedores = Hibernate_AD.findAllProveedores();

            if(arr_proveedores != null && arr_proveedores.size() > 0){

                jT_prov_min.setText( "1" );

                Integer max = arr_proveedores.size();  

                jT_prov_max.setText( max.toString() );

                pintarProveedorListado(arr_proveedores.get(0));

            }        

        }
        
    }//GEN-LAST:event_jB_prov_eject_consultaMouseClicked
   
    private void jB_prov_inicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_prov_inicioMouseClicked
        // TODO add your handling code here:    
        
        if(jB_prov_inicio.isEnabled()){
            
            if(arr_proveedores != null && arr_proveedores.size() > 0){   
            
                pintarProveedorListado(arr_proveedores.get(0));   

                jT_prov_min.setText( "1" );

                nav_prov = 0;

            } 
            
        }                 
        
    }//GEN-LAST:event_jB_prov_inicioMouseClicked

    private void jB_prov_finalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_prov_finalMouseClicked
        // TODO add your handling code here:
        
        if(jB_prov_inicio.isEnabled()){
            
            if(arr_proveedores != null && arr_proveedores.size() > 0){   

               pintarProveedorListado(arr_proveedores.get((arr_proveedores.size() - 1)));

               Integer min = arr_proveedores.size();   

               jT_prov_min.setText( min.toString() );

               nav_prov = (arr_proveedores.size() - 1);

           }    
            
        }
        
    }//GEN-LAST:event_jB_prov_finalMouseClicked

    private void jB_prov_antMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_prov_antMouseClicked
        // TODO add your handling code here:
        
        if(jB_prov_ant.isEnabled()){
         
            if(arr_proveedores != null && arr_proveedores.size() > 0){
            
                if( (nav_prov - 1)>=0){

                    pintarProveedorListado(arr_proveedores.get(nav_prov - 1));

                    Integer min = nav_prov; 

                    jT_prov_min.setText( min.toString() );

                    nav_prov = nav_prov - 1;

                }else{

                    pintarProveedorListado(arr_proveedores.get((arr_proveedores.size() - 1)));

                    Integer min = arr_proveedores.size();  

                    jT_prov_min.setText( min.toString() );

                    nav_prov = arr_proveedores.size() - 1;

                }

            } 
            
        }
        
    }//GEN-LAST:event_jB_prov_antMouseClicked

    private void jB_prov_siguiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_prov_siguiMouseClicked
        // TODO add your handling code here:        
        
        if(arr_proveedores != null && arr_proveedores.size() > 0){
            
            if( (nav_prov + 1) < arr_proveedores.size() ){ 
                
                pintarProveedorListado(arr_proveedores.get(nav_prov + 1));
                
                nav_prov = nav_prov + 1;
                
                Integer min = nav_prov + 1;  
                
                jT_prov_min.setText( min.toString() );   
                
            }else{
                
                pintarProveedorListado(arr_proveedores.get(0));  
                
                jT_prov_min.setText( "1" );
                
                nav_prov = 0;
                
            }
            
        } 
        
    }//GEN-LAST:event_jB_prov_siguiMouseClicked

    private void jB_prov_eliminar_02MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_prov_eliminar_02MouseClicked
        // TODO add your handling code here:
        if(jB_prov_eliminar_02.isEnabled()){
        
            if(arr_proveedores != null && arr_proveedores.size() > 0){            
            
                try {
                    if(Hibernate_AD.deleteProveedor(arr_proveedores.get(nav_prov))){
                        
                        JOptionPane.showMessageDialog(this, "Proveedor ( " + arr_proveedores.get(nav_prov).getNombre() + " )  eliminado correcamente.");
                        
                        arr_proveedores.remove(nav_prov);
                        
                        if(arr_proveedores.size() > 0){
                            
                            if((nav_prov - 1)>=0){
                                
                                pintarProveedorListado(arr_proveedores.get(nav_prov - 1));
                                
                                Integer min = nav_prov;
                                
                                jT_prov_min.setText( min.toString() );
                                
                                nav_prov = nav_prov - 1;
                                
                            }else{
                                
                                pintarProveedorListado(arr_proveedores.get((arr_proveedores.size() - 1)));
                                
                                Integer min = arr_proveedores.size();
                                
                                jT_prov_min.setText( min.toString() );
                                
                                nav_prov = arr_proveedores.size() - 1;
                                
                            }
                            
                            Integer max = arr_proveedores.size();
                            
                            jT_prov_max.setText( max.toString() );
                            
                        }else{
                            
                            jT_prov_min.setText( "0" );
                            
                            jT_prov_max.setText( "000" );
                            
                            pintarProveedorListado(new Proveedores());
                            
                        }
                        
                    }else
                        
                        JOptionPane.showMessageDialog(this, "Proveedor ( " + arr_proveedores.get(nav_prov).getNombre() + " )  no se ha podido eliminar.");
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalOrphanException ex) {
                    Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            
        }   
        
    }//GEN-LAST:event_jB_prov_eliminar_02MouseClicked
    
    private void jB_prov_limpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_prov_limpiarMouseClicked
        // TODO add your handling code here:
        
        limpiarProveedor();
        
    }//GEN-LAST:event_jB_prov_limpiarMouseClicked

    private void jB_prov_filtro_ejecutarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_prov_filtro_ejecutarMouseClicked
        // TODO add your handling code here:
        
        try{
            
            dtm_prov.setNumRows(0); 
            
            this.arr_proveedores = Hibernate_AD.findProveedorByFiltro(jComboBox_prov_filtro.getSelectedIndex(), jT_prov_filtro.getText());
        
            if(arr_proveedores != null && arr_proveedores.size() > 0){                
                
                for(Proveedores cur_prov : arr_proveedores)
                    
                    pintarProveedorTabla(cur_prov);
                            
            }   
            
        }catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        }  
        
    }//GEN-LAST:event_jB_prov_filtro_ejecutarMouseClicked
    
    private void jB_prov_modificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_prov_modificarMouseClicked
        // TODO add your handling code here:
        
        if(mod_prov != null){            
           
            Proveedores p = rellenarProveedor(mod_prov.getCodigo());
            
            if(p != null){
                
                if(Hibernate_AD.insertOrUpdateProveedor(p))            {
                
                    JOptionPane.showMessageDialog(null, "Datos del proveedor (" + p.getCodigo()+ ") modificados correctamente");

                    limpiarProveedor();

                }else

                    JOptionPane.showMessageDialog(null, "Los datos del proveedor (" + p.getCodigo()+ ") no se pudieron modifcar");
            }
                            
        }
        
    }//GEN-LAST:event_jB_prov_modificarMouseClicked
    
    private void jB_prov_eliminar_01MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_prov_eliminar_01MouseClicked
        // TODO add your handling code here:
        if(jB_prov_eliminar_01.isEnabled()){
         
            if(mod_prov != null){     
            
                try {
                    if(Hibernate_AD.deleteProveedor(mod_prov)){
                        
                        JOptionPane.showMessageDialog(this, "Proveedor ( " + arr_proveedores.get(nav_prov).getNombre() + " )  eliminado correcamente.");
                        
                        limpiarProveedor();
                        
                    }else
                        
                        JOptionPane.showMessageDialog(this, "Proveedor ( " + arr_proveedores.get(nav_prov).getNombre() + " )  no se ha podido eliminar.");
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalOrphanException ex) {
                    Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            
        }
        
    }//GEN-LAST:event_jB_prov_eliminar_01MouseClicked

    private void jT_prov_cod_01KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jT_prov_cod_01KeyReleased
        // TODO add your handling code here:
         
        jT_prov_cod_01.setText(jT_prov_cod_01.getText().toUpperCase());
        
        if(!jT_prov_cod_01.getText().equals(""))
            this.arr_proveedores = Hibernate_AD.findProveedorByFiltro(0, jT_prov_cod_01.getText());
        else
            this.arr_proveedores = null;
            
        if(arr_proveedores != null && arr_proveedores.size() > 0){
            
            jT_prov_nombre_01.setText(arr_proveedores.get(0).getNombre());

            jT_prov_apellidos_01.setText(arr_proveedores.get(0).getApellidos());

            jT_prov_dir_01.setText(arr_proveedores.get(0).getDireccion());

            mod_prov = arr_proveedores.get(0);

            jB_prov_insertar.setEnabled(false);

            jB_prov_modificar.setEnabled(true);

            jB_prov_eliminar_01.setEnabled(true);

        }else{
            
            jT_prov_nombre_01.setText("");
            
            jT_prov_apellidos_01.setText("");
            
            jT_prov_dir_01.setText("");
            
            jB_prov_insertar.setEnabled((!jT_prov_cod_01.getText().equals("") && jT_prov_cod_01.getText().trim().length() > 0));
            
            jB_prov_modificar.setEnabled(false);
            
            jB_prov_eliminar_01.setEnabled(false);
            
            mod_prov = null;
            
        }
        
    }//GEN-LAST:event_jT_prov_cod_01KeyReleased

    private void jT_prov_filtroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jT_prov_filtroKeyReleased
        // TODO add your handling code here:
        try{
            
            jT_prov_filtro.setText(jT_prov_filtro.getText().toUpperCase());            
            
            dtm_prov.setNumRows(0); 
            
            this.arr_proveedores = Hibernate_AD.findProveedorByFiltro(jComboBox_prov_filtro.getSelectedIndex(), jT_prov_filtro.getText());
        
            if(arr_proveedores != null && arr_proveedores.size() > 0){                
                
                for(Proveedores cur_prov : arr_proveedores)
                    
                    pintarProveedorTabla(cur_prov);
                            
            }   
            
        }catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        }        
    }//GEN-LAST:event_jT_prov_filtroKeyReleased

    private void jComboBox_prov_filtroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_prov_filtroItemStateChanged
        // TODO add your handling code here:
        try{
            
            jT_prov_filtro.setText(jT_prov_filtro.getText().toUpperCase());            
            
            dtm_prov.setNumRows(0); 
            
            this.arr_proveedores = Hibernate_AD.findProveedorByFiltro(jComboBox_prov_filtro.getSelectedIndex(), jT_prov_filtro.getText());
        
            if(arr_proveedores != null && arr_proveedores.size() > 0){                
                
                for(Proveedores cur_prov : arr_proveedores)
                    
                    pintarProveedorTabla(cur_prov);
                            
            }   
            
        }catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        }        
    }//GEN-LAST:event_jComboBox_prov_filtroItemStateChanged

    private void jT_pieza_cod_01KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jT_pieza_cod_01KeyReleased
        // TODO add your handling code here:
        
        jT_pieza_cod_01.setText(jT_pieza_cod_01.getText().toUpperCase());
        
        if(!jT_pieza_cod_01.getText().equals(""))
            this.arr_piezas = Hibernate_AD.findPiezaByFiltro(0, jT_pieza_cod_01.getText());
        else
            this.arr_piezas = null;
        
        if(arr_piezas != null && arr_piezas.size() > 0){
            
            jT_pieza_nombre_01.setText(arr_piezas.get(0).getNombre());
            
            jT_pieza_precio_01.setText(((Float) arr_piezas.get(0).getPrecio()).toString());
            
            jT_pieza_desc_01.setText(arr_piezas.get(0).getDescripcion());
            
            mod_pieza = arr_piezas.get(0);
            
            jB_pieza_insertar.setEnabled(false);
            
            jB_pieza_modificar.setEnabled(true);
            
            jB_pieza_eliminar_01.setEnabled(true);
            
        }else{
            
            jT_pieza_nombre_01.setText("");
            
            jT_pieza_precio_01.setText("");  
            
             jT_pieza_desc_01.setText("");
            
            jB_pieza_insertar.setEnabled((!jT_pieza_cod_01.getText().equals("") && jT_pieza_cod_01.getText().trim().length() > 0));
            
            jB_pieza_modificar.setEnabled(false);
            
            jB_pieza_eliminar_01.setEnabled(false);
            
            mod_pieza = null;
            
        }
        
    }//GEN-LAST:event_jT_pieza_cod_01KeyReleased

    private void jB_pieza_limpiar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_pieza_limpiar1MouseClicked
        // TODO add your handling code here:
        
        limpiarPieza();
        
    }//GEN-LAST:event_jB_pieza_limpiar1MouseClicked

    private void jB_pieza_eliminar_01MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_pieza_eliminar_01MouseClicked
        // TODO add your handling code here:
       
       if(jB_pieza_eliminar_01.isEnabled()){
           
           if(mod_pieza != null){     
            
            try {
                
                if(Hibernate_AD.deletePieza(mod_pieza)){

                    JOptionPane.showMessageDialog(this, "Pieza ( " + arr_piezas.get(nav_pieza).getNombre() + " )  eliminado correcamente.");

                    limpiarPieza();

                }else

                    JOptionPane.showMessageDialog(this, "Pieza ( " + arr_piezas.get(nav_pieza).getNombre() + " )  no se ha podido eliminar.");

            } catch (IllegalOrphanException ex) {

                Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

            } catch (NonexistentEntityException ex) {

                Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

            }

         }
           
       } 
       
    }//GEN-LAST:event_jB_pieza_eliminar_01MouseClicked

    private void jB_pieza_modificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_pieza_modificarMouseClicked
        // TODO add your handling code here:
        
        if(jB_pieza_modificar.isEnabled()){
            
            if(mod_pieza != null){            

               Piezas p = rellenarPieza(mod_pieza.getCodigo());

               if(p != null){

                   try {

                       if(Hibernate_AD.updatePieza(p))            {

                           JOptionPane.showMessageDialog(null, "Datos de la pieza (" + p.getCodigo()+ ") modificados correctamente");

                           limpiarPieza();

                       }else

                           JOptionPane.showMessageDialog(null, "Los datos de la pieza (" + p.getCodigo()+ ") no se pudieron modifcar");

                   } catch (Exception ex) {

                       Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

                   }
                   
               }

           }   
            
        }
         
    }//GEN-LAST:event_jB_pieza_modificarMouseClicked

    private void jB_pieza_insertarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_pieza_insertarMouseClicked
        // TODO add your handling code here:
        try{  
            
            if(jB_pieza_insertar.isEnabled()){
                
               Piezas p = rellenarPieza(null); 

               if(p != null){

                   if(Hibernate_AD.insertPieza(p)){

                       JOptionPane.showMessageDialog(null, "Datos de pieza agregados correctamente");            

                       limpiarPieza();

                   }else

                       JOptionPane.showMessageDialog(null, "No se ha podido grabar la nueva pieza");      
               }   
                
            }
            
        }
        catch(Exception ex){
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jB_pieza_insertarMouseClicked

    private void jB_pieza_eliminar_02MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_pieza_eliminar_02MouseClicked
        // TODO add your handling code here:
        
        if(jB_pieza_eliminar_02.isEnabled()){
         
            if(arr_piezas != null && arr_piezas.size() > 0){
            
                try {

                    if(Hibernate_AD.deletePieza(arr_piezas.get(nav_pieza))){

                        JOptionPane.showMessageDialog(this, "Pieza ( " + arr_piezas.get(nav_pieza).getNombre() + " )  eliminado correcamente.");

                        arr_piezas.remove(nav_pieza);   

                        if(arr_piezas.size() > 0){

                            if((nav_pieza - 1)>=0){

                                pintarPiezaListado(arr_piezas.get(nav_pieza- 1));

                                Integer min = nav_pieza;

                                jT_pieza_min.setText( min.toString() );

                                nav_pieza = nav_pieza - 1;

                            }else{

                                pintarPiezaListado(arr_piezas.get((arr_piezas.size() - 1)));

                                Integer min = arr_piezas.size();

                                jT_pieza_min.setText( min.toString() );

                                nav_pieza = arr_piezas.size() - 1;

                            }    

                            Integer max = arr_piezas.size();

                            jT_pieza_max.setText( max.toString() );

                        }else{

                            jT_pieza_min.setText( "0" );

                            jT_pieza_max.setText( "000" );

                            pintarPiezaListado(new Piezas());

                        }

                    }else

                        JOptionPane.showMessageDialog(this, "Pieza ( " + arr_piezas.get(nav_pieza).getNombre() + " )  no se ha podido eliminar.");

                } catch (IllegalOrphanException ex) {

                    Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

                } catch (NonexistentEntityException ex) {

                    Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

                }

            }    
            
        }
        
    }//GEN-LAST:event_jB_pieza_eliminar_02MouseClicked

    private void jB_pieza_antMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_pieza_antMouseClicked
        // TODO add your handling code here:
        
         if(arr_piezas != null && arr_piezas.size() > 0){
            
            if( (nav_pieza - 1)>=0){
                
                pintarPiezaListado(arr_piezas.get(nav_pieza - 1));
                
                Integer min = nav_pieza; 
                
                jT_pieza_min.setText( min.toString() );
                
                nav_pieza = nav_pieza - 1;
                
            }else{
                
                pintarPiezaListado(arr_piezas.get((arr_piezas.size() - 1)));
                
                Integer min = arr_piezas.size();  
                
                jT_pieza_min.setText( min.toString() );
                
                nav_pieza = arr_piezas.size() - 1;
                
            }
            
        } 
    }//GEN-LAST:event_jB_pieza_antMouseClicked

    private void jB_pieza_siguiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_pieza_siguiMouseClicked
        // TODO add your handling code here:
        if(arr_piezas != null && arr_piezas.size() > 0){
            
            if( (nav_pieza + 1) < arr_piezas.size() ){ 
                
                pintarPiezaListado(arr_piezas.get(nav_pieza + 1));
                
                nav_pieza = nav_pieza + 1;
                
                Integer min = nav_pieza + 1;  
                
                jT_pieza_min.setText( min.toString() );   
                
            }else{
                
                pintarPiezaListado(arr_piezas.get(0));  
                
                jT_pieza_min.setText( "1" );
                
                nav_pieza = 0;
                
            }
            
        } 
    }//GEN-LAST:event_jB_pieza_siguiMouseClicked

    private void jB_pieza_finalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_pieza_finalMouseClicked
        // TODO add your handling code here:
        if(arr_piezas != null && arr_piezas.size() > 0){   
            
            pintarPiezaListado(arr_piezas.get((arr_piezas.size() - 1)));
            
            Integer min = arr_piezas.size();   
            
            jT_pieza_min.setText( min.toString() );
            
            nav_pieza = (arr_piezas.size() - 1);
            
        } 
    }//GEN-LAST:event_jB_pieza_finalMouseClicked

    private void jB_pieza_inicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_pieza_inicioMouseClicked
        // TODO add your handling code here:
        if(arr_piezas != null && arr_piezas.size() > 0){   
            
            pintarPiezaListado(arr_piezas.get(0));   
            
            jT_pieza_min.setText( "1" );
            
            nav_pieza = 0;
            
        }  
    }//GEN-LAST:event_jB_pieza_inicioMouseClicked

    private void jB_pieza_eject_consultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_pieza_eject_consultaMouseClicked
        // TODO add your handling code here:
        this.arr_piezas = Hibernate_AD.findAllPiezas();
         
        if(arr_piezas != null && arr_piezas.size() > 0){
            
            jT_pieza_min.setText( "1" );
            
            Integer max = arr_piezas.size();  
            
            jT_pieza_max.setText( max.toString() );
            
            pintarPiezaListado(arr_piezas.get(0));
            
        } 
    }//GEN-LAST:event_jB_pieza_eject_consultaMouseClicked

    private void jT_proyecto_cod_01KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jT_proyecto_cod_01KeyReleased
        // TODO add your handling code here:
        
        jT_proyecto_cod_01.setText(jT_proyecto_cod_01.getText().toUpperCase());
        
        if(!jT_proyecto_cod_01.getText().equals(""))
            
            this.arr_proyectos = Hibernate_AD.findProyectoByFiltro(0, jT_proyecto_cod_01.getText());
        
        else
            
            this.arr_proyectos = null;
        
        if(arr_proyectos != null && arr_proyectos.size() > 0){
            
            jT_proyecto_nombre_01.setText(arr_proyectos.get(0).getNombre());
            
            jT_proyecto_ciudad_01.setText(arr_proyectos.get(0).getCiudad());
            
            mod_proyecto = arr_proyectos.get(0);
            
            jB_proyecto_insertar.setEnabled(false);
            
            jB_proyecto_modificar.setEnabled(true);
            
            jB_proyecto_eliminar_01.setEnabled(true);
            
        }else{
            
            jT_proyecto_nombre_01.setText("");
            
            jT_proyecto_ciudad_01.setText("");              
            
            jB_proyecto_insertar.setEnabled((!jT_proyecto_cod_01.getText().equals("") && jT_proyecto_cod_01.getText().trim().length() > 0));
            
            jB_proyecto_modificar.setEnabled(false);
            
            jB_proyecto_eliminar_01.setEnabled(false);
            
            mod_proyecto = null;
            
        }
        
    }//GEN-LAST:event_jT_proyecto_cod_01KeyReleased

    private void jB_proyecto_limpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_limpiarMouseClicked
        // TODO add your handling code here:
        
        limpiarProyecto();
        
    }//GEN-LAST:event_jB_proyecto_limpiarMouseClicked

    private void jB_proyecto_eliminar_01MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_eliminar_01MouseClicked
        // TODO add your handling code here:
        
        if(jB_proyecto_eliminar_01.isEnabled()){
            
            if(mod_proyecto != null){     
            
                try {
                    if(Hibernate_AD.deleteProyecto(mod_proyecto)){

                        JOptionPane.showMessageDialog(this, "Proyecto ( " + arr_proyectos.get(nav_proyecto).getNombre() + " )  eliminado correcamente.");

                        limpiarProyecto();

                    }else

                        JOptionPane.showMessageDialog(this, "proyecto ( " + arr_proyectos.get(nav_proyecto).getNombre() + " )  no se ha podido eliminar.");

                } catch (IllegalOrphanException ex) {

                    Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

                } catch (NonexistentEntityException ex) {

                    Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
            
        }
        
    }//GEN-LAST:event_jB_proyecto_eliminar_01MouseClicked

    private void jB_proyecto_modificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_modificarMouseClicked
        // TODO add your handling code here:
        
        if(jB_proyecto_modificar.isEnabled()){
            
            if(mod_proyecto != null){            
           
                Proyectos p = rellenarProyecto(mod_proyecto.getCodigo());

                if(p != null){

                    try {

                        if(Hibernate_AD.updateProyecto(p)){

                            JOptionPane.showMessageDialog(null, "Datos del proyecto (" + p.getCodigo()+ ") modificados correctamente");

                            limpiarProveedor();

                        }else

                            JOptionPane.showMessageDialog(null, "Los datos del proyecto (" + p.getCodigo()+ ") no se pudieron modifcar");

                    } catch (Exception ex) {

                        Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

            }
            
        }
        
    }//GEN-LAST:event_jB_proyecto_modificarMouseClicked

    private void jB_proyecto_insertarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_insertarMouseClicked
        // TODO add your handling code here:
         try{
             
            if(jB_proyecto_insertar.isEnabled()){
                 
                Proyectos p = rellenarProyecto(null); 
            
                if(p != null){

                    if(Hibernate_AD.insertProyecto(p)){

                        JOptionPane.showMessageDialog(null, "Datos de proyectos agregados correctamente");            

                        limpiarProyecto();

                    }else

                        JOptionPane.showMessageDialog(null, "No se ha podido grabar el nuevo Proyecto");    

                }    
                 
            }
            
        }
        catch(Exception ex){
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jB_proyecto_insertarMouseClicked

    private void jB_proyecto_eliminar_02MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_eliminar_02MouseClicked
        // TODO add your handling code here:
        
        if(jB_proyecto_eliminar_02.isEnabled()){
            
            if(arr_proyectos != null && arr_proyectos.size() > 0){

               try {

                   if(Hibernate_AD.deleteProyecto(arr_proyectos.get(nav_proyecto))){

                       JOptionPane.showMessageDialog(this, "Pieza ( " + arr_proyectos.get(nav_proyecto).getNombre() + " )  eliminado correcamente.");

                       arr_proyectos.remove(nav_proyecto);   

                       if(arr_proyectos.size() > 0){

                           if((nav_proyecto - 1)>=0){

                               pintarProyectoListado(arr_proyectos.get(nav_proyecto- 1));

                               Integer min = nav_proyecto;

                               jT_proyecto_min.setText( min.toString() );

                               nav_proyecto = nav_proyecto - 1;

                           }else{

                               pintarProyectoListado(arr_proyectos.get((arr_proyectos.size() - 1)));

                               Integer min = arr_proyectos.size();

                               jT_proyecto_min.setText( min.toString() );

                               nav_proyecto = arr_proyectos.size() - 1;

                           }    

                           Integer max = arr_proyectos.size();

                           jT_proyecto_max.setText( max.toString() );

                       }else{

                           jT_proyecto_min.setText( "0" );

                           jT_proyecto_max.setText( "000" );

                           pintarProyectoListado(new Proyectos());

                       }

                   }else

                       JOptionPane.showMessageDialog(this, "Pieza ( " + arr_proyectos.get(nav_proyecto).getNombre() + " )  no se ha podido eliminar.");

               } catch (IllegalOrphanException ex) {

                   Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

               } catch (NonexistentEntityException ex) {

                   Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

               }

           }    
            
        }        
                 
    }//GEN-LAST:event_jB_proyecto_eliminar_02MouseClicked

    private void jB_proyecto_antMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_antMouseClicked
        // TODO add your handling code here:
        if(arr_proyectos != null && arr_proyectos.size() > 0){
            
            if( (nav_proyecto - 1)>=0){
                
                pintarProyectoListado(arr_proyectos.get(nav_proyecto - 1));
                
                Integer min = nav_proyecto; 
                
                jT_proyecto_min.setText( min.toString() );
                
                nav_proyecto = nav_proyecto - 1;
                
            }else{
                
                pintarProyectoListado(arr_proyectos.get((arr_proyectos.size() - 1)));
                
                Integer min = arr_proyectos.size();  
                
                jT_proyecto_min.setText( min.toString() );
                
                nav_proyecto = arr_proyectos.size() - 1;
                
            }
            
        } 
    }//GEN-LAST:event_jB_proyecto_antMouseClicked

    private void jB_proyecto_siguiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_siguiMouseClicked
        // TODO add your handling code here:
        
        if(arr_proyectos != null && arr_proyectos.size() > 0){
            
            if( (nav_proyecto + 1) < arr_proyectos.size() ){ 
                
                pintarProyectoListado(arr_proyectos.get(nav_proyecto + 1));
                
                nav_proyecto = nav_proyecto + 1;
                
                Integer min = nav_proyecto + 1;  
                
                jT_proyecto_min.setText( min.toString() );   
                
            }else{
                
                pintarProyectoListado(arr_proyectos.get(0));  
                
                jT_proyecto_min.setText( "1" );
                
                nav_proyecto = 0;
                
            }
            
        } 
        
    }//GEN-LAST:event_jB_proyecto_siguiMouseClicked

    private void jB_proyecto_finalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_finalMouseClicked
        // TODO add your handling code here:
        
        if(arr_proyectos != null && arr_proyectos.size() > 0){   
            
            pintarProyectoListado(arr_proyectos.get((arr_proyectos.size() - 1)));
            
            Integer min = arr_proyectos.size();   
            
            jT_proyecto_min.setText( min.toString() );
            
            nav_proyecto = (arr_proyectos.size() - 1);
            
        } 
        
    }//GEN-LAST:event_jB_proyecto_finalMouseClicked

    private void jB_proyecto_inicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_inicioMouseClicked
        // TODO add your handling code here:
         if(arr_proyectos != null && arr_proyectos.size() > 0){   
            
            pintarProyectoListado(arr_proyectos.get(0));   
            
            jT_proyecto_min.setText( "1" );
            
            nav_proyecto = 0;
            
        }  
         
    }//GEN-LAST:event_jB_proyecto_inicioMouseClicked

    private void jB_proyecto_eject_consultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_eject_consultaMouseClicked
        // TODO add your handling code here:
        
        this.arr_proyectos = Hibernate_AD.findAllProyectos();
         
        if(arr_proyectos != null && arr_proyectos.size() > 0){
            
            jT_proyecto_min.setText( "1" );
            
            Integer max = arr_proyectos.size();  
            
            jT_proyecto_max.setText( max.toString() );
            
            pintarProyectoListado(arr_proyectos.get(0));
            
        } 
        
    }//GEN-LAST:event_jB_proyecto_eject_consultaMouseClicked

    private void jComboBox_pieza_filtroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_pieza_filtroItemStateChanged
        // TODO add your handling code here:
         try{
            
            jT_pieza_filtro.setText(jT_pieza_filtro.getText().toUpperCase());            
            
            dtm_piezas.setNumRows(0); 
            
            this.arr_piezas = Hibernate_AD.findPiezaByFiltro(jComboBox_pieza_filtro.getSelectedIndex(), jT_pieza_filtro.getText());
        
            if(arr_piezas != null && arr_piezas.size() > 0){                
                
                for(Piezas cur_pieza : arr_piezas)
                    
                    pintarPiezaTabla(cur_pieza);
                            
            }   
            
        }catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        }        
    }//GEN-LAST:event_jComboBox_pieza_filtroItemStateChanged

    private void jT_pieza_filtroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jT_pieza_filtroKeyReleased
        // TODO add your handling code here:
        
         try{
            
            jT_pieza_filtro.setText(jT_pieza_filtro.getText().toUpperCase());            
            
            dtm_piezas.setNumRows(0); 
            
            this.arr_piezas = Hibernate_AD.findPiezaByFiltro(jComboBox_pieza_filtro.getSelectedIndex(), jT_pieza_filtro.getText());
        
            if(arr_piezas != null && arr_piezas.size() > 0){                
                
                for(Piezas cur_pieza : arr_piezas)
                    
                    pintarPiezaTabla(cur_pieza);
                            
            }   
            
        }catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        }        
         
    }//GEN-LAST:event_jT_pieza_filtroKeyReleased

    private void jB_pieza_filtro_ejecutar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_pieza_filtro_ejecutar1MouseClicked
        // TODO add your handling code here:
        try{
            
            dtm_piezas.setNumRows(0); 
            
            this.arr_piezas = Hibernate_AD.findPiezaByFiltro(jComboBox_pieza_filtro.getSelectedIndex(), jT_pieza_filtro.getText());
        
            if(arr_piezas != null && arr_piezas.size() > 0){                
                
                for(Piezas cur_pieza : arr_piezas)
                    
                    pintarPiezaTabla(cur_pieza);
                            
            }   
            
        }catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        }        
    }//GEN-LAST:event_jB_pieza_filtro_ejecutar1MouseClicked

    private void jComboBox_proyecto_filtroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_proyecto_filtroItemStateChanged
        // TODO add your handling code here:
        
        try{
            
            jT_proyecto_filtro.setText(jT_proyecto_filtro.getText().toUpperCase());            
            
            dtm_proyectos.setNumRows(0); 
            
            this.arr_proyectos = Hibernate_AD.findProyectoByFiltro(jComboBox_proyecto_filtro.getSelectedIndex(), jT_proyecto_filtro.getText());
        
            if(arr_proyectos != null && arr_proyectos.size() > 0){                
                
                for(Proyectos cur_proyecto : arr_proyectos)
                    
                    pintarProyectoTabla(cur_proyecto);
                            
            }   
            
        }catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        }     
        
    }//GEN-LAST:event_jComboBox_proyecto_filtroItemStateChanged

    private void jT_proyecto_filtroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jT_proyecto_filtroKeyReleased
        // TODO add your handling code here:
        
          try{
            
            jT_proyecto_filtro.setText(jT_proyecto_filtro.getText().toUpperCase());            
            
            dtm_proyectos.setNumRows(0); 
            
            this.arr_proyectos = Hibernate_AD.findProyectoByFiltro(jComboBox_proyecto_filtro.getSelectedIndex(), jT_proyecto_filtro.getText());
        
            if(arr_proyectos != null && arr_proyectos.size() > 0){                
                
                for(Proyectos cur_proyecto : arr_proyectos)
                    
                    pintarProyectoTabla(cur_proyecto);
                            
            }   
            
        }catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        } 
        
    }//GEN-LAST:event_jT_proyecto_filtroKeyReleased

    private void jB_proyecto_filtro_ejecutarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_filtro_ejecutarMouseClicked
        // TODO add your handling code here:
        
         try{
            
            dtm_proyectos.setNumRows(0); 
            
            this.arr_proyectos = Hibernate_AD.findProyectoByFiltro(jComboBox_proyecto_filtro.getSelectedIndex(), jT_proyecto_filtro.getText());
        
            if(arr_proyectos != null && arr_proyectos.size() > 0){                
                
                for(Proyectos cur_proyecto : arr_proyectos)
                    
                    pintarProyectoTabla(cur_proyecto);
                            
            }   
            
        }catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        }  
         
    }//GEN-LAST:event_jB_proyecto_filtro_ejecutarMouseClicked

    private void jB_gG_recargaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_gG_recargaMouseClicked
        // TODO add your handling code here:
        this.arr_proveedores  = Hibernate_AD.findAllProveedores();
        this.arr_piezas  = Hibernate_AD.findAllPiezas();
        this.arr_proyectos  = Hibernate_AD.findAllProyectos();
        
        jCombo_gG_cod_prov.removeAllItems();
        jCombo_gG_cod_prov.addItem("Selecciona un código");
        jCombo_gG_cod_pieza.removeAllItems();
        jCombo_gG_cod_pieza.addItem("Selecciona un código");
        jCombo_gG_cod_proyecto.removeAllItems();
        jCombo_gG_cod_proyecto.addItem("Selecciona un código");
        
        if(arr_proveedores != null && arr_proveedores.size() > 0){
            
            for(Proveedores cur_prov : arr_proveedores){
                
                jCombo_gG_cod_prov.addItem(cur_prov.getCodigo());
                
            }            
            
        }        
        
        if(arr_piezas != null && arr_piezas.size() > 0){
            
            for(Piezas cur_pieza : arr_piezas){
                
                jCombo_gG_cod_pieza.addItem(cur_pieza.getCodigo());
                
            }            
            
        }
        
        if(arr_proyectos!= null && arr_proyectos.size() > 0){
            
            for(Proyectos cur_proyecto : arr_proyectos){
                
                jCombo_gG_cod_proyecto.addItem(cur_proyecto.getCodigo());
                
            }           
            
        } 
    }//GEN-LAST:event_jB_gG_recargaMouseClicked
   
    private void jCombo_gG_cod_provItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCombo_gG_cod_provItemStateChanged
        // TODO add your handling code here:
        if(jCombo_gG_cod_prov.getItemCount() > 0 && jCombo_gG_cod_prov.getSelectedIndex() != 0){
            
            arr_proveedores = Hibernate_AD.findProveedorByFiltro(0, jCombo_gG_cod_prov.getSelectedItem().toString());
            
            if(arr_proveedores != null && arr_proveedores.size() > 0){
                
                Proveedores cur_prov = arr_proveedores.get(0);
                
                String str = cur_prov.getNombre() + " " + cur_prov.getApellidos() + " | Dirección -> " + cur_prov.getDireccion();
                
                jT_gG_proveedor.setText(str);
                
            }
            
        }else
            
            jT_gG_proveedor.setText("");
        
        exiteGestion();
        
    }//GEN-LAST:event_jCombo_gG_cod_provItemStateChanged

    private void jCombo_gG_cod_piezaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCombo_gG_cod_piezaItemStateChanged
        // TODO add your handling code here:
         if(jCombo_gG_cod_pieza.getItemCount() > 0 && jCombo_gG_cod_pieza.getSelectedIndex() != 0){
            
            arr_piezas = Hibernate_AD.findPiezaByFiltro(0, jCombo_gG_cod_pieza.getSelectedItem().toString());
            
            if(arr_piezas != null && arr_piezas.size() > 0){
                
                Piezas cur_pieza = arr_piezas.get(0);
                
                String str = cur_pieza.getNombre() + " - " + cur_pieza.getPrecio() + " | Descripción -> " + cur_pieza.getDescripcion();
                
                jT_gG_pieza.setText(str);
                
            }
            
        }else
             
            jT_gG_pieza.setText("");
         
         exiteGestion();
         
    }//GEN-LAST:event_jCombo_gG_cod_piezaItemStateChanged

    private void jCombo_gG_cod_proyectoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCombo_gG_cod_proyectoItemStateChanged
        // TODO add your handling code here:
        
        if(jCombo_gG_cod_proyecto.getItemCount() > 0 && jCombo_gG_cod_proyecto.getSelectedIndex() != 0){
            
            arr_proyectos = Hibernate_AD.findProyectoByFiltro(0, jCombo_gG_cod_proyecto.getSelectedItem().toString());
            
            if(arr_proyectos != null && arr_proyectos.size() > 0){
                
                Proyectos cur_proyecto = arr_proyectos.get(0);
                
                String str = cur_proyecto.getNombre() + " - " + cur_proyecto.getCiudad();
                
                jT_gG_proyecto.setText(str);
                
            }
            
        }else            

            jT_gG_proyecto.setText("");
        
        exiteGestion();
        
    }//GEN-LAST:event_jCombo_gG_cod_proyectoItemStateChanged

    private void jB_gG_insertar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_gG_insertar1MouseClicked
        // TODO add your handling code here:
        
         try{  
             
            if(jB_gG_insertar1.isEnabled()){
            
                Gestion g = rellenarGestion(null); 
            
                if(g != null){
                
                    if(Hibernate_AD.insertGestion(g)){

                        JOptionPane.showMessageDialog(null, "Datos de gestion global agregados correctamente");            

                        limpiarGestion();

                    }else

                        JOptionPane.showMessageDialog(null, "No se ha podido grabar la nueva gestion global");    

                }
            
            }
            
        }
        catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
    }//GEN-LAST:event_jB_gG_insertar1MouseClicked

    private void jB_proyecto_gG_ejecutar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_proyecto_gG_ejecutar1MouseClicked
        // TODO add your handling code here:
          try{
            
            dtm_gestion.setNumRows(0); 
            
            this.arr_gestiones = Hibernate_AD.findAllGestiones();
        
            if(arr_gestiones != null && arr_gestiones.size() > 0){                
                
                arr_gestiones.stream().forEach((cur_gestion) -> {
                    pintarGestionTabla(cur_gestion);
                });
                            
            }else{
                
                JOptionPane.showMessageDialog(null, "No se encuentran gestiones");    
                
            }               
            
        }catch(Exception ex){
            
            Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);
            
        }        
    }//GEN-LAST:event_jB_proyecto_gG_ejecutar1MouseClicked

    private void jB_gG_limpiar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_gG_limpiar1MouseClicked
        // TODO add your handling code here:
        limpiarGestion();
    }//GEN-LAST:event_jB_gG_limpiar1MouseClicked

    private void jB_gG_modificar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_gG_modificar1MouseClicked
        // TODO add your handling code here:
        
        if(jB_gG_modificar1.isEnabled()){
            
            if(mod_gestion != null){            

                Gestion g = rellenarGestion(mod_gestion.getGestionPK());

                if(g != null){

                    try {

                        if(Hibernate_AD.updateGestion(g)){

                            JOptionPane.showMessageDialog(null, "Datos de gestion (" + g.getGestionPK().getCodproveedor() + " - " 
                            + g.getGestionPK().getCodpieza() + " - " + g.getGestionPK().getCodproyecto() + ") modificados correctamente");

                            limpiarGestion();

                        }else

                            JOptionPane.showMessageDialog(null, "Los datos del gestion (" + g.getGestionPK().getCodproveedor() + " - " 
                            + g.getGestionPK().getCodpieza() + " - " + g.getGestionPK().getCodproyecto() + ") no se pudieron modifcar");

                    } catch (Exception ex) {

                        Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

            }
            
        }
        
    }//GEN-LAST:event_jB_gG_modificar1MouseClicked

    private void jB_gG_eliminar_2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jB_gG_eliminar_2MouseClicked
        // TODO add your handling code here:
        
        if(jB_gG_eliminar_2.isEnabled()){
            
            if(mod_gestion != null){     
            
                try {
                    if(Hibernate_AD.deleteGestion(mod_gestion)){

                        JOptionPane.showMessageDialog(this, "Gestion (" + mod_gestion.getGestionPK().getCodproveedor() + " - " 
                            + mod_gestion.getGestionPK().getCodpieza() + " - " + mod_gestion.getGestionPK().getCodproyecto() + ")  eliminado correcamente.");

                        limpiarGestion();

                    }else

                        JOptionPane.showMessageDialog(this, "Gestion (" + mod_gestion.getGestionPK().getCodproveedor() + " - " 
                            + mod_gestion.getGestionPK().getCodpieza() + " - " + mod_gestion.getGestionPK().getCodproyecto() + ")  no se ha podido eliminar.");

                } catch (IllegalOrphanException ex) {

                    Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

                } catch (NonexistentEntityException ex) {

                    Logger.getLogger(VistaGestionProyectos.class.getName()).log(Level.SEVERE, null, ex);

                }

             }
            
        }
        
    }//GEN-LAST:event_jB_gG_eliminar_2MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaGestionProyectos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaGestionProyectos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaGestionProyectos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaGestionProyectos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaGestionProyectos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JT_pManager;
    private javax.swing.JButton jB_gG_eliminar_2;
    private javax.swing.JButton jB_gG_insertar1;
    private javax.swing.JButton jB_gG_limpiar1;
    private javax.swing.JButton jB_gG_modificar1;
    private javax.swing.JButton jB_gG_recarga;
    private javax.swing.JButton jB_pieza_ant;
    private javax.swing.JButton jB_pieza_eject_consulta;
    private javax.swing.JButton jB_pieza_eliminar_01;
    private javax.swing.JButton jB_pieza_eliminar_02;
    private javax.swing.JButton jB_pieza_filtro_ejecutar1;
    private javax.swing.JButton jB_pieza_final;
    private javax.swing.JButton jB_pieza_inicio;
    private javax.swing.JButton jB_pieza_insertar;
    private javax.swing.JButton jB_pieza_limpiar1;
    private javax.swing.JButton jB_pieza_modificar;
    private javax.swing.JButton jB_pieza_sigui;
    private javax.swing.JButton jB_prov_ant;
    private javax.swing.JButton jB_prov_eject_consulta;
    private javax.swing.JButton jB_prov_eliminar_01;
    private javax.swing.JButton jB_prov_eliminar_02;
    private javax.swing.JButton jB_prov_filtro_ejecutar;
    private javax.swing.JButton jB_prov_final;
    private javax.swing.JButton jB_prov_inicio;
    private javax.swing.JButton jB_prov_insertar;
    private javax.swing.JButton jB_prov_limpiar;
    private javax.swing.JButton jB_prov_modificar;
    private javax.swing.JButton jB_prov_sigui;
    private javax.swing.JButton jB_proyecto_ant;
    private javax.swing.JButton jB_proyecto_eject_consulta;
    private javax.swing.JButton jB_proyecto_eliminar_01;
    private javax.swing.JButton jB_proyecto_eliminar_02;
    private javax.swing.JButton jB_proyecto_filtro_ejecutar;
    private javax.swing.JButton jB_proyecto_final;
    private javax.swing.JButton jB_proyecto_gG_ejecutar1;
    private javax.swing.JButton jB_proyecto_inicio;
    private javax.swing.JButton jB_proyecto_insertar;
    private javax.swing.JButton jB_proyecto_limpiar;
    private javax.swing.JButton jB_proyecto_modificar;
    private javax.swing.JButton jB_proyecto_sigui;
    private javax.swing.JComboBox jComboBox_pieza_filtro;
    private javax.swing.JComboBox jComboBox_prov_filtro;
    private javax.swing.JComboBox jComboBox_proyecto_filtro;
    private javax.swing.JComboBox<String> jCombo_gG_cod_pieza;
    private javax.swing.JComboBox<String> jCombo_gG_cod_prov;
    private javax.swing.JComboBox<String> jCombo_gG_cod_proyecto;
    private javax.swing.JLabel jL_prov_apellidos_err;
    private javax.swing.JLabel jL_prov_apellidos_err1;
    private javax.swing.JLabel jL_prov_apellidos_err2;
    private javax.swing.JLabel jL_prov_cod_err;
    private javax.swing.JLabel jL_prov_cod_err1;
    private javax.swing.JLabel jL_prov_cod_err2;
    private javax.swing.JLabel jL_prov_dir_err;
    private javax.swing.JLabel jL_prov_dir_err1;
    private javax.swing.JLabel jL_prov_dir_err2;
    private javax.swing.JLabel jL_prov_dir_err3;
    private javax.swing.JLabel jL_prov_nombre_err;
    private javax.swing.JLabel jL_prov_nombre_err1;
    private javax.swing.JLabel jL_prov_nombre_err2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField jT_gG_cantidad;
    private java.awt.Label jT_gG_pieza;
    private java.awt.Label jT_gG_proveedor;
    private java.awt.Label jT_gG_proyecto;
    private javax.swing.JTextArea jT_gG_sms_error1;
    private javax.swing.JTabbedPane jT_gestionGlobal;
    private javax.swing.JTextField jT_pieza_cod_01;
    private java.awt.Label jT_pieza_cod_02;
    private javax.swing.JTextField jT_pieza_desc_01;
    private java.awt.Label jT_pieza_desc_02;
    private javax.swing.JTextField jT_pieza_filtro;
    private javax.swing.JLabel jT_pieza_max;
    private java.awt.Label jT_pieza_min;
    private javax.swing.JTextField jT_pieza_nombre_01;
    private java.awt.Label jT_pieza_nombre_02;
    private javax.swing.JTextField jT_pieza_precio_01;
    private java.awt.Label jT_pieza_precio_02;
    private javax.swing.JTextArea jT_pieza_sms_error;
    private javax.swing.JTabbedPane jT_piezas;
    private javax.swing.JTextField jT_prov_apellidos_01;
    private java.awt.Label jT_prov_apellidos_02;
    private javax.swing.JTextField jT_prov_cod_01;
    private java.awt.Label jT_prov_cod_02;
    private javax.swing.JTextField jT_prov_dir_01;
    private java.awt.Label jT_prov_dir_02;
    private javax.swing.JTextField jT_prov_filtro;
    private javax.swing.JLabel jT_prov_max;
    private java.awt.Label jT_prov_min;
    private javax.swing.JTextField jT_prov_nombre_01;
    private java.awt.Label jT_prov_nombre_02;
    private javax.swing.JTextArea jT_prov_sms_error;
    private javax.swing.JTabbedPane jT_proveedores;
    private javax.swing.JTextField jT_proyecto_ciudad_01;
    private java.awt.Label jT_proyecto_ciudad_02;
    private javax.swing.JTextField jT_proyecto_cod_01;
    private java.awt.Label jT_proyecto_cod_02;
    private javax.swing.JTextField jT_proyecto_filtro;
    private javax.swing.JLabel jT_proyecto_max;
    private java.awt.Label jT_proyecto_min;
    private javax.swing.JTextField jT_proyecto_nombre_01;
    private java.awt.Label jT_proyecto_nombre_02;
    private javax.swing.JTextArea jT_proyecto_sms_error;
    private javax.swing.JTabbedPane jT_proyectos;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane10;
    private javax.swing.JTabbedPane jTabbedPane11;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTabbedPane jTabbedPane6;
    private javax.swing.JTabbedPane jTabbedPane7;
    private javax.swing.JTabbedPane jTabbedPane8;
    private javax.swing.JTabbedPane jTabbedPane9;
    private javax.swing.JTable jTable_gG;
    private javax.swing.JTable jTable_pieza;
    private javax.swing.JTable jTable_prov;
    private javax.swing.JTable jTable_proyecto;
    private javax.swing.JTextArea jTextArea1;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.Label label26;
    private java.awt.Label label27;
    private java.awt.Label label28;
    private java.awt.Label label29;
    private java.awt.Label label3;
    private java.awt.Label label30;
    private java.awt.Label label31;
    private java.awt.Label label32;
    private java.awt.Label label33;
    private java.awt.Label label34;
    private java.awt.Label label35;
    private java.awt.Label label36;
    private java.awt.Label label37;
    private java.awt.Label label38;
    private java.awt.Label label39;
    private java.awt.Label label4;
    private java.awt.Label label40;
    private java.awt.Label label41;
    private java.awt.Label label42;
    private java.awt.Label label43;
    private java.awt.Label label44;
    private java.awt.Label label5;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.Label label8;
    private java.awt.Label label9;
    // End of variables declaration//GEN-END:variables
}
