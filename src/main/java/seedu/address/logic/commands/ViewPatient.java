package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

public class ViewPatient extends ViewCommand {

    public static final String COMMAND_OBJECT = "pat";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "-" + COMMAND_OBJECT
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + "-" + COMMAND_OBJECT + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Index targetIndex;

    public ViewPatient(Index targetIndex) {
        super(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToView = lastShownList.get(targetIndex.getZeroBased());
        Predicate<Person> predicate = p -> p.equals(personToView);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(String.format(MESSAGE_VIEW_PERSON_SUCCESS, personToView));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewPatient // instanceof handles nulls
                && targetIndex.equals(((ViewPatient) other).targetIndex)); // state check
    }
}
