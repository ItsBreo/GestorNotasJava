package ui;

import logic.NoteManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class NoteApp extends JFrame {
    private String userEmail;
    private JTextField titleField, searchField;
    private JTextArea contentArea;
    private JList<String> noteList;
    private DefaultListModel<String> listModel;
    private JButton saveButton, deleteButton, clearButton, logoutButton;
    private JCheckBox autoSaveCheck;
    private boolean autoSaveEnabled = false;

    public NoteApp(String userEmail) {
        this.userEmail = userEmail;
        setTitle("Creador de Notas - " + userEmail);
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        noteList = new JList<>(listModel);
        noteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        noteList.addListSelectionListener(e -> loadSelectedNote());

        titleField = new JTextField(20);
        contentArea = new JTextArea(10, 20);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);

        contentArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (autoSaveEnabled) saveNote();
            }
        });

        searchField = new JTextField(15);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                searchNotes();
            }
        });

        saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> saveNote());

        deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(e -> deleteNote());

        clearButton = new JButton("Limpiar");
        clearButton.addActionListener(e -> clearFields());

        logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> logout());

        autoSaveCheck = new JCheckBox("Guardar automáticamente");
        autoSaveCheck.addItemListener(e -> autoSaveEnabled = autoSaveCheck.isSelected());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Título:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Contenido:"));
        inputPanel.add(new JScrollPane(contentArea));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(autoSaveCheck);
        buttonPanel.add(logoutButton);

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Buscar:"));
        searchPanel.add(searchField);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(noteList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(searchPanel, BorderLayout.EAST);

        loadNotes();
        setVisible(true);
    }

    private void loadNotes() {
        listModel.clear();
        List<String> notes = NoteManager.loadNotes(userEmail);
        for (String note : notes) {
            listModel.addElement(note.split(":")[0]); // Solo mostramos el título
        }
    }

    private void loadSelectedNote() {
        String selectedTitle = noteList.getSelectedValue();
        if (selectedTitle != null) {
            titleField.setText(selectedTitle);
            contentArea.setText(NoteManager.loadNoteContent(userEmail, selectedTitle));
        }
    }

    private void saveNote() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título y contenido no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        NoteManager.saveNote(userEmail, title, content);
        if (!listModel.contains(title)) listModel.addElement(title);
        JOptionPane.showMessageDialog(this, "Nota guardada correctamente.");
    }

    private void deleteNote() {
        String selectedTitle = noteList.getSelectedValue();
        if (selectedTitle != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta nota?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                NoteManager.deleteNote(userEmail, selectedTitle);
                listModel.removeElement(selectedTitle);
                clearFields();
            }
        }
    }

    private void clearFields() {
        titleField.setText("");
        contentArea.setText("");
    }

    private void searchNotes() {
        String query = searchField.getText().trim().toLowerCase();
        listModel.clear();
        List<String> notes = NoteManager.loadNotes(userEmail);
        for (String note : notes) {
            if (note.toLowerCase().contains(query)) {
                listModel.addElement(note.split(":")[0]);
            }
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginUI();
        }
    }

    public static void main(String[] args) {
        // Aseguramos que la interfaz gráfica se ejecute en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new LoginUI());
    }
}
