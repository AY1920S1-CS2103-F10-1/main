package cs.f10.t1.nursetraverse.model.medicalcondition;

import static java.util.Objects.requireNonNull;

import cs.f10.t1.nursetraverse.commons.util.AppUtil;

/**
 * Represents a MedicalCondition in the patient book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidConditionName(String)}
 */
public class MedicalCondition {

    public static final String MESSAGE_CONSTRAINTS = "Condition names should be alphanumeric with spaces.";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String conditionName;

    /**
     * Constructs a {@code MedicalCondition}.
     *
     * @param conditionName A valid condition name.
     */
    public MedicalCondition(String conditionName) {
        requireNonNull(conditionName);
        AppUtil.checkArgument(isValidConditionName(conditionName), MESSAGE_CONSTRAINTS);
        this.conditionName = conditionName;
    }

    /**
     * Returns true if a given string is a valid condition name.
     */
    public static boolean isValidConditionName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MedicalCondition // instanceof handles nulls
                && conditionName.equals(((MedicalCondition) other).conditionName)); // state check
    }

    @Override
    public int hashCode() {
        return conditionName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + conditionName + ']';
    }

}
