package dao;

import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao {
    private MongoClient mongoClient;
    private MongoDatabase database;

    @Override
    public void connect() {
        // Conexión por defecto a localhost en el puerto 27017
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase("shop");
    }

    @Override
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> inventory = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("inventory");
        
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                
                // Obtener el objeto anidado de precio
                Document priceDoc = (Document) doc.get("wholesalerPrice");
                double priceValue = priceDoc != null ? priceDoc.getDouble("value") : 0.0;
                
                Product p = new Product(
                    doc.getString("name"),
                    new Amount(priceValue),
                    doc.getBoolean("available", true),
                    doc.getInteger("stock", 0)
                );
                
                // Si existe un campo 'id', se lo asignamos al producto
                if (doc.getInteger("id") != null) {
                    p.setId(doc.getInteger("id"));
                }
                inventory.add(p);
            }
        }
        return inventory;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        try {
            MongoCollection<Document> collection = database.getCollection("historical_inventory");
            for (Product p : inventory) {
                Document priceDoc = new Document("value", p.getWholesalerPrice().getValue())
                                        .append("currency", "€");
                
                Document doc = new Document("id", p.getId())
                                    .append("name", p.getName())
                                    .append("wholesalerPrice", priceDoc)
                                    .append("available", p.isAvailable())
                                    .append("stock", p.getStock())
                                    .append("created_at", new Date()); // Añade la fecha actual
                
                collection.insertOne(doc);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void addProduct(Product product) {
        MongoCollection<Document> collection = database.getCollection("inventory");
        
        Document priceDoc = new Document("value", product.getWholesalerPrice().getValue())
                                .append("currency", "€");
        
        // Mongo autogenera _id, pero insertamos también el 'id' relacional según la muestra
        Document doc = new Document("id", product.getId()) 
                            .append("name", product.getName())
                            .append("wholesalerPrice", priceDoc)
                            .append("available", product.isAvailable())
                            .append("stock", product.getStock());
                            
        collection.insertOne(doc);
    }

    @Override
    public void updateProduct(Product product) {
        MongoCollection<Document> collection = database.getCollection("inventory");
        
        // Filtramos por nombre o id para actualizar
        Document query = new Document("name", product.getName()); 
        Document update = new Document("$set", new Document("stock", product.getStock()));
        
        collection.updateOne(query, update);
    }

    @Override
    public void deleteProduct(int productId) {
        MongoCollection<Document> collection = database.getCollection("inventory");
        
        // Eliminamos buscando por el 'id' del producto
        Document query = new Document("id", productId);
        collection.deleteOne(query);
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        MongoCollection<Document> collection = database.getCollection("users");
        
        // Filtro para encontrar credenciales
        Document query = new Document("employeeId", employeeId).append("password", password);
        Document result = collection.find(query).first();
        
        if (result != null) {
            // Creamos un empleado si el usuario existe en Mongo
            Employee employee = new Employee(
                result.getInteger("employeeId", employeeId), 
                result.getString("name") != null ? result.getString("name") : "Usuario Autenticado", 
                result.getString("password")
            );
            return employee;
        }
        
        return null; // Retorna nulo si no se encuentra
    }
}