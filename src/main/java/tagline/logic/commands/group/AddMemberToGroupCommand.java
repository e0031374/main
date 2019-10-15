package tagline.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static tagline.logic.parser.group.GroupCliSyntax.GROUP_COMMAND_WORD;
import static tagline.logic.parser.group.GroupCliSyntax.PREFIX_CONTACTID;
import static tagline.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tagline.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tagline.logic.parser.CliSyntax.PREFIX_NAME;
import static tagline.logic.parser.CliSyntax.PREFIX_PHONE;
import static tagline.logic.parser.CliSyntax.PREFIX_TAG;
import static tagline.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import tagline.commons.core.Messages;
import tagline.commons.core.index.Index;
import tagline.commons.util.CollectionUtil;
import tagline.logic.commands.Command;
import tagline.logic.commands.CommandResult;
import tagline.logic.commands.exceptions.CommandException;
import tagline.model.Model;
import tagline.model.group.GroupDescription;
import tagline.model.group.GroupName;
import tagline.model.person.Address;
import tagline.model.person.Email;
import tagline.model.person.Name;
import tagline.model.group.Group;
import tagline.model.person.Phone;
import tagline.model.tag.Tag;

/**
 * INCOMPLETE TODO
 * Edits the details of an existing group in the address book.
 */
public class AddMemberToGroupCommand extends Command {

    // GroupCLi synta
    public static final String COMMAND_WORD = GROUP_COMMAND_WORD + " " + "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds one or more members to a group"
            //": Edits the details of the group identified "
            + "by the group name and contact ID of member to be added.\n"
            //+ "Existing values will be overwritten by the input values.\n"
            //+ "Parameters: INDEX (must be a positive integer) "
            + "Parameters: GROUPNAME (must be a non empty string) "
            + "[" + PREFIX_CONTACTID + "CONTACT ID]...\n "
            //+ "[" + PREFIX_NAME + "NAME] "
            //+ "[" + PREFIX_PHONE + "PHONE] "
            //+ "[" + PREFIX_EMAIL + "EMAIL] "
            //+ "[" + PREFIX_ADDRESS + "ADDRESS] "
            //+ "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " Wakandan-Royal-Guard "
            + PREFIX_CONTACTID + "62353";

    public static final String MESSAGE_ADD_MEMBER_SUCCESS = "Edited Group: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_MEMBER = "This group member already exists in the group.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This group already exists in the address book.";

    private final Index index;
    private final EditGroupDescriptor editGroupDescriptor;

    /**
     * @param index of the group in the filtered group list to edit
     * @param editGroupDescriptor details to edit the group with
     */
    public AddMemberToGroupCommand(Index index, EditGroupDescriptor editGroupDescriptor) {
        requireNonNull(index);
        requireNonNull(editGroupDescriptor);

        this.index = index;
        this.editGroupDescriptor = new EditGroupDescriptor(editGroupDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Group> lastShownList = model.getFilteredGroupList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Group groupToEdit = lastShownList.get(index.getZeroBased());
        Group editedGroup = createEditedGroup(groupToEdit, editGroupDescriptor);

        if (!groupToEdit.isSameGroup(editedGroup) && model.hasGroup(editedGroup)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setGroup(groupToEdit, editedGroup);
        model.updateFilteredGroupList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_ADD_MEMBER_SUCCESS, editedGroup));
    }

    /**
     * Creates and returns a {@code Group} with the details of {@code groupToEdit}
     * edited with {@code editGroupDescriptor}.
     */
    private static Group createEditedGroup(Group groupToEdit, EditGroupDescriptor editGroupDescriptor) {
        assert groupToEdit != null;

        GroupName updatedGroupName = editGroupDescriptor.getGroupName().orElse(groupToEdit.getGroupName());
        GroupDescription updatedGroupDescription = editGroupDescriptor.getGroupDescription().orElse(groupToEdit.getGroupName());
        Name updatedName = editGroupDescriptor.getName().orElse(groupToEdit.getName());
        Phone updatedPhone = editGroupDescriptor.getPhone().orElse(groupToEdit.getPhone());
        Email updatedEmail = editGroupDescriptor.getEmail().orElse(groupToEdit.getEmail());
        Address updatedAddress = editGroupDescriptor.getAddress().orElse(groupToEdit.getAddress());
        Set<Tag> updatedTags = editGroupDescriptor.getTags().orElse(groupToEdit.getTags());

        return new Group(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddMemberToGroupCommand)) {
            return false;
        }

        // state check
        AddMemberToGroupCommand e = (AddMemberToGroupCommand) other;
        return index.equals(e.index)
                && editGroupDescriptor.equals(e.editGroupDescriptor);
    }

    /**
     * Stores the details to edit the group with. Each non-empty field value will replace the
     * corresponding field value of the group.
     */
    public static class EditGroupDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditGroupDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditGroupDescriptor(EditGroupDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditGroupDescriptor)) {
                return false;
            }

            // state check
            EditGroupDescriptor e = (EditGroupDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags());
        }
    }
}
