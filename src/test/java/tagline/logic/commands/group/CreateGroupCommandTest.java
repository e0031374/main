//@@author e0031374
package tagline.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tagline.testutil.Assert.assertThrows;
import static tagline.testutil.TypicalContacts.ALICE;
import static tagline.testutil.TypicalContacts.BENSON;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import tagline.commons.core.GuiSettings;
import tagline.logic.commands.CommandResult;
import tagline.logic.commands.exceptions.CommandException;
import tagline.model.Model;
import tagline.model.ReadOnlyUserPrefs;
import tagline.model.contact.Contact;
import tagline.model.contact.ContactId;
import tagline.model.contact.ReadOnlyAddressBook;
import tagline.model.group.Group;
import tagline.model.group.GroupBook;
import tagline.model.group.MemberId;
import tagline.model.group.ReadOnlyGroupBook;
import tagline.model.note.Note;
import tagline.model.note.NoteId;
import tagline.model.note.ReadOnlyNoteBook;
import tagline.testutil.AddressBookBuilder;
import tagline.testutil.GroupBuilder;

public class CreateGroupCommandTest {

    @Test
    public void constructor_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CreateGroupCommand(null));
    }

    @Test
    public void execute_groupAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingGroupAdded modelStub = new ModelStubAcceptingGroupAdded();
        Group testGroup = new GroupBuilder().withMemberIds("001", "002", "003").build();
        Group correctGroup = new GroupBuilder().withMemberIds("001", "002").build();

        MemberId[] three = {new MemberId("003")};
        String uiString = GroupCommand.notFoundString(Arrays.asList(three));

        CommandResult commandResult = new CreateGroupCommand(testGroup).execute(modelStub);

        assertEquals(String.format(CreateGroupCommand.MESSAGE_SUCCESS + uiString,
                correctGroup), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(correctGroup), modelStub.groupsAdded);
    }

    @Test
    public void execute_duplicateGroup_throwsCommandException() {
        Group validGroup = new GroupBuilder().build();
        CreateGroupCommand createGroupCommand = new CreateGroupCommand(validGroup);
        ModelStub modelStub = new ModelStubWithGroup(validGroup);

        assertThrows(CommandException.class,
                CreateGroupCommand.MESSAGE_DUPLICATE_GROUP, () -> createGroupCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Group alice = new GroupBuilder().withGroupName("Alice").build();
        Group bob = new GroupBuilder().withGroupName("Bob").build();
        CreateGroupCommand addAliceCommand = new CreateGroupCommand(alice);
        CreateGroupCommand addBobCommand = new CreateGroupCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        CreateGroupCommand addAliceCommandCopy = new CreateGroupCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different group -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getNoteBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setNoteBookFilePath(Path noteBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getGroupBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGroupBookFilePath(Path noteBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addContact(Contact contact) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setNoteBook(ReadOnlyNoteBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyNoteBook getNoteBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGroupBook(ReadOnlyGroupBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyGroupBook getGroupBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasContact(Contact contact) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteContact(Contact target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setContact(Contact target, Contact editedContact) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<Contact> findContact(ContactId id) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Contact> getFilteredContactList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredContactList(Predicate<Contact> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasNote(Note note) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addNote(Note note) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteNote(Note target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setNote(Note target, Note editedNote) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<Note> findNote(NoteId noteId) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Note> getFilteredNoteList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredNoteList(Predicate<Note> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasGroup(Group note) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addGroup(Group note) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteGroup(Group target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGroup(Group target, Group editedGroup) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Group> getFilteredGroupList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredGroupList(Predicate<Group> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single group.
     */
    private class ModelStubWithGroup extends ModelStub {
        private final Group contact;

        ModelStubWithGroup(Group contact) {
            requireNonNull(contact);
            this.contact = contact;
        }

        @Override
        public boolean hasGroup(Group contact) {
            requireNonNull(contact);
            return this.contact.isSameGroup(contact);
        }

    }

    /**
     * A Model stub that always accept the contact being added.
     */
    private class ModelStubAcceptingGroupAdded extends ModelStub {
        final ArrayList<Group> groupsAdded = new ArrayList<>();

        @Override
        public boolean hasGroup(Group contact) {
            requireNonNull(contact);
            return groupsAdded.stream().anyMatch(contact::isSameGroup);
        }

        @Override
        public void addGroup(Group contact) {
            requireNonNull(contact);
            groupsAdded.add(contact);
        }

        @Override
        public ReadOnlyGroupBook getGroupBook() {
            return new GroupBook();
        }

        @Override
        public void updateFilteredContactList(Predicate<Contact> predicate) {
        }

        @Override
        public ObservableList<Contact> getFilteredContactList() {
            return new AddressBookBuilder().withContact(ALICE).withContact(BENSON).build().getContactList();
        }

    }
}
