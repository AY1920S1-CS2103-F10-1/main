package cs.f10.t1.nursetraverse.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import cs.f10.t1.nursetraverse.commons.exceptions.IllegalValueException;
import cs.f10.t1.nursetraverse.model.medicalcondition.MedicalCondition;

/**
 * Jackson-friendly version of {@link MedicalCondition}.
 */
public class JsonAdaptedMedicalCondition {

    private final String conditionName;

    /**
     * Constructs a {@code JsonAdaptedMedicalCondition} with the given {@code conditionName}.
     */
    @JsonCreator
    public JsonAdaptedMedicalCondition(String conditionName) {
        this.conditionName = conditionName;
    }

    /**
     * Converts a given {@code MedicalCondition} into this class for Jackson use.
     */
    public JsonAdaptedMedicalCondition(MedicalCondition source) {
        conditionName = source.conditionName;
    }

    @JsonValue
    public String getConditionName() {
        return conditionName;
    }

    /**
     * Converts this Jackson-friendly adapted condition object into the model's {@code MedicalCondition} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted condition.
     */
    public MedicalCondition toModelType() throws IllegalValueException {
        if (!MedicalCondition.isValidConditionName(conditionName)) {
            throw new IllegalValueException(MedicalCondition.MESSAGE_CONSTRAINTS);
        }
        return new MedicalCondition(conditionName);
    }

}
