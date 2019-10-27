package cs.f10.t1.nursetraverse.logic.parser.visit;

import cs.f10.t1.nursetraverse.commons.core.Messages;
import cs.f10.t1.nursetraverse.commons.core.index.Index;
import cs.f10.t1.nursetraverse.logic.commands.visit.BeginVisitCommand;
import cs.f10.t1.nursetraverse.logic.parser.Parser;
import cs.f10.t1.nursetraverse.logic.parser.ParserUtil;
import cs.f10.t1.nursetraverse.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new BeginVisitCommand object
 */
public class BeginVisitCommandParser implements Parser<BeginVisitCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the BeginVisitCommand
     * and returns a BeginVisitCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public BeginVisitCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new BeginVisitCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, BeginVisitCommand.MESSAGE_USAGE), pe);
        }
    }
}
