package cs.f10.t1.nursetraverse.logic.parser;

import static cs.f10.t1.nursetraverse.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cs.f10.t1.nursetraverse.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static cs.f10.t1.nursetraverse.logic.parser.CliSyntax.PREFIX_EMAIL;
import static cs.f10.t1.nursetraverse.logic.parser.CliSyntax.PREFIX_MED_CON;
import static cs.f10.t1.nursetraverse.logic.parser.CliSyntax.PREFIX_NAME;
import static cs.f10.t1.nursetraverse.logic.parser.CliSyntax.PREFIX_PATIENT_VISIT_TODO;
import static cs.f10.t1.nursetraverse.logic.parser.CliSyntax.PREFIX_PHONE;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import cs.f10.t1.nursetraverse.commons.core.index.Index;
import cs.f10.t1.nursetraverse.logic.commands.EditCommand;
import cs.f10.t1.nursetraverse.logic.parser.exceptions.ParseException;
import cs.f10.t1.nursetraverse.model.medicalcondition.MedicalCondition;
import cs.f10.t1.nursetraverse.model.visittodo.VisitTodo;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_MED_CON, PREFIX_PATIENT_VISIT_TODO);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditCommand.MESSAGE_USAGE), pe);
        }

        EditCommand.EditPatientDescriptor editPatientDescriptor = new EditCommand.EditPatientDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPatientDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPatientDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPatientDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPatientDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        parseMedicalConditionsForEdit(argMultimap.getAllValues(PREFIX_MED_CON))
                .ifPresent(editPatientDescriptor::setMedicalConditions);
        parseVisitTodosForEdit(argMultimap.getAllValues(PREFIX_PATIENT_VISIT_TODO))
                .ifPresent(editPatientDescriptor::setVisitTodos);

        if (!editPatientDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPatientDescriptor);
    }

    /**
     * Parses {@code Collection<String> medicalConditions} into a {@code Set<MedicalCondition>},
     * if {@code medicalConditions} is non-empty.
     * If {@code medicalConditions} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<MedicalCondition>} containing zero medicalConditions.
     */
    private Optional<Set<MedicalCondition>> parseMedicalConditionsForEdit(Collection<String> medicalConditions)
            throws ParseException {
        assert medicalConditions != null;

        if (medicalConditions.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> medicalConditionSet =
                medicalConditions.size() == 1 && medicalConditions.contains("")
                        ? Collections.emptySet() : medicalConditions;
        return Optional.of(ParserUtil.parseMedicalConditions(medicalConditionSet));
    }

    /**
     * Parses {@code Collection<String> VisitTodos} into a {@code Collection<VisitTodos>}
     * if {@code VisitTodos} is non-empty.
     * If {@code VisitTodos} contain only one element which is an empty string, it will be parsed into a
     * {@code Collection<VisitTodo>} containing zero VisitTodos.
     */
    private Optional<Collection<VisitTodo>> parseVisitTodosForEdit(Collection<String> visitTodos)
            throws ParseException {
        assert visitTodos != null;

        if (visitTodos.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> visitTodosSet = visitTodos.size() == 1
                && visitTodos.contains("")
                ? Collections.emptySet() : visitTodos;
        return Optional.of(ParserUtil.parseVisitTodos(visitTodosSet));
    }
}
