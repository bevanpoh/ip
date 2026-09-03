package am.ui;

import am.Am;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML controller for the main AM conversation window.
 */
public class MainWindow extends AnchorPane {
    private static final String WELCOME_MESSAGE = "My name is AM.\nWhat do you want?";

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private Am am;

    /** Creates an empty controller for FXMLLoader to populate. */
    public MainWindow() {
    }

    /**
     * Installs the listener that keeps the conversation scrolled to its newest message.
     */
    @FXML
    private void initialize() {
        dialogContainer.heightProperty().addListener(observable -> scrollToLatestMessage());
    }

    /**
     * Connects the window to the chatbot and displays its welcome message.
     *
     * @param am chatbot that processes commands and persists tasks
     */
    public void setAm(Am am) {
        this.am = am;
        appendDialog(DialogBox.getAmDialog(WELCOME_MESSAGE));
        userInput.requestFocus();
    }

    /** Processes a command entered in the text field or submitted by the button. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        String response = am.getResponse(input);
        appendDialog(DialogBox.getUserDialog(input));
        appendDialog(DialogBox.getAmDialog(response));
        userInput.clear();

        if (am.isExitRequested()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }

    /**
     * Adds a new message to the conversation.
     *
     * @param dialogBox message box to add
     */
    private void appendDialog(DialogBox dialogBox) {
        dialogContainer.getChildren().add(dialogBox);
    }

    /** Scrolls the conversation to the newest message after its layout changes. */
    private void scrollToLatestMessage() {
        scrollPane.setVvalue(1.0);
    }
}
