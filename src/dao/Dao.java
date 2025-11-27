package dao;

import java.util.ArrayList;
import model.Employee;
import model.Product;

import java.util.ArrayList;
import model.Employee;
import model.Product;

public interface Dao {
    public void connect();
    public void disconnect();
    public ArrayList<Product> getInventory();
    public boolean writeInventory(ArrayList<Product> inventory);
    public Employee getEmployee(int employeeId, String password);
    
    // Nuevos métodos requeridos por la práctica
    public void addProduct(Product product);
    public void updateProduct(Product product);
    public void deleteProduct(int productId);
}