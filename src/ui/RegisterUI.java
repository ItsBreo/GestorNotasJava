package ui;

import logic.UserManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterUI extends JFrame {
    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private JButton registerButton, backButton;

    public RegisterUI() {
        // Configuración de la ventana
        setTitle("Creador de Notas - Registro");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        // Panel de color suave para todo el contenido
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 240, 240));
        add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Título
        JLabel titleLabel = new JLabel("Creador de Notas - Registro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(50, 50, 50));
        panel.add(titleLabel, gbc);

        // Campo de nombre de usuario
        gbc.gridy = 1;
        panel.add(new JLabel("Nombre de usuario:"), gbc);
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Campo de correo electrónico
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Correo electrónico:"), gbc);
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        // Campo de contraseña
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Contraseña:"), gbc);
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Botón de registro
        gbc.gridy = 4;
        registerButton = new JButton("Registrarse");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                boolean success = UserManager.registerUser(username, email, password);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Registro exitoso. Inicie sesión.");
                    dispose();
                    new LoginUI();
                } else {
                    JOptionPane.showMessageDialog(null, "Error en el registro, compruebe los datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(registerButton, gbc);

        // Botón de volver
        gbc.gridy = 5;
        backButton = new JButton("Volver");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(220, 53, 69));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {dispose(); new LoginUI();});
        panel.add(backButton, gbc);

        // Centrar todo
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterUI::new);
    }
}