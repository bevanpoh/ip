package am.ui;

import java.util.Scanner;

/** Handles console input and formatted output for the task manager. */
public class Ui {
    private static final String INDENT = "    ";
    private static final String SEPARATOR = "_________________________________________________________________";
    private static final String BANNER = """
             ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░
            ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░
            ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░
            ░▒▓████████▓▒░▒▓████████▓▒░▒▓████████▓▒░▒▓████████▓▒░▒▓████████▓░
            ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░
            ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░
            ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░""";

    private final Scanner scanner;

    /** Creates a user interface connected to standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Prints a message between the application's separator lines. */
    public void printResponse(String message) {
        System.out.println(insertIndent(SEPARATOR));
        System.out.println(insertIndent(message));
        System.out.println(insertIndent(SEPARATOR));
    }

    /** Prints an error message using the standard response format. */
    public void showError(String message) {
        printResponse(message);
    }

    /** Reads the next command line from standard input. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Prints the application's welcome banner and prompt. */
    public void showWelcome() {
        printResponse(String.format("%s\nMy name is AM.\nWhat do you want?", BANNER));
    }

    private static String insertIndent(String message) {
        return INDENT + message.replace("\n", "\n" + INDENT);
    }
}
