package cs.f10.t1.nursetraverse.model;

import static cs.f10.t1.nursetraverse.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import cs.f10.t1.nursetraverse.commons.core.index.Index;
import cs.f10.t1.nursetraverse.commons.exceptions.CopyError;
import cs.f10.t1.nursetraverse.commons.exceptions.IllegalValueException;
import cs.f10.t1.nursetraverse.model.appointment.Appointment;
import cs.f10.t1.nursetraverse.model.appointment.UniqueAppointmentList;
import cs.f10.t1.nursetraverse.model.datetime.DateTime;
import cs.f10.t1.nursetraverse.model.datetime.EndDateTime;
import cs.f10.t1.nursetraverse.model.datetime.RecurringDateTime;
import cs.f10.t1.nursetraverse.model.datetime.StartDateTime;
import cs.f10.t1.nursetraverse.model.patient.Patient;
import cs.f10.t1.nursetraverse.storage.JsonSerializableAppointmentBook;
import javafx.collections.ObservableList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePatient comparison)
 */
public class AppointmentBook implements ReadOnlyAppointmentBook {

    private final UniqueAppointmentList appointments;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        appointments = new UniqueAppointmentList();
    }

    public AppointmentBook() {}

    /**
     * Creates an PatientBook using the Patients in the {@code toBeCopied}
     */
    public AppointmentBook(ReadOnlyAppointmentBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the appointment list with {@code appointments}.
     * {@code appointments} must not contain duplicate appointments.
     */
    public void setAppointments(List<Appointment> appointments) {
        this.appointments.setAppointments(appointments);
    }

    /**
     * Resets the existing data of this {@code AppointmentBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAppointmentBook newData) {
        requireNonNull(newData);

        setAppointments(newData.getAppointmentList());
    }

    //// appointment-level operations

    /**
     * Returns true if an appointment with the same identity as {@code appointment} exists in the appointment book.
     */
    public boolean hasAppointment(Appointment appointment) {
        requireNonNull(appointment);
        return appointments.contains(appointment);
    }

    /**
     * Returns index of appointment.
     */
    public int indexOfAppointment(Appointment appointment) {
        requireNonNull(appointment);
        return appointments.indexOf(appointment);
    }

    /**
     * Adds an appointment to the appointment book.
     * The patient must not already exist in the appointment book.
     */
    public void addAppointment(Appointment a) {
        appointments.add(a);
    }

    /**
     * Replaces the given patient {@code target} in the list with {@code editedAppointment}.
     * {@code target} must exist in the appointment book.
     * The patient identity of {@code editedAppointment} must not be the same as another existing patient in the
     * appointment book.
     */
    public void setAppointment(Appointment target, Appointment editedAppointment) {
        requireNonNull(editedAppointment);

        appointments.setAppointment(target, editedAppointment);
    }

    /**
     * Replaces all appointments with {@code patientToEdit} in the list with {@code editedPatient}.
     */
    public void editAppointments(Patient patientToEdit, Patient editedPatient) {
        requireAllNonNull(patientToEdit, editedPatient);
        List<Appointment> newAppointments = new ArrayList<>();
        for (Appointment appt : appointments) {
            if (appt.getPatient().equals(patientToEdit)) {
                appt.setPatient(editedPatient);
            }
            newAppointments.add(appt);
        }
        setAppointments(newAppointments);
    }

    /**
     * Removes {@code key} from this {@code AppointmentBook}.
     * {@code key} must exist in the appointment book.
     */
    public void removeAppointment(Appointment key) {
        requireNonNull(key);
        appointments.remove(key);

        if (key.getFrequency().isRecurringFrequency()) {
            addRecurringAppointment(key);
        }
    }

    /**
     * Removes all appointments with this {@code patient} from this {@code AppointmentBook}.
     */
    public void removeAppointments(Patient patient, Index patientIndex) {
        requireNonNull(patient);
        List<Appointment> keepAppointments = new ArrayList<>();
        for (Appointment appt : appointments) {
            if (!appt.getPatient().equals(patient)) {
                int currPatientIndex = appt.getPatientIndex().getOneBased();
                int targetPatientIndex = patientIndex.getOneBased();

                if (currPatientIndex > targetPatientIndex) {
                    appt.setPatientIndex(Index.fromOneBased(currPatientIndex - 1));
                }
                keepAppointments.add(appt);
            }
        }
        setAppointments(keepAppointments);
    }

    public void addRecurringAppointment(Appointment key) {
        RecurringDateTime frequency = key.getFrequency();
        StartDateTime nextStartDateTime = new StartDateTime(frequency
                                                            .getNextAppointmentDateTime(key.getStartDateTime()));
        EndDateTime nextEndDateTime = new EndDateTime(frequency
                                                      .getNextAppointmentDateTime(key.getEndDateTime()));
        Index patientIndex = key.getPatientIndex();
        Patient patient = key.getPatient();
        String description = key.getDescription();

        Appointment nextAppointment = new Appointment(nextStartDateTime, nextEndDateTime, frequency, patientIndex,
                                                      description);
        nextAppointment.setPatient(patient);
        addAppointment(nextAppointment);
    }

    //// util methods
    @Override
    public AppointmentBook deepCopy() {
        try {
            return new JsonSerializableAppointmentBook(this).toModelType();
        } catch (IllegalValueException e) {
            throw new CopyError("Error copying AppointmentBook");
        }
    }

    @Override
    public String toString() {
        return appointments.asUnmodifiableObservableList().size() + " appointments";
        // TODO: refine later
    }

    @Override
    public ObservableList<Appointment> getAppointmentList() {
        return appointments.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AppointmentBook // instanceof handles nulls
                && appointments.equals(((AppointmentBook) other).appointments));
    }

    @Override
    public int hashCode() {
        return appointments.hashCode();
    }

}

