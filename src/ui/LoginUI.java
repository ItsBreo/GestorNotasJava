package ui;

import logic.UserManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginUI() {
        // Configuración de la ventana
        setTitle("Creador de Notas - Inicio de Sesión");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        // Panel con fondo suave
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 240, 240));
        add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Título
        JLabel titleLabel = new JLabel("Creador de Notas - Inicio de Sesión");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(50, 50, 50));
        panel.add(titleLabel, gbc);

        // Campo de correo electrónico
        gbc.gridy = 1;
        panel.add(new JLabel("Correo electrónico:"), gbc);
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        // Campo de contraseña
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Contraseña:"), gbc);
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Botón de inicio de sesión
        gbc.gridy = 3;
        loginButton = new JButton("Iniciar sesión");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                boolean success = UserManager.authenticateUser(email, password);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Bienvenido a Creador de Notas.");
                    dispose();
                    new NoteApp(email);
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas, intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(loginButton, gbc);

        // Botón de registro
        gbc.gridy = 4;
        registerButton = new JButton("Registrarse");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(40, 167, 69));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> {dispose(); new RegisterUI();});
        panel.add(registerButton, gbc);

        // Centrar todo
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }
}
