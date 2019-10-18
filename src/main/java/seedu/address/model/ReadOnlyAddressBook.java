package seedu.address.model;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import seedu.address.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns a {@code Pair<Integer, Integer>} containing the
     * PatientIndex and AppointmentIndex respectively to indicate the ongoing visit.
     * If there is no ongoing visit, returns {@code new Pair<>(-1,-1)}.
     */
    Pair<Integer, Integer> getIndexPairOfOngoingPatientAndVisit();

    /**
     * Returns a deep copy of this {@code readOnlyAddressBook}. Changes made to the copy will not affect this object
     * and vice versa.
     */
    ReadOnlyAddressBook deepCopy();
}
