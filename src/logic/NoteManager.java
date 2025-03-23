package logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NoteManager {
    public static List<String> loadNotes(String userEmail) {
        List<String> notes = new ArrayList<>();
        File userDir = new File("data/usuarios/" + userEmail);
        if (!userDir.exists()) return notes;

        for (File file : userDir.listFiles()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                notes.add(file.getName().replace(".txt", "") + ": " + br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return notes;
    }

    public static String loadNoteContent(String userEmail, String title) {
        File file = new File("data/usuarios/" + userEmail + "/" + title + ".txt");
        if (!file.exists()) return "";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void saveNote(String userEmail, String title, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/usuarios/" + userEmail + "/" + title + ".txt"))) {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteNote(String userEmail, String title) {
        File file = new File("data/usuarios/" + userEmail + "/" + title + ".txt");
        if (file.exists()) {
            file.delete();
        }
    }
}