package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Employee;
import model.Product;
import model.Amount; 

public class DaoImplFile implements Dao {

    private final String INPUT_FILE = "files/inputInventory.txt";

    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> productList = new ArrayList<>();
        System.out.println("Intentando leer inventario de: " + INPUT_FILE);

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                
                String[] parts = line.split(";");
                
                String name = parts[0].split(":")[1].trim();
                double priceValue = Double.parseDouble(parts[1].split(":")[1].trim());
                int stock = Integer.parseInt(parts[2].split(":")[1].trim());

                Amount wholesalerPrice = new Amount(priceValue);
                boolean available = (stock > 0);
                Product product = new Product(name, wholesalerPrice, available, stock); 
                
                productList.add(product);
            }
            System.out.println("Inventario leído correctamente. Productos cargados: " + productList.size());

        } catch (Exception e) {
            System.err.println("!!! ERROR CRÍTICO AL LEER INVENTARIO !!!");
            e.printStackTrace(); 
            System.err.println("-------------------------------------------");
        }
        
        return productList;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        
        String date = LocalDate.now().toString();
        String fileName = "files/inventory_" + date + ".txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) { 
            
            for (int i = 0; i < inventory.size(); i++) { 
                Product p = inventory.get(i);
                String line = (i + 1) + ";Product: " + p.getName() + "; Stock:" + p.getStock() + ";";
                bw.write(line); 
                bw.newLine(); 
            }
            
            bw.write("Total number of products:" + inventory.size() + ";");
            bw.newLine();
            
            return true; 

        } catch (Exception e) { 
            System.err.println("!!! ERROR CRÍTICO AL ESCRIBIR INVENTARIO !!!");
            e.printStackTrace(); 
            System.err.println("-------------------------------------------");
            return false; 
        }
    }

    // --- MÉTODOS SIN IMPLEMENTACIÓN ---
    
    @Override
    public void connect() { }

    @Override
    public void disconnect() { }

    @Override
    public Employee getEmployee(int employeeId, String pw) {
        return null;
    }
}