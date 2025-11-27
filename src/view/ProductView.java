package view;

import main.Shop;
import model.Amount;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProductView extends JDialog implements ActionListener {
    
    private Shop shop;
    private int option;
    
    // Componentes de la interfaz
    private JTextField inputName;
    private JTextField inputStock;
    private JTextField inputPrice;
    private JComboBox<String> comboProducts; // Para seleccionar productos existentes
    private JButton okButton;
    private JButton cancelButton;

    public ProductView(Shop shop, int option) {
        this.shop = shop;
        this.option = option;
        
        setTitle("Gestión de Productos");
        setSize(400, 300);
        setLayout(new GridLayout(0, 2, 10, 10)); // Layout de rejilla
        setModal(true); // Bloquea la ventana de atrás
        
        initComponents();
        
        // Centrar en pantalla
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Opción 2: AÑADIR NUEVO PRODUCTO
        if (option == 2) {
            add(new JLabel("Nombre del producto:"));
            inputName = new JTextField();
            add(inputName);

            add(new JLabel("Stock inicial:"));
            inputStock = new JTextField();
            add(inputStock);

            add(new JLabel("Precio Mayorista:"));
            inputPrice = new JTextField();
            add(inputPrice);
        } 
        // Opción 3 (Stock) y 9 (Eliminar): NECESITAN SELECCIONAR PRODUCTO EXISTENTE
        else if (option == 3 || option == 9) {
            add(new JLabel("Seleccionar Producto:"));
            comboProducts = new JComboBox<>();
            
            // Rellenar el combo con los nombres de los productos del inventario
            ArrayList<Product> inventory = shop.getInventory();
            for (Product p : inventory) {
                comboProducts.addItem(p.getName());
            }
            add(comboProducts);

            // Si es opción 3, necesitamos campo para cantidad a añadir
            if (option == 3) {
                add(new JLabel("Cantidad a añadir:"));
                inputStock = new JTextField();
                add(inputStock);
            }
        }

        // Botones comunes
        okButton = new JButton("OK");
        okButton.addActionListener(this);
        add(okButton);

        cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(this);
        add(cancelButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            this.dispose();
            return;
        }

        if (e.getSource() == okButton) {
            try {
                // --- OPCIÓN 2: AÑADIR PRODUCTO ---
                if (option == 2) {
                    String name = inputName.getText();
                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío");
                        return;
                    }
                    
                    // Verificamos que los campos numéricos no estén vacíos antes de parsear
                    if (inputStock.getText().isEmpty() || inputPrice.getText().isEmpty()) {
                         JOptionPane.showMessageDialog(this, "Debes rellenar precio y stock");
                         return;
                    }

                    int stock = Integer.parseInt(inputStock.getText());
                    double priceVal = Double.parseDouble(inputPrice.getText());
                    Amount price = new Amount(priceVal);

                    // Invocar metodo shop.addProduct
                    shop.addProduct(name, price, true, stock);
                    
                    JOptionPane.showMessageDialog(this, "Producto añadido correctamente a la BD.");
                    this.dispose(); // Cerrar ventana tras éxito
                } 
                
                // --- OPCIÓN 3: AÑADIR STOCK ---
                else if (option == 3) {
                    Product selectedProduct = getSelectedProduct();
                    if (selectedProduct != null) {
                        if (inputStock.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Introduce cantidad");
                            return;
                        }
                        int stockToAdd = Integer.parseInt(inputStock.getText());
                        
                        // Invocar metodo shop.updateProduct
                        shop.updateProduct(selectedProduct, stockToAdd);
                        
                        JOptionPane.showMessageDialog(this, "Stock actualizado en BD.");
                        this.dispose(); // Cerrar ventana tras éxito
                    } else {
                         JOptionPane.showMessageDialog(this, "Debes seleccionar un producto.");
                    }
                } 
                
                // --- OPCIÓN 9: ELIMINAR PRODUCTO ---
                else if (option == 9) {
                    Product selectedProduct = getSelectedProduct();
                    if (selectedProduct != null) {
                        // Confirmación antes de borrar
                        int confirm = JOptionPane.showConfirmDialog(this, 
                            "¿Estás seguro de eliminar " + selectedProduct.getName() + "?",
                            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                            
                        if (confirm == JOptionPane.YES_OPTION) {
                            // Invocar metodo shop.deleteProduct
                            shop.deleteProduct(selectedProduct);
                            JOptionPane.showMessageDialog(this, "Producto eliminado de la BD.");
                            this.dispose(); // Cerrar ventana tras éxito
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Debes seleccionar un producto.");
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Introduce números válidos en precio/stock.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    // Método auxiliar para obtener el objeto Product seleccionado en el ComboBox
    private Product getSelectedProduct() {
        if (comboProducts == null || comboProducts.getSelectedItem() == null) return null;
        
        String name = (String) comboProducts.getSelectedItem();
        return shop.findProduct(name); // Requiere que tengas el método findProduct en Shop
    }
}