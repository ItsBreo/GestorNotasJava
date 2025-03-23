package logic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.List;

public class UserManager {
    private static final String USERS_FILE = "data/users.txt";
    private static final String USERS_DIRECTORY = "data/usuarios/";

    // Método para registrar un usuario
    public static boolean registerUser(String username, String email, String password) {
        System.out.println("Recibiendo datos para registrar: " + username + ", " + email);

        if (username.isEmpty() || email.isEmpty() || password.length() < 6) {
            System.out.println("Error: Campos vacíos o contraseña muy corta.");
            return false;
        }

        File userFile = new File(USERS_FILE);
        if (!userFile.exists()) {
            try {
                Files.createDirectories(Paths.get("data")); // Crear carpeta data si no existe
                userFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creando el archivo users.txt");
                e.printStackTrace();
                return false;
            }
        }

        if (userExists(email)) {
            System.out.println("Error: Usuario ya existe.");
            return false;
        }

        String hashedPassword = hashPassword(password);
        System.out.println("Contraseña hasheada: " + hashedPassword);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(email + ":" + hashedPassword);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al escribir en users.txt");
            e.printStackTrace();
            return false;
        }

        try {
            Files.createDirectories(Paths.get(USERS_DIRECTORY + email));
        } catch (IOException e) {
            System.out.println("Error creando la carpeta del usuario.");
            e.printStackTrace();
            return false;
        }

        System.out.println("Registro exitoso para: " + email);
        return true;
    }

    // Método para verificar si un usuario ya existe
    private static boolean userExists(String email) {
        try {
            if (!Files.exists(Paths.get(USERS_FILE))) {
                return false; // Si el archivo no existe, no hay usuarios registrados
            }
            List<String> lines = Files.readAllLines(Paths.get(USERS_FILE), StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(email)) {
                    return true; // Usuario ya registrado
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para autenticar un usuario con email y contraseña
    public static boolean authenticateUser(String email, String password) {
        if (!Files.exists(Paths.get(USERS_FILE))) {
            System.out.println("Error: No hay usuarios registrados.");
            return false;
        }

        String hashedPassword = hashPassword(password);

        try {
            List<String> lines = Files.readAllLines(Paths.get(USERS_FILE), StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(email) && parts[1].equals(hashedPassword)) {
                    System.out.println("Inicio de sesión exitoso para: " + email);
                    return true; // Usuario autenticado correctamente
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Error: Credenciales incorrectas.");
        return false;
    }

    // Método para hashear contraseñas con SHA-256
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear la contraseña");
        }
    }
}
