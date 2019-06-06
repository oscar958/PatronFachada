/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Facade.Facade;


/**
 *
 * @author oscar
 */
public class JavaApplication2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Facade facade;
        
        facade = new Facade();
        
        facade.crearCliente("Oscar", "Salamanca", "04/10/1995", "Masculino", "1");
        facade.crearCliente("Sergio", "Salamanca", "10/20/1990", "Masculino", "2");
        facade.crearCliente("Fabian", "Salamanca", "12/28/1992", "Masculino", "3");
        facade.crearCliente("Julian", "Salamanca", "12/07/1993", "Masculino", "4");
        facade.crearCliente("Maria", "Salamanca", "10/05/2002", "Femenino", "5");
        facade.crearCliente("Camilo", "Moreno", "12/18/1995", "Masculino", "6");
        facade.crearCliente("David", "Laverde", "01/10/2000", "Masculino", "7");
        facade.crearCliente("Teresa", "Chacón", "03/08/1971", "Femenino", "8");
        facade.crearCliente("Sergio", "Salamanca", "04/06/1971", "Masculino", "9");
        facade.crearCliente("José", "Chacón", "01/10/1980", "Masculino", "10"); 
       
       facade.listarClientes();
       
       facade.crearOficina("1", "El Retiro");
        facade.crearOficina("2", "RTI");
        facade.crearOficina("3", "Globant");
        facade.crearOficina("4", "Acceture");
        facade.crearOficina("5", "Grability");
        facade.crearOficina("6", "DevCode");
        facade.crearOficina("7", "Platzi");
        facade.crearOficina("8", "Tesla");
        facade.crearOficina("9", "SolarCity");
        facade.crearOficina("10", "BMW");
        
        facade.listarOficinas();
       
    }
    
}
