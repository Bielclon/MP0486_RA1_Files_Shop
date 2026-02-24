package main;

import model.Amount;
import model.Product;
import model.Sale;
import model.Employee; // Asegúrate de tener esta clase si la usas
import java.util.ArrayList;
import java.util.Scanner;
import dao.DaoImplMongoDB;
import dao.Dao;
import dao.DaoImplJDBC;

public class Shop {

    private Amount cash;
    public ArrayList<Product> inventory;
    private ArrayList<Sale> sales;
    
    // Atributo DAO modificado según 
    private Dao dao; 

    public Shop() {
        this.cash = new Amount(100.00); // Caja inicial
        this.inventory = new ArrayList<>();
        this.sales = new ArrayList<>();
        
        // Inicialización usando la implementación JDBC 
        this.dao = new DaoImplMongoDB();
    }

    public static void main(String[] args) {
        Shop shop = new Shop();
        
        // Cargamos inventario desde la base de datos al iniciar
        shop.loadInventory(); 
        
        // Iniciamos la vista gráfica
        // ShopView view = new ShopView(shop); // Descomenta esto si ya tienes la vista lista
        // view.setVisible(true);
    }

    public void loadInventory() {
        // Conectar y obtener inventario de la BD 
        dao.connect();
        this.inventory = dao.getInventory();
    }

    public void writeInventory() {
        // Exportar inventario actual a la tabla histórica 
        dao.connect();
        dao.writeInventory(this.inventory);
        dao.disconnect();
    }

    /**
     * Método modificado para insertar en base de datos
     * 
     */
    public void addProduct(String name, Amount wholesalerPrice, boolean available, int stock) {
        Product product = new Product(name, wholesalerPrice, available, stock);
        
        // Guardar en base de datos
        dao.connect();
        dao.addProduct(product);
        
        // Opción A: Recargar todo el inventario para asegurar que tenemos el ID generado por la BD
        this.inventory = dao.getInventory();
        
        // Opción B (Más rápida pero riesgosa si necesitas el ID inmediatamente):
        // this.inventory.add(product); 
        
        dao.disconnect();
    }

    /**
     * Nuevo método para actualizar stock en base de datos
     * 
     */
    public void updateProduct(Product product, int stock) {
        // Actualizar en memoria (objeto Java)
        // Nota: Asumimos que el stock que recibimos es la cantidad A AÑADIR, no el total.
        // Si recibes el total directo, cambia la lógica a: product.setStock(stock);
        int newStock = product.getStock() + stock;
        product.setStock(newStock);
        
        // Actualizar en base de datos
        dao.connect();
        dao.updateProduct(product);
        dao.disconnect();
    }

    /**
     * Nuevo método para eliminar producto de la base de datos
     * 
     */
    public void deleteProduct(Product product) {
        // Eliminar de base de datos usando el ID del producto
        dao.connect();
        dao.deleteProduct(product.getId());
        dao.disconnect();
        
        // Eliminar de la lista en memoria
        this.inventory.remove(product);
    }

    // Getters necesarios para la vista
    public Amount getCash() {
        return cash;
    }

    public ArrayList<Product> getInventory() {
        return inventory;
    }
    
    // Método auxiliar para buscar un producto por nombre (útil para validaciones)
    public Product findProduct(String name) {
        for (Product product : inventory) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }
    
    // Métodos de lógica de caja (Cash) si los necesitas aquí o en su propia clase
    public void initSession() {
        // Lógica de login si es necesaria, delegando en dao.getEmployee si aplica
    	
    	
    }
}