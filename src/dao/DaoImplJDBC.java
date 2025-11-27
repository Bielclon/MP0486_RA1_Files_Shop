package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplJDBC implements Dao {
    private Connection connection;

    @Override
    public void connect() {
        // Define credentials
        String url = "jdbc:mysql://localhost:3306/shop";
        String user = "root";
        String pass = ""; // PON AQUI TU CONTRASEÑA
        try {
            this.connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> inventory = new ArrayList<>();
        String query = "SELECT * FROM inventory"; // 
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                // Se asume que Product tiene un constructor adecuado o setters
                Product p = new Product(
                    rs.getString("name"), 
                    new Amount(rs.getDouble("wholesalerPrice")), 
                    rs.getBoolean("available"), 
                    rs.getInt("stock")
                );
                p.setId(rs.getInt("id")); // Importante para luego editar/borrar
                inventory.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventory;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        // Exportar a historical_inventory 
        String query = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (Product p : inventory) {
                ps.setInt(1, p.getId());
                ps.setString(2, p.getName());
                ps.setDouble(3, p.getWholesalerPrice().getValue());
                ps.setBoolean(4, p.isAvailable());
                ps.setInt(5, p.getStock());
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void addProduct(Product product) {
        // Insertar nuevo registro 
        String query = "INSERT INTO inventory (name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getWholesalerPrice().getValue());
            ps.setBoolean(3, product.isAvailable());
            ps.setInt(4, product.getStock());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        // Actualizar stock 
        String query = "UPDATE inventory SET stock = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, product.getStock());
            ps.setInt(2, product.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(int productId) {
        // Eliminar registro 
        String query = "DELETE FROM inventory WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        // USUARIO HARDCODEADO DE PRUEBA
        // Credenciales -> ID: 123, Password: test
        if (employeeId == 123 && password.equals("test")) {
            // Creamos un empleado "falso" en memoria para dejar pasar el login
            Employee e = new Employee("Usuario de Pruebas"); 
            // Si tu constructor de Employee está vacío, usa: Employee e = new Employee(); e.setName("Usuario Pruebas");
            return e;
        }
        
        // Si no coincide, devolvemos null (login fallido)
        return null;
    }
}