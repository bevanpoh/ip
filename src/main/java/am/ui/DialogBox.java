package am.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Represents one message in the conversation, loaded from a reusable FXML view.
 */
public class DialogBox extends HBox {
    private static final String AM_AVATAR = "A";
    private static final String USER_AVATAR = "Y";

    @FXML
    private Label dialog;

    @FXML
    private Label avatar;

    /**
     * Creates a message box and loads its reusable FXML layout.
     *
     * @param message message text to display
     * @param isUser whether the message was entered by the user
     */
    private DialogBox(String message, boolean isUser) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the dialog-box view.", exception);
        }

        dialog.setText(message);
        dialog.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(dialog, Priority.ALWAYS);
        avatar.setText(isUser ? USER_AVATAR : AM_AVATAR);
        getStyleClass().add(isUser ? "user-dialog" : "am-dialog");

        if (isUser) {
            flip();
        }
    }

    /**
     * Creates a message box containing user input.
     *
     * @param message user input to display
     * @return a right-aligned user message box
     */
    public static DialogBox getUserDialog(String message) {
        return new DialogBox(message, true);
    }

    /**
     * Creates a message box containing AM's response.
     *
     * @param message response to display
     * @return a left-aligned AM message box
     */
    public static DialogBox getAmDialog(String message) {
        return new DialogBox(message, false);
    }

    /** Reverses the child order and aligns the user message to the right. */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_RIGHT);
    }
}
