package cs.f10.t1.nursetraverse.model.medicalcondition;

import org.junit.jupiter.api.Test;

import cs.f10.t1.nursetraverse.testutil.Assert;

public class MedicalConditionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new MedicalCondition(null));
    }

    @Test
    public void constructor_invalidConditionName_throwsIllegalArgumentException() {
        String invalidConditionName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new MedicalCondition(invalidConditionName));
    }

    @Test
    public void isValidConditionName() {
        // null tag name
        Assert.assertThrows(NullPointerException.class, () -> MedicalCondition.isValidConditionName(null));
    }

}
