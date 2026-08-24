import java.util.Scanner;

public class AM {
    private static final String INDENT = "    ";
    private static final String SEPARATOR = "_________________________________________________________________";
    private static final String BANNER = " ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░\n"
            + "░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░\n"
            + "░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░\n"
            + "░▒▓████████▓▒░▒▓████████▓▒░▒▓████████▓▒░▒▓████████▓▒░▒▓████████▓░\n"
            + "░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░\n"
            + "░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░\n"
            + "░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░\n";
    private static final String WELCOME = "My name is AM.\nWhat do you want?";
    private static final String FAREWELL = "You may leave.\nI will be here.";

    private static String insertIndent(String msg) {
        return INDENT + msg.replace("\n", "\n" + INDENT);
    }

    private static void printResponse(String... messages) {
        System.out.println(insertIndent(SEPARATOR));
        for (String message : messages) {
            System.out.println(insertIndent(message));
        }
        System.out.println(insertIndent(SEPARATOR));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printResponse(BANNER, WELCOME);

        while (true) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                printResponse(FAREWELL);
                break;
            }
            printResponse(command);
        }
    }
}
