package cs.f10.t1.nursetraverse.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cs.f10.t1.nursetraverse.logic.parser.exceptions.ParseException;
import cs.f10.t1.nursetraverse.model.medicalcondition.MedicalCondition;
import cs.f10.t1.nursetraverse.model.patient.Address;
import cs.f10.t1.nursetraverse.model.patient.Email;
import cs.f10.t1.nursetraverse.model.patient.Name;
import cs.f10.t1.nursetraverse.model.patient.Phone;
import cs.f10.t1.nursetraverse.testutil.Assert;
import cs.f10.t1.nursetraverse.testutil.TypicalIndexes;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_MED_CON = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_MED_CON_1 = "friend";
    private static final String VALID_MED_CON_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        Assert.assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        Assertions.assertEquals(TypicalIndexes.INDEX_FIRST_PATIENT, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        Assertions.assertEquals(TypicalIndexes.INDEX_FIRST_PATIENT, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        Assertions.assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        Assertions.assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        Assertions.assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        Assertions.assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        Assertions.assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        Assertions.assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        Assertions.assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        Assertions.assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseMedicalCondition_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseMedicalCondition(null));
    }

    @Test
    public void parseMedicalCondition_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseMedicalCondition(INVALID_MED_CON));
    }

    @Test
    public void parseMedicalCondition_validValueWithoutWhitespace_returnsMedicalCondition() throws Exception {
        MedicalCondition expectedMedicalCondition = new MedicalCondition(VALID_MED_CON_1);
        Assertions.assertEquals(expectedMedicalCondition, ParserUtil.parseMedicalCondition(VALID_MED_CON_1));
    }

    @Test
    public void parseMedicalCondition_validValueWithWhitespace_returnsTrimmedMedicalCondition() throws Exception {
        String medicalConditionWithWhitespace = WHITESPACE + VALID_MED_CON_1 + WHITESPACE;
        MedicalCondition expectedMedicalCondition = new MedicalCondition(VALID_MED_CON_1);
        Assertions.assertEquals(expectedMedicalCondition,
                ParserUtil.parseMedicalCondition(medicalConditionWithWhitespace));
    }

    @Test
    public void parseMedicalConditions_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseMedicalConditions(null));
    }

    @Test
    public void parseMedicalConditions_collectionWithInvalidMedicalConditions_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil
                .parseMedicalConditions(Arrays.asList(VALID_MED_CON_1, INVALID_MED_CON)));
    }

    @Test
    public void parseMedicalConditions_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseMedicalConditions(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseMedicalConditions_collectionWithValidMedicalConditions_returnsMedicalConditionSet()
            throws Exception {
        Set<MedicalCondition> actualMedicalConditionSet = ParserUtil
                .parseMedicalConditions(Arrays.asList(VALID_MED_CON_1, VALID_MED_CON_2));
        Set<MedicalCondition> expectedMedicalConditionSet = new HashSet<MedicalCondition>(
                Arrays.asList(new MedicalCondition(VALID_MED_CON_1), new MedicalCondition(VALID_MED_CON_2)));

        assertEquals(expectedMedicalConditionSet, actualMedicalConditionSet);
    }
}
