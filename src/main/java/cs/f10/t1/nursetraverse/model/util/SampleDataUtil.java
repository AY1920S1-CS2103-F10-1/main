package cs.f10.t1.nursetraverse.model.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cs.f10.t1.nursetraverse.commons.core.index.Index;
import cs.f10.t1.nursetraverse.model.AppointmentBook;
import cs.f10.t1.nursetraverse.model.PatientBook;
import cs.f10.t1.nursetraverse.model.ReadOnlyAppointmentBook;
import cs.f10.t1.nursetraverse.model.ReadOnlyPatientBook;
import cs.f10.t1.nursetraverse.model.appointment.Appointment;
import cs.f10.t1.nursetraverse.model.datetime.EndDateTime;
import cs.f10.t1.nursetraverse.model.datetime.RecurringDateTime;
import cs.f10.t1.nursetraverse.model.datetime.StartDateTime;
import cs.f10.t1.nursetraverse.model.patient.Address;
import cs.f10.t1.nursetraverse.model.patient.Email;
import cs.f10.t1.nursetraverse.model.patient.Name;
import cs.f10.t1.nursetraverse.model.patient.Patient;
import cs.f10.t1.nursetraverse.model.patient.Phone;
import cs.f10.t1.nursetraverse.model.tag.Tag;
import cs.f10.t1.nursetraverse.model.visit.Remark;
import cs.f10.t1.nursetraverse.model.visit.Visit;
import cs.f10.t1.nursetraverse.model.visittask.Detail;
import cs.f10.t1.nursetraverse.model.visittask.VisitTask;
import cs.f10.t1.nursetraverse.model.visittodo.VisitTodo;

/**
 * Contains utility methods for populating {@code PatientBook} with sample data.
 */
public class SampleDataUtil {
    public static Patient[] getSamplePatients() {
        Patient[] resultPatientArr = new Patient[] {
            new Patient(new Name("Alex Yeoh"),
                    new Phone("87438807"),
                    new Email("alexyeoh@example.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"),
                    getTagSet("friends"), getVisitTodos(),
                    collateVisits()),
            new Patient(new Name("Bernice Yu"),
                    new Phone("99272758"),
                    new Email("berniceyu@example.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    getTagSet("colleagues", "friends"),
                    getVisitTodos("Blood Pressure"),
                    collateVisits()),
            new Patient(new Name("Charlotte Oliveiro"),
                    new Phone("93210283"),
                    new Email("charlotte@example.com"),
                    new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    getTagSet("neighbours"),
                    getVisitTodos("Body Temperature"),
                    collateVisits()),
            new Patient(new Name("David Li"),
                    new Phone("91031282"),
                    new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    getTagSet("family"),
                    getVisitTodos("Pain Level", "Check if patient has been exercising"),
                    collateVisits()),
            new Patient(new Name("Irfan Ibrahim"),
                    new Phone("92492021"),
                    new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"),
                    getTagSet("classmates"),
                    getVisitTodos("Check cognitive abilities"),
                    collateVisits()),
            new Patient(new Name("Roy Balakrishnan"),
                    new Phone("92624417"),
                    new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"),
                    getTagSet("colleagues"),
                    getVisitTodos("Check wounds"),
                    collateVisits())
        };

        // Add Visits, as they need a reference to the patient they are attached to
        resultPatientArr[0].addVisit(makeVisit("Alex is in good health.",
                "10-11-2019 1500",
                "10-11-2019 1700",
                collateVisitTasks(
                        makeVisitTask("Apply Eyedrops", "", true),
                        makeVisitTask("Top-up medicine", "", true),
                        makeVisitTask("Check his diet",
                                "Stopped eating donuts", true),
                        makeVisitTask("Check his sleep cycle",
                                "Could not sleep on Monday and Thursday", true)
                ),
                resultPatientArr[0]
        ));
        resultPatientArr[1].addVisit(makeVisit("Charlotte was very quiet.",
                "12-11-2018 1500",
                "12-11-2018 1700",
                collateVisitTasks(
                        makeVisitTask("Check bed for bugs", "", true),
                        makeVisitTask("Top-up medicine", "", true)
                ),
                resultPatientArr[1]
        ));
        resultPatientArr[2].addVisit(makeVisit("",
                "12-12-2018 1500",
                "12-12-2018 1700",
                collateVisitTasks(
                        makeVisitTask("Blood pressure", "140/90mmHg", true),
                        makeVisitTask("Check bed for bugs", "", true),
                        makeVisitTask("Top-up medicine", "", false),
                        makeVisitTask("Ask spouse about David's condition",
                                "", false)
                ),
                resultPatientArr[2]
        ));

        return resultPatientArr;
    }

    public static Appointment[] getSampleAppointments() {
        Long zero = Long.parseLong("0");
        Appointment[] resultAppointmentArr = new Appointment[] {
                new Appointment(new StartDateTime("01-12-2019 1000"),
                        new EndDateTime("01-12-2019 1200"),
                        new RecurringDateTime(new Long[]{zero, zero, zero, zero, zero, zero}),
                        Index.fromOneBased(1), "Dental checkup")
        };

        return resultAppointmentArr;
    }

    public static ReadOnlyPatientBook getSamplePatientBook() {
        PatientBook sampleAb = new PatientBook();
        for (Patient samplePatient : getSamplePatients()) {
            sampleAb.addPatient(samplePatient);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a visitTodo collection containing the list of strings given.
     */
    public static Collection<VisitTodo> getVisitTodos(String... strings) {
        return Arrays.stream(strings)
                .map(VisitTodo::new)
                .collect(Collectors.toList());
    }

    /**
     * Returns a visit task list containing the list of visit tasks given.
     */
    public static List<VisitTask> collateVisitTasks(VisitTask... visitTasks) {
        return Arrays.stream(visitTasks).collect(Collectors.toList());
    }

    /**
     * Helper function to return a visit task.
     */
    public static VisitTask makeVisitTask(String description, String detail, boolean isDone) {
        return new VisitTask(new VisitTodo(description), new Detail(detail), isDone);
    }

    /**
     * Helper function to return a visit.
     */
    public static Visit makeVisit(String remark, String start, String end, List<VisitTask> visitTasks,
                                  Patient patient) {
        if (end == null) {
            return new Visit(new Remark(remark), new StartDateTime(start),
                    EndDateTime.UNFINISHED_VISIT_END_DATE_TIME, visitTasks, patient);
        }
        return new Visit(new Remark(remark), new StartDateTime(start),
                new EndDateTime(end), visitTasks, patient);
    }

    /**
     * Returns a visit list containing the list of visits given.
     */
    public static List<Visit> collateVisits(Visit... visits) {
        return Arrays.stream(visits).collect(Collectors.toList());
    }

    public static ReadOnlyAppointmentBook getSampleAppointmentBook() {
        AppointmentBook sampleAb = new AppointmentBook();
        Patient[] samplePatients = getSamplePatients();
        for (Appointment sampleAppointment : getSampleAppointments()) {
            Patient samplePatient = samplePatients[sampleAppointment.getPatientIndex().getZeroBased()];
            sampleAppointment.setPatient(samplePatient);
            sampleAb.addAppointment(sampleAppointment);
        }
        return sampleAb;
    }
}
