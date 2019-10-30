package cs.f10.t1.nursetraverse.testutil;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cs.f10.t1.nursetraverse.logic.commands.EditCommand;
import cs.f10.t1.nursetraverse.model.medicalcondition.MedicalCondition;
import cs.f10.t1.nursetraverse.model.patient.Address;
import cs.f10.t1.nursetraverse.model.patient.Email;
import cs.f10.t1.nursetraverse.model.patient.Name;
import cs.f10.t1.nursetraverse.model.patient.Patient;
import cs.f10.t1.nursetraverse.model.patient.Phone;
import cs.f10.t1.nursetraverse.model.visittodo.VisitTodo;

/**
 * A utility class to help with building EditPatientDescriptor objects.
 */
public class EditPatientDescriptorBuilder {

    private EditCommand.EditPatientDescriptor descriptor;

    public EditPatientDescriptorBuilder() {
        descriptor = new EditCommand.EditPatientDescriptor();
    }

    public EditPatientDescriptorBuilder(EditCommand.EditPatientDescriptor descriptor) {
        this.descriptor = new EditCommand.EditPatientDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPatientDescriptor} with fields containing {@code patient}'s details
     */
    public EditPatientDescriptorBuilder(Patient patient) {
        descriptor = new EditCommand.EditPatientDescriptor();
        descriptor.setName(patient.getName());
        descriptor.setPhone(patient.getPhone());
        descriptor.setEmail(patient.getEmail());
        descriptor.setAddress(patient.getAddress());
        descriptor.setMedicalConditions(patient.getMedicalConditions());
    }

    /**
     * Sets the {@code Name} of the {@code EditPatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code medicalConditions} into a {@code Set<MedicalCondition>}
     * and set it to the {@code EditPatientDescriptor}
     * that we are building.
     */
    public EditPatientDescriptorBuilder withMedicalConditions(String... medicalConditions) {
        Set<MedicalCondition> medicalConditionSet =
                Stream.of(medicalConditions).map(MedicalCondition::new).collect(Collectors.toSet());
        descriptor.setMedicalConditions(medicalConditionSet);
        return this;
    }

    /**
     * Parses the {@code visitTodos} into a {@code Collection<VisitTodo>}
     * and set it to the {@code EditPatientDescriptor}
     * that we are building.
     */
    public EditPatientDescriptorBuilder withVisitTodos(String... visitTodos) {
        Collection<VisitTodo> collection = Stream.of(visitTodos)
                .map(VisitTodo::new).collect(Collectors.toSet());
        descriptor.setVisitTodos(collection);
        return this;
    }

    public EditCommand.EditPatientDescriptor build() {
        return descriptor;
    }
}
