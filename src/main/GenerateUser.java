package main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Employee;

public class GenerateUser {
    public static void main(String[] args) {
        // Conecta a la base de datos (se creará automáticamente si no existe)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("objects/users.odb");
        EntityManager em = emf.createEntityManager();

        // Inicia transacción
        em.getTransaction().begin();
        
        // Creamos el usuario de prueba con ID 123 y Password "test"
        Employee e = new Employee(123, "Usuario Prueba", "test");
        
        // Lo guardamos en ObjectDB
        em.persist(e); 
        
        // Confirmamos la transacción
        em.getTransaction().commit(); 
        
        System.out.println("Usuario 123 con password 'test' creado con éxito en objects/users.odb");
        
        // Cerramos conexiones
        em.close();
        emf.close();
    }
}