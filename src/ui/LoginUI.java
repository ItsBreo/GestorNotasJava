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
        setTitle("Iniciar Sesión");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Correo electrónico:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Iniciar Sesión");
        registerButton = new JButton("Registrarse");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (UserManager.authenticateUser(email, password)) {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
                    dispose(); // Cerrar la ventana de login
                    new NoteApp(email);
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cerrar la ventana actual
                new RegisterUI(); // Abrir la pantalla de registro
            }
        });

        add(loginButton);
        add(registerButton);

        setVisible(true);
    }
}