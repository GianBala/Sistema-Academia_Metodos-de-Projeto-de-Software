package br.edu.academia.ui.console;

import java.util.Scanner;

public class ConsoleUtils {

    private final Scanner scanner;

    public ConsoleUtils(Scanner scanner) {
        this.scanner = scanner;
    }

    public void clearConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    public String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public void waitForEnter() {
        System.out.println();
        readInput("Pressione Enter para continuar...");
    }

    public void printHeader(String title) {
        System.out.println("========== " + title + " ==========");
    }

    public void printSeparator(String title) {
        System.out.println("=" + "=".repeat(29) + " " + title + " " + "=".repeat(29));
    }
}
