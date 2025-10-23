package view;
import model.Amount; // Para formatear el dinero
import model.Product; // Para la lista de inventario
import java.util.ArrayList; // Para la lista de inventario
import javax.swing.JTextArea; // Para mostrar el inventario en una lista
import javax.swing.JScrollPane; // Para añadir scroll a la lista
import java.awt.Dimension; // Para darle tamaño a la lista
//--- FIN DE IMPORTS ---

// Imports de Swing para la interfaz gráfica
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

// Imports de AWT para el layout y los eventos
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Import de la clase principal que maneja la lógica
import main.Shop;

/**
 * Clase ShopView
 * * Representa la ventana principal (JFrame) del menú de la tienda.
 * Implementa ActionListener para manejar clics de botones.
 * Implementa KeyListener para manejar atajos de teclado.
 */
public class ShopView extends JFrame implements ActionListener, KeyListener {

    // Atributo para comunicarse con la lógica de la tienda (controlador/modelo)
    private Shop shop;
    
    // Atributos para los componentes de la interfaz (los botones)
    JButton exportButton;
    JButton countButton;
    JButton addButton;
    JButton viewButton;
    JButton exitButton;

    /**
     * Constructor de ShopView
     * @param shop La instancia de la clase Shop que contiene la lógica.
     */
    public ShopView(Shop shop) {
        this.shop = shop; // Guarda la referencia al objeto Shop
        
        // --- 1. Configuración de la Ventana (JFrame) ---
        this.setTitle("Menú Principal de la Tienda");
        this.setSize(350, 450); // Tamaño (ancho, alto)
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la app al cerrar la ventana
        this.setLocationRelativeTo(null); // Centra la ventana en la pantalla

        // --- 2. Creación del Panel y Definición del Layout ---
        // Se crea un panel para contener todos los botones
        JPanel buttonPanel = new JPanel();
        
        // Se usa GridLayout (5 filas, 1 columna) para apilar los botones verticalmente.
        // Los '10' y '10' añaden un espacio (gap) de 10 píxeles entre los botones.
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));

        // --- 3. Creación y Configuración de los Botones ---

        // Botón 0: Exportar inventario
        exportButton = new JButton("0. Exportar inventario");
        exportButton.setActionCommand("0");      // Identificador para el listener
        exportButton.addActionListener(this); // La propia clase escuchará el clic
        buttonPanel.add(exportButton);          // Añadir el botón al panel

        // Botón 1: Contar caja
        countButton = new JButton("1. Contar caja");
        countButton.setActionCommand("1");
        countButton.addActionListener(this);
        buttonPanel.add(countButton);

        // Botón 2: Añadir Stock (Nombre asumido)
        addButton = new JButton("2. Añadir Stock");
        addButton.setActionCommand("2");
        addButton.addActionListener(this);
        buttonPanel.add(addButton);

        // Botón 3: Ver Inventario (Nombre asumido)
        viewButton = new JButton("3. Ver Inventario");
        viewButton.setActionCommand("3");
        viewButton.addActionListener(this);
        buttonPanel.add(viewButton);
        
        // Botón 9: Salir
        exitButton = new JButton("9. Salir");
        exitButton.setActionCommand("9");
        exitButton.addActionListener(this);
        buttonPanel.add(exitButton);
        
        // --- 4. Añadir el Panel a la Ventana ---
        // Se añade el panel (que ya contiene todos los botones) al JFrame
        this.add(buttonPanel);
        
        // --- 5. Configuración del KeyListener ---
        // Se le dice al JFrame que escuche los eventos de teclado
        this.addKeyListener(this);
        // Es necesario que la ventana sea "focusable" para que reciba los eventos de tecla
        this.setFocusable(true);
        this.requestFocusInWindow(); // Pide el foco al iniciar
    }

    /**
     * Método que se dispara cuando se hace clic en un botón.
     * Requerido por la interfaz ActionListener.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Obtiene el "ActionCommand" que definimos (ej: "0", "1", "9")
        String option = e.getActionCommand();

        // switch para manejar la opción seleccionada
        switch (option) {
            
            case "0": // Exportar inventario
                boolean success = shop.writeInventory(); // Llama a la lógica
                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Inventario exportado correctamente",
                            "Informacion",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error exportando el inventario",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "1": // Contar caja
                // 1. Llama al método getCash() de tu clase Shop
                double dineroEnCaja = shop.getCash();
                
                // 2. Usa tu clase Amount para formatear el dinero
                Amount dineroFormateado = new Amount(dineroEnCaja);

                // 3. Muestra el resultado real en el JOptionPane
                JOptionPane.showMessageDialog(this,
                        "Dinero actual en caja: " + dineroFormateado.toString(),
                        "Contar Caja",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "2": // Añadir Stock
                try {
                    // 1. Preguntar por el producto
                    String nombreProducto = JOptionPane.showInputDialog(this,
                            "Introduce el nombre del producto:",
                            "Añadir Stock",
                            JOptionPane.QUESTION_MESSAGE);
                    
                    // Si el usuario no cancela
                    if (nombreProducto != null && !nombreProducto.trim().isEmpty()) {
                        // 2. Buscar el producto
                        Product producto = shop.findProduct(nombreProducto);
                        
                        if (producto != null) {
                            // 3. Preguntar la cantidad
                            String cantidadStr = JOptionPane.showInputDialog(this,
                                    "Producto: " + producto.getName() + "\nStock actual: " + producto.getStock() + "\n\n¿Cantidad a añadir?",
                                    "Añadir Stock",
                                    JOptionPane.QUESTION_MESSAGE);
                            
                            if (cantidadStr != null) {
                                // 4. Actualizar el stock
                                int cantidadAAñadir = Integer.parseInt(cantidadStr);
                                if (cantidadAAñadir > 0) {
                                    producto.setStock(producto.getStock() + cantidadAAñadir);
                                    JOptionPane.showMessageDialog(this,
                                            "Stock actualizado. Nuevo stock: " + producto.getStock(),
                                            "Stock Actualizado",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(this, "La cantidad debe ser positiva.", "Error", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        } else {
                            // Si el producto no se encuentra
                            JOptionPane.showMessageDialog(this,
                                    "Producto no encontrado: " + nombreProducto,
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    // Si el usuario no introduce un número en la cantidad
                    JOptionPane.showMessageDialog(this,
                            "Cantidad no válida. Introduce solo números.",
                            "Error de Formato",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;
                
            case "3": // Ver Inventario
                // 1. Obtener la lista de productos
                ArrayList<Product> inventario = shop.getInventory();
                
                if (inventario == null || inventario.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El inventario está vacío.", "Inventario", JOptionPane.INFORMATION_MESSAGE);
                    break; // Salir del case
                }
                
                // 2. Construir un String largo con todos los productos
                StringBuilder sb = new StringBuilder("--- INVENTARIO ACTUAL ---\n\n");
                for (Product p : inventario) {
                    sb.append("ID: ").append(p.getId()).append("\n");
                    sb.append("Nombre: ").append(p.getName()).append("\n");
                    sb.append("Precio: ").append(p.getPublicPrice().toString()).append("\n");
                    sb.append("Stock: ").append(p.getStock()).append("\n");
                    sb.append("----------------------------\n");
                }
                
                // 3. Crear un JTextArea para mostrar el texto
                JTextArea textArea = new JTextArea(sb.toString());
                textArea.setEditable(false); // Para que no se pueda editar
                
                // 4. Meter el JTextArea en un JScrollPane (para poder hacer scroll)
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(350, 400)); // Tamaño de la ventana
                
                // 5. Mostrar el panel con scroll en el JOptionPane
                JOptionPane.showMessageDialog(this,
                        scrollPane,
                        "Ver Inventario",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "9": // Salir
                JOptionPane.showMessageDialog(this, "Saliendo de la aplicación...");
                System.exit(0); // Cierra la aplicación
                break;
        }
        
        // Importante: Devolver el foco a la ventana después de un clic,
        // para que los atajos de teclado sigan funcionando.
        this.requestFocusInWindow();
    }
    
    // --- Métodos Requeridos por la interfaz KeyListener ---
    
    /**
     * Método que se dispara cuando se presiona una tecla.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Obtiene el carácter de la tecla presionada
        char key = e.getKeyChar();
        
        // switch para manejar el atajo de teclado
        switch (key) {
            case '0':
                exportButton.doClick(); // Simula un clic en el botón 0
                break;
            case '1':
                countButton.doClick(); // Simula un clic en el botón 1
                break;
            case '2':
                addButton.doClick(); // Simula un clic en el botón 2
                break;
            case '3':
                viewButton.doClick(); // Simula un clic en el botón 3
                break;
            case '9':
                exitButton.doClick(); // Simula un clic en el botón 9
                break;
        }
    }

    /**
     * No se usa, pero es requerido por la interfaz.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // No necesitamos implementar esto
    }

    /**
     * No se usa, pero es requerido por la interfaz.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // No necesitamos implementar esto
    }
}