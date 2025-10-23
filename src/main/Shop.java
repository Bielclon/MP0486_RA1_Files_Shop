package main; 

import java.util.ArrayList;
import dao.DaoImplFile;     // Importa la clase del paquete 'dao'
import model.Product;       // Importa la clase del paquete 'model'
import model.Employee;      // (Asegúrate de importar todo lo que Shop necesite)
// (otros imports)

public class Shop {

    private ArrayList<Product> inventory;
    private double cash;
    // (otros atributos)
    
    private DaoImplFile dao; // Atributo DAO
    
    
    /**
     * Constructor de Shop.
     */
    public Shop() {
        this.cash = 100.00;
        this.inventory = new ArrayList<Product>();
        
        this.dao = new DaoImplFile(); // Inicializa el DAO
    }
    
    public double getCash() { //
        return this.cash; //
    }
    
    public Product findProduct(String name) {
        // Recorremos la lista de inventario
        for (Product product : this.inventory) {
            // Comparamos el nombre (ignorando mayúsculas/minúsculas)
            if (product.getName().equalsIgnoreCase(name)) {
                return product; // ¡Encontrado! Lo devolvemos.
            }
        }
        
        // Si el bucle termina, no se encontró
        return null; 
    }
    public void addProduct(Product product) {
        this.inventory.add(product);
    }
        

    /**
     * MÉTODO MODIFICADO: readInventory.
     * Delega la carga del inventario al DAO.
     */
    public void readInventory() {
        // Ahora solo llama al DAO
        this.inventory = dao.getInventory(); //
    }

    /**
     * MÉTODO NUEVO: writeInventory.
     * Delega la escritura del inventario al DAO.
     * @return boolean true si éxito, false si error.
     */
    public boolean writeInventory() { //
        // Llama al método del DAO y devuelve su resultado
        return dao.writeInventory(this.inventory); 
    }
    
    // --- getters y setters ---
    public ArrayList<Product> getInventory() {
        return inventory;
    }

    // (resto de métodos de la clase Shop: addProduct, findProduct, getCash, etc.)
}