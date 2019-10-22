package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import seedu.address.autocomplete.AutoCompleteWord;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {
    private static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private final CommandExecutor commandExecutor;
    private final AutoCompletePanel autoCompletePanel;

    @FXML
    private TextField commandTextField;

    public CommandBox(CommandExecutor commandExecutor, AutoCompletePanel autoCompletePanel) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.autoCompletePanel = autoCompletePanel;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        try {
            commandExecutor.execute(commandTextField.getText());
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

    public void setOnButtonPressedListener() {
        commandTextField.setOnKeyPressed(event -> {
            int newIndex;
            switch (event.getCode()) {
            case UP:
                autoCompletePanel.setSelected(autoCompletePanel.getSelectedIndex() - 1);
                commandTextField.positionCaret(commandTextField.getText().length());
                break;
            case DOWN:
                autoCompletePanel.setSelected(autoCompletePanel.getSelectedIndex() + 1);
                commandTextField.positionCaret(commandTextField.getText().length());
                break;
            case RIGHT:
                try {
                    StringBuilder textInTextField = new StringBuilder();
                    for (AutoCompleteWord autoCompleteWord : autoCompletePanel.getMatchedWordsList()) {
                        textInTextField
                                .append(autoCompleteWord.getSuggestedWord())
                                .append(autoCompleteWord.getConnectorChar());
                    }
                    textInTextField.append(autoCompletePanel.getSelected().getSuggestedWord());
                    commandTextField.setText(textInTextField.toString());
                    commandTextField.positionCaret(commandTextField.getText().length());

                    autoCompletePanel.updateListView(textInTextField.toString());

                } catch (NullPointerException e) {
                    logger.info("Nothing is selected thus left key does not work");
                }
                break;
            default:
                autoCompletePanel.updateListView(commandTextField.getText() + event.getText());
            }
        });
    }
}
