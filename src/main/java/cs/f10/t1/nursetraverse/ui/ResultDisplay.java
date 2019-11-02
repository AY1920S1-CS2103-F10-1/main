package cs.f10.t1.nursetraverse.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> implements Observer {

    private static final String FXML = "ResultDisplay.fxml";

    @FXML
    private TextArea resultDisplay;

    public ResultDisplay() {
        super(FXML);
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
    }

    @Override
    public void update(KeyCode keyCode) {
    }

    @Override
    public void update(KeyCode keyCode, String resultString) {
        if (keyCode == KeyCode.SHIFT) {
            resultDisplay.setText(resultString);
        }
    }
}
