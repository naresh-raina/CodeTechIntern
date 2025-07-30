import java.io.*;
import java.util.*;

public class FileOperationsDemo {

    static final String FILE_NAME = "sample.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // Menu
            System.out.println("\n--- File Operations Menu ---");
            System.out.println("1. Write to File");
            System.out.println("2. Read from File");
            System.out.println("3. Modify File Content");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear newline

            switch (choice) {
                case 1:
                    System.out.print("Enter text to write into the file:\n> ");
                    String text = scanner.nextLine();
                    writeToFile(text);
                    break;
                case 2:
                    System.out.println("File contents:");
                    readFromFile();
                    break;
                case 3:
                    System.out.print("Enter word to replace:\n> ");
                    String oldWord = scanner.nextLine();
                    System.out.print("Enter new word:\n> ");
                    String newWord = scanner.nextLine();
                    modifyFileContent(oldWord, newWord);
                    break;
                case 4:
                    System.out.println(" Exiting program.");
                    break;
                default:
                    System.out.println(" Invalid choice! Please enter 1-4.");
            }

        } while (choice != 4);

        scanner.close();
    }

    // Method to write to file (overwrite)
    public static void writeToFile(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(text);
            System.out.println(" Text written to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println(" Error writing file: " + e.getMessage());
        }
    }

    // Method to read from file
    public static void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null)
                System.out.println(line);
        } catch (IOException e) {
            System.err.println(" Error reading file: " + e.getMessage());
        }
    }

    // Method to modify file content
    public static void modifyFileContent(String oldWord, String newWord) {
        try {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line.replaceAll(oldWord, newWord)).append("\n");
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                writer.write(content.toString());
            }
            System.out.println(" File modified successfully.");
        } catch (IOException e) {
            System.err.println(" Error modifying file: " + e.getMessage());
        }
    }
}
// This Java program demonstrates basic file operations such as writing to a file, reading from a file, and modifying file content.