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
        setTitle("Registro de Usuario");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Nombre de usuario:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Correo electrónico:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        add(passwordField);

        registerButton = new JButton("Registrarse");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                System.out.println("Intentando registrar: " + email);

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

        add(registerButton);

        backButton = new JButton("Volver");
        backButton.addActionListener(e -> {
            dispose();
            new LoginUI();
        });

        add(backButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterUI::new);
    }
}
