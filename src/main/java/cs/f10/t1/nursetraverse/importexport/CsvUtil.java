package cs.f10.t1.nursetraverse.importexport;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import cs.f10.t1.nursetraverse.commons.core.LogsCenter;
import cs.f10.t1.nursetraverse.commons.exceptions.IllegalValueException;
import cs.f10.t1.nursetraverse.commons.util.FileUtil;
import cs.f10.t1.nursetraverse.importexport.exceptions.ExportingException;
import cs.f10.t1.nursetraverse.importexport.exceptions.ImportingException
import cs.f10.t1.nursetraverse.model.patient.Patient;
import cs.f10.t1.nursetraverse.storage.JsonAdaptedPatient;

/**
 * Reads and writes Java based Patient objects to and fro .csv files
 */
public class CsvUtil {
    private static final Logger logger = LogsCenter.getLogger(CsvUtil.class);

    private static final String MESSAGE_OVERRIDING_FORBIDDEN = "File with given filename already exists,"
            + "overriding is not allowed";
    private static final String MESSAGE_MISSING_FILE = "File not found.";

    // Folder paths
    private static final String exportFolder = "exports";
    private static final String importFolder = "imports";

    //=========== Writing/Export functions =============================================================

    /**
     * Writes a list of Patients into a .csv file
     * @param patients
     * @return string of the path that was written to
     * @throws IOException
     */
    public static String writePatientsToCsv(List<Patient> patients, String filename)
            throws IOException, ExportingException {
        String pathString = exportFolder + "/" + filename + ".csv";
        Path writePath = Paths.get(pathString);
        if (FileUtil.isFileExists(writePath)) {
            throw new ExportingException(MESSAGE_OVERRIDING_FORBIDDEN);
        }
        assert !patients.isEmpty();
        String toWrite = getCsvStringFromPatients(patients);
        FileUtil.createFile(writePath);
        logger.info("Writing export data to: " + writePath);
        FileUtil.writeToFile(writePath, toWrite);
        return pathString;
    }

    /**
     * Converts a List of patient objects into a String to be written into a .csv file
     * @param patients
     * @return csv friendly string
     * @throws JsonProcessingException
     */
    private static String getCsvStringFromPatients(List<Patient> patients) throws JsonProcessingException {
        CsvMapper mapper = new CsvMapper();
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .addMixIn(JsonAdaptedPatient.class, PatientMixIn.class);
        CsvSchema schema = mapper.schemaFor(JsonAdaptedPatient.class).withHeader().withArrayElementSeparator("\n");
        ObjectWriter writer = mapper.writer(schema);
        return writer.writeValueAsString(convertToCsvAdaptedList(patients));
    }

    /**
     * Converts a list of Patient objects to a list of Jackson .csv friendly objects
     * @param patients
     * @return csv adapted patients
     */
    private static List<JsonAdaptedPatient> convertToCsvAdaptedList(List<Patient> patients) {
        List<JsonAdaptedPatient> csvAdaptedList = new ArrayList<>();
        for (Patient patient : patients) {
            csvAdaptedList.add(convertToCsvAdaptedPatient(patient));
        }
        return csvAdaptedList;
    }

    /**
     * Converts a Patient object to a Jackson .csv friendly object
     * @param patient
     * @return JsonAdaptedPatient
     */
    private static JsonAdaptedPatient convertToCsvAdaptedPatient(Patient patient) {
        return new JsonAdaptedPatient(patient);
    }

    //=========== Reading/Import functions ===============================================================

    /**
     * Reads data from a .csv file and converts it to a list of {@Code Patient} objects
     * @return a list of patients
     * @throws IOException
     * @throws IllegalValueException if illegal values exist in the .csv file
     */

    public static List<Patient> readPatientsFromCsv(String fileName)
            throws IOException, IllegalValueException, ImportingException {
        String pathString = importFolder + "/" + fileName + ".csv";
        Path readPath = Paths.get(pathString);
        if (!FileUtil.isFileExists(readPath)) {
            throw new ImportingException(MESSAGE_MISSING_FILE);
        }

        CsvMapper mapper = new CsvMapper();
        mapper.addMixIn(JsonAdaptedPatient.class, PatientMixIn.class)
                // when an empty field is encountered, create a null object
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        CsvSchema schema = mapper.schemaFor(JsonAdaptedPatient.class)
                    .withHeader()
                    // list elements are separated by a new line
                    .withArrayElementSeparator("\n");
        MappingIterator<JsonAdaptedPatient> iter = mapper.readerFor(JsonAdaptedPatient.class)
                    .with(schema)
                    .readValues(readPath.toFile());
        List<JsonAdaptedPatient> importedCsvPatients = iter.readAll();

        return convertImportedPatientsToPatientList(importedCsvPatients);
    }

    /**
     * Converts a {@Code JsonAdaptedPatient} read from a csv file into a {@Code Patient} object
     */
    private static Patient convertToPatient(JsonAdaptedPatient patient) throws IllegalValueException {
        return patient.toModelType();
    }

    /**
     * Converts a list of {@Code JsonAdaptedPatient} objects read from a csv file
     * into a list of {@Code Patient} objects
     */
    private static List<Patient> convertImportedPatientsToPatientList(List<JsonAdaptedPatient> patients)
            throws IllegalValueException {
        List<Patient> newPatientList = new ArrayList<>();
        for (JsonAdaptedPatient patient : patients) {
            newPatientList.add(convertToPatient(patient));
        }
        return newPatientList;
    }

}
