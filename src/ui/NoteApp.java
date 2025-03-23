package ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.*;

public class NoteApp extends JFrame {
    private JTextField titleField, searchField;
    private JTextArea contentArea;
    private JList<String> noteList;
    private DefaultListModel<String> listModel;
    private JButton saveButton, editButton, deleteButton, clearButton, searchButton, logoutButton;
    private JCheckBox autoSaveCheckBox;
    private Timer autoSaveTimer;
    private String currentUser;

    public NoteApp(String user) {
        currentUser = user;
        setTitle("Creador de Notas - " + currentUser);
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Creamos el modelo de la lista de notas
        listModel = new DefaultListModel<>();
        noteList = new JList<>(listModel);
        noteList.setFont(new Font("Arial", Font.PLAIN, 14));
        noteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        noteList.setBackground(new Color(245, 245, 245));

        // Cargar las notas del usuario
        loadUserNotes();

        // Panel principal para todo el contenido
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Panel para los campos de entrada (título y contenido)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.setBackground(new Color(255, 255, 255));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título (cambio a línea simple)
        titleField = new JTextField();
        titleField.setFont(new Font("Arial", Font.BOLD, 14));
        titleField.setBackground(new Color(245, 245, 245));
        titleField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        titleField.setPreferredSize(new Dimension(400, 30));

        // Línea separadora
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));

        // Contenido (campo grande de texto)
        contentArea = new JTextArea(10, 30);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBackground(new Color(245, 245, 245));
        contentArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        contentArea.setPreferredSize(new Dimension(400, 200));

        inputPanel.add(new JLabel("Título:"), BorderLayout.NORTH);
        inputPanel.add(titleField, BorderLayout.CENTER);
        inputPanel.add(separator, BorderLayout.SOUTH);
        inputPanel.add(new JScrollPane(contentArea), BorderLayout.SOUTH);

        // Panel de la lista de notas
        JPanel noteListPanel = new JPanel();
        noteListPanel.setLayout(new BorderLayout(10, 10));
        noteListPanel.setBackground(new Color(245, 245, 245));

        noteListPanel.add(new JScrollPane(noteList), BorderLayout.CENTER);

        // Panel de botones para las acciones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        saveButton = new JButton("Guardar");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(0, 123, 255));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setPreferredSize(new Dimension(120, 35));

        editButton = new JButton("Editar");
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setBackground(new Color(255, 159, 28));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setPreferredSize(new Dimension(120, 35));

        deleteButton = new JButton("Eliminar");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setPreferredSize(new Dimension(120, 35));

        clearButton = new JButton("Limpiar");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setPreferredSize(new Dimension(120, 35));

        logoutButton = new JButton("Cerrar sesión");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setPreferredSize(new Dimension(120, 35));

        // Añadimos el checkbox para habilitar o deshabilitar el auto-guardado
        autoSaveCheckBox = new JCheckBox("Guardar automáticamente");
        autoSaveCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        autoSaveCheckBox.setBackground(new Color(245, 245, 245));

        buttonPanel.add(saveButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(autoSaveCheckBox);

        // Panel de búsqueda y cierre de sesión
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(245, 245, 245));

        searchButton = new JButton("Buscar");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setPreferredSize(new Dimension(120, 35));

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(200, 30));

        topPanel.add(new JLabel("Buscar notas:"), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        // Añadimos todos los paneles al panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(noteListPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Configurar el botón de cerrar sesión
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginUI();
        });

        // Acción del checkbox de guardar automáticamente
        autoSaveCheckBox.addActionListener(e -> {
            if (autoSaveCheckBox.isSelected()) {
                autoSaveTimer.restart();
            } else {
                autoSaveTimer.stop();
            }
        });

        // Guardar automáticamente después de 2 segundos de inactividad (si está activado el checkbox)
        autoSaveTimer = new Timer(2000, e -> saveNote());
        contentArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (autoSaveCheckBox.isSelected()) {
                    autoSaveTimer.restart();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (autoSaveCheckBox.isSelected()) {
                    autoSaveTimer.restart();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (autoSaveCheckBox.isSelected()) {
                    autoSaveTimer.restart();
                }
            }
        });

        // Acciones de los botones
        saveButton.addActionListener(e -> saveNote());
        clearButton.addActionListener(e -> clearFields());
        deleteButton.addActionListener(e -> deleteNote());
        editButton.addActionListener(e -> editNote());
        searchButton.addActionListener(e -> searchNote());

        setVisible(true);
    }

    private void saveNote() {
        String title = titleField.getText();
        String content = contentArea.getText();

        if (!title.isEmpty() && !content.isEmpty()) {
            try {
                // Guardar las notas del usuario en un archivo
                File userFolder = new File("data/usuarios/" + currentUser);
                if (!userFolder.exists()) {
                    userFolder.mkdirs();
                }

                File noteFile = new File(userFolder, title + ".txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(noteFile));
                writer.write(content);
                writer.close();

                listModel.addElement(title); // Añadir el título de la nota a la lista
                clearFields();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        titleField.setText("");
        contentArea.setText("");
    }

    private void loadUserNotes() {
        File userFolder = new File("data/usuarios/" + currentUser);
        if (userFolder.exists() && userFolder.isDirectory()) {
            File[] noteFiles = userFolder.listFiles((dir, name) -> name.endsWith(".txt"));
            for (File file : noteFiles) {
                listModel.addElement(file.getName().replace(".txt", ""));
            }
        }
    }

    private void deleteNote() {
        String selectedNote = noteList.getSelectedValue();
        if (selectedNote != null) {
            File userFolder = new File("data/usuarios/" + currentUser);
            File noteFile = new File(userFolder, selectedNote + ".txt");

            if (noteFile.delete()) {
                listModel.removeElement(selectedNote);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la nota.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editNote() {
        String selectedNote = noteList.getSelectedValue();
        if (selectedNote != null) {
            File userFolder = new File("data/usuarios/" + currentUser);
            File noteFile = new File(userFolder, selectedNote + ".txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(noteFile))) {
                String content = reader.readLine();
                titleField.setText(selectedNote);
                contentArea.setText(content);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar la nota para edición.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchNote() {
        String query = searchField.getText().toLowerCase();
        listModel.clear();

        File userFolder = new File("data/usuarios/" + currentUser);
        if (userFolder.exists() && userFolder.isDirectory()) {
            File[] noteFiles = userFolder.listFiles((dir, name) -> name.endsWith(".txt"));
            for (File file : noteFiles) {
                String noteTitle = file.getName().replace(".txt", "");
                if (noteTitle.toLowerCase().contains(query)) {
                    listModel.addElement(noteTitle);
                }
            }
        }
    }

    public static void main(String[] args) {
        new LoginUI(); // Iniciar con el login
    }
}