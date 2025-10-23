package dao;

import java.util.ArrayList;
import model.Employee;
import model.Product;

/**
 * Interface Dao.
 * Contiene los métodos que implementarán las clases DaoImplFile y DaoImplJDBC.
 */
public interface Dao {

    public void connect(); //
    
    public void disconnect(); //

    public Employee getEmployee(int employeeId, String pw); //

    /**
     * NUEVO: Obtiene la lista de productos del inventario.
     * @return ArrayList de productos.
     */
    public ArrayList<Product> getInventory(); //

    /**
     * NUEVO: Escribe la lista de productos del inventario a un fichero.
     * @param inventory La lista de productos a escribir.
     * @return true si la escritura fue exitosa, false si hubo un error.
     */
    public boolean writeInventory(ArrayList<Product> inventory); //

}