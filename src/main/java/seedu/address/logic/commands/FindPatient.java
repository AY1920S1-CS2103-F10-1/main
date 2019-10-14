package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindPatient extends FindCommand {

    public static final String COMMAND_OBJECT = "pat";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "-" + COMMAND_OBJECT
            + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + "-" + COMMAND_OBJECT + " alice bob charlie";

    private final NameContainsKeywordsPredicate predicate;

    public FindPatient(NameContainsKeywordsPredicate predicate) {
        super(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindPatient // instanceof handles nulls
                && predicate.equals(((FindPatient) other).predicate)); // state check
    }
}
