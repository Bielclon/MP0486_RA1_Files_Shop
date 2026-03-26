package dao;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;

import model.Employee;
import model.Product;

public class DaoImplObjectDB implements Dao {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void connect() {
        // Conecta o crea la base de datos orientada a objetos
        emf = Persistence.createEntityManagerFactory("objects/users.odb");
        em = emf.createEntityManager();
    }

    @Override
    public void disconnect() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }	

    @Override
    public Employee getEmployee(int employeeId, String password) {
        Employee employee = null;
        try {
            // Consulta JPQL (ObjectDB soporta JPA) buscando por id y password
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.employeeId = :empId AND e.password = :pass", Employee.class);
            query.setParameter("empId", employeeId);
            query.setParameter("pass", password);
            
            employee = query.getSingleResult(); // Retorna el empleado si lo encuentra
            
        } catch (NoResultException e) {
            // Si no encuentra resultados, devolvemos null para que falle el login
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return employee;
    }

    // --- El resto de los métodos se dejan vacíos ya que esta práctica solo afecta al Login ---

    @Override
    public ArrayList<Product> getInventory() { return null; }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) { return false; }

    @Override
    public void addProduct(Product product) {}

    @Override
    public void updateProduct(Product product) {}

    @Override
    public void deleteProduct(int productId) {}
}