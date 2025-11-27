package model;

public class Product {
    // AÑADIR ESTE ATRIBUTO ID
    private int id;
    
    // Atributos existentes
    private String name;
    private Amount publicPrice;
    private Amount wholesalerPrice;
    private boolean available;
    private int stock;
    private static int totalProducts; // Si lo usas para lógica interna antigua

    // CONSTRUCTOR 
    public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
        this.name = name;
        this.wholesalerPrice = wholesalerPrice;
        this.publicPrice = new Amount(wholesalerPrice.getValue() * 2); // Ejemplo de lógica de precio
        this.available = available;
        this.stock = stock;
        totalProducts++;
    }

    // --- AÑADIR ESTOS MÉTODOS PARA EL ID ---
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ---------------------------------------

    // Resto de tus getters y setters existentes (getName, getStock, etc.)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Amount getWholesalerPrice() { return wholesalerPrice; }
    public void setWholesalerPrice(Amount wholesalerPrice) { this.wholesalerPrice = wholesalerPrice; }
    
    public Amount getPublicPrice() { return publicPrice; }
    public void setPublicPrice(Amount publicPrice) { this.publicPrice = publicPrice; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}