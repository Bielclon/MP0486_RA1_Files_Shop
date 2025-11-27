package view;

import main.Shop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopView extends JFrame implements ActionListener {

    private Shop shop;
    
    // Declaración de los botones
    private JButton btnCash;
    private JButton btnAddProduct;
    private JButton btnAddStock;
    private JButton btnDeleteProduct;
    private JButton btnExport;

    public ShopView(Shop shop) {
        this.shop = shop;
        
        // Configuración de la ventana
        setTitle("Menú Principal - Tienda");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Usamos GridLayout de una sola columna para apilar los botones
        setLayout(new GridLayout(6, 1, 10, 10)); 
        
        // Borde vacío alrededor para estética
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- 1. Título ---
        JLabel titleLabel = new JLabel("Menú de la Tienda", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel);

        // --- 2. Crear y añadir botones ---
        
        // Botón Caja
        btnCash = new JButton("1. Abrir Caja (Cash)");
        btnCash.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCash.addActionListener(this);
        add(btnCash);

        // Botón Añadir Producto
        btnAddProduct = new JButton("2. Añadir Producto");
        btnAddProduct.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAddProduct.addActionListener(this);
        add(btnAddProduct);

        // Botón Añadir Stock
        btnAddStock = new JButton("3. Añadir Stock");
        btnAddStock.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAddStock.addActionListener(this);
        add(btnAddStock);

        // Botón Eliminar Producto
        btnDeleteProduct = new JButton("9. Eliminar Producto");
        btnDeleteProduct.setFont(new Font("Arial", Font.PLAIN, 14));
        btnDeleteProduct.addActionListener(this);
        add(btnDeleteProduct);
        
        // Botón Exportar
        btnExport = new JButton("0. Exportar Inventario (Backup)");
        btnExport.setFont(new Font("Arial", Font.BOLD, 14));
        btnExport.setBackground(new Color(220, 230, 240)); 
        btnExport.addActionListener(this); 
        add(btnExport);

        // Centrar en pantalla
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnCash) {
            // --- AQUÍ ESTÁ EL CAMBIO ---
            CashView cashView = new CashView(shop);
            cashView.setVisible(true);
            // ---------------------------
        
        } else if (source == btnAddProduct) {
            openProductView(2); 
            
        } else if (source == btnAddStock) {
            openProductView(3); 
            
        } else if (source == btnDeleteProduct) {
            openProductView(9); 
            
        } else if (source == btnExport) {
            shop.writeInventory();
            JOptionPane.showMessageDialog(this, "Inventario exportado correctamente a la base de datos.");
        }
    }

    /**
     * Método auxiliar para abrir la ventana de productos
     */
    public void openProductView(int option) {
        ProductView productView = new ProductView(shop, option);
        productView.setVisible(true);
    }
}