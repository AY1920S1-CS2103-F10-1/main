package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ListAppointments;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListPatients;
import seedu.address.logic.parser.exceptions.ParseException;

public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String commandObject, String args) throws ParseException {
        switch (commandObject) {
            case ListPatients.COMMAND_OBJECT:
                return new ListPatients();
            case ListAppointments.COMMAND_OBJECT:
                return new ListAppointments();
            default:
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }

    }
}
