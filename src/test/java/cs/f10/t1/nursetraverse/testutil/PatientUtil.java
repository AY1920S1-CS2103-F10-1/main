package cs.f10.t1.nursetraverse.testutil;

import static cs.f10.t1.nursetraverse.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static cs.f10.t1.nursetraverse.logic.parser.CliSyntax.PREFIX_EMAIL;
import static cs.f10.t1.nursetraverse.logic.parser.CliSyntax.PREFIX_MED_CON;
import static cs.f10.t1.nursetraverse.logic.parser.CliSyntax.PREFIX_NAME;
import static cs.f10.t1.nursetraverse.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Set;

import cs.f10.t1.nursetraverse.logic.commands.AddCommand;
import cs.f10.t1.nursetraverse.logic.commands.EditCommand;
import cs.f10.t1.nursetraverse.model.medicalcondition.MedicalCondition;
import cs.f10.t1.nursetraverse.model.patient.Patient;

/**
 * A utility class for Patient.
 */
public class PatientUtil {

    /**
     * Returns an add command string for adding the {@code patient}.
     */
    public static String getAddCommand(Patient patient) {
        return AddCommand.COMMAND_WORD + " " + getPatientDetails(patient);
    }

    /**
     * Returns the part of command string for the given {@code patient}'s details.
     */
    public static String getPatientDetails(Patient patient) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + patient.getName().fullName + " ");
        sb.append(PREFIX_PHONE + patient.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + patient.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + patient.getAddress().value + " ");
        patient.getMedicalConditions().stream().forEach(
            s -> sb.append(PREFIX_MED_CON + s.conditionName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPatientDescriptor}'s details.
     */
    public static String getEditPatientDescriptorDetails(EditCommand.EditPatientDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value)
                .append(" "));
        if (descriptor.getMedicalConditions().isPresent()) {
            Set<MedicalCondition> medicalConditions = descriptor.getMedicalConditions().get();
            if (medicalConditions.isEmpty()) {
                sb.append(PREFIX_MED_CON);
            } else {
                medicalConditions.forEach(s -> sb.append(PREFIX_MED_CON).append(s.conditionName).append(" "));
            }
        }
        return sb.toString();
    }
}
