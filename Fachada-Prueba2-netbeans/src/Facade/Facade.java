/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facade;

import GUI.JavaApplication2;
import controladores.ClientesJpaController;
import controladores.OficinasJpaController;
import entidades.Clientes;
import entidades.Oficinas;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author oscar
 */
public class Facade {

    ClientesJpaController controladorCliente;
    OficinasJpaController controladorOficinas;

    public Facade() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaApplication2PU");
        this.controladorCliente = new ClientesJpaController(emf);
        this.controladorOficinas = new OficinasJpaController(emf);
    }

    public void crearCliente(String nombre, String apellido, String fechaNacimiento, String genero, String codigo) {
        Clientes cliente = new Clientes();
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setFechaNacimiento(new Date(fechaNacimiento));
        cliente.setGenero(genero);
        cliente.setCodigoCliente(Short.valueOf(codigo));

        try {
            this.controladorCliente.create(cliente);
            System.out.println("Se ha creado el cliente: " + nombre);
        } catch (Exception ex) {
            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listarClientes() {
        List<Clientes> clientes = this.controladorCliente.findClientesEntities();

        System.out.println("------------------------------------------------------------");
        System.out.println("Se encontraron los siguientes clientes: ");
        clientes.forEach((cliente) -> {
            System.out.println(cliente.getCodigoCliente() + " " + cliente.getNombre() + " " + cliente.getApellido() + " " + cliente.getGenero() + " " + cliente.getFechaNacimiento());
        });
        System.out.println("------------------------------------------------------------");

    }

    public void crearOficina(String codigo, String nombre) {
        Oficinas oficina = new Oficinas();
        oficina.setCodigoOficina(Short.valueOf(codigo));
        oficina.setNombre(nombre);

        try {
            this.controladorOficinas.create(oficina);
            System.out.println("Se ha creado la oficina: " + nombre);
        } catch (Exception ex) {
            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void listarOficinas() {
        List<Oficinas> oficinas = this.controladorOficinas.findOficinasEntities();
        System.out.println("------------------------------------------------------------");
        System.out.println("Se encontraron los siguientes oficinas: ");
        oficinas.forEach((oficina) -> {
            System.out.println(oficina.getCodigoOficina() + " " + oficina.getNombre());
        });
        System.out.println("------------------------------------------------------------");
    }

}
