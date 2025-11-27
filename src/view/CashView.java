package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import main.Shop;

public class CashView extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField textFieldCash;

    /**
     * Create the dialog.
     */
    public CashView(Shop shop) {
        // Configuración básica de la ventana
        setTitle("Caja - Balance");
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        
        // Evitar que se pierda el foco detrás
        setModal(true); 
        setLocationRelativeTo(null);

        // Etiqueta "Dinero en caja"
        JLabel lblCash = new JLabel("Dinero total en caja:");
        lblCash.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblCash.setBounds(49, 50, 196, 28);
        contentPanel.add(lblCash);

        // Campo de texto con el valor
        textFieldCash = new JTextField();
        textFieldCash.setHorizontalAlignment(SwingConstants.RIGHT);
        textFieldCash.setEditable(false); // Mejor setEditable(false) que setEnabled(false) para que se lea mejor
        textFieldCash.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textFieldCash.setBounds(210, 55, 120, 25);
        contentPanel.add(textFieldCash);
        textFieldCash.setColumns(10);

        // --- LÓGICA PARA OBTENER EL DINERO ---
        // shop.getCash() devuelve un objeto Amount. Necesitamos .getValue()
        if (shop.getCash() != null) {
            double value = shop.getCash().getValue();
            textFieldCash.setText(String.format("%.2f €", value));
        } else {
            textFieldCash.setText("0.00 €");
        }

        // --- BOTONES (Descomentados y arreglados) ---
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("Cerrar");
                okButton.setActionCommand("OK");
                // Añadimos la acción para cerrar la ventana
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose(); // Cierra esta ventana
                    }
                });
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }
    }
}