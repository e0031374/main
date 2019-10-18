package tagline.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static tagline.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tagline.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tagline.logic.parser.CliSyntax.PREFIX_NAME;
import static tagline.logic.parser.CliSyntax.PREFIX_PHONE;
import static tagline.logic.parser.CliSyntax.PREFIX_TAG;
import static tagline.model.group.GroupModel.PREDICATE_SHOW_ALL_GROUPS;

import java.util.*;

import tagline.commons.util.CollectionUtil;
import tagline.logic.commands.Command;
import tagline.logic.commands.CommandResult;
import tagline.logic.commands.exceptions.CommandException;
import tagline.logic.parser.group.GroupCliSyntax;
import tagline.model.Model;
import tagline.model.group.Group;
import tagline.model.group.GroupName;
import tagline.model.group.GroupNameEqualsKeywordPredicate;
import tagline.model.group.ContactId;

/**
 * Edits the details of an existing group in the address book.
 */
public class AddMemberToGroupCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Add a member to the group identified "
            + "by the index number used in the displayed group list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Group: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This group already exists in the address book.";

    //private final Group group;
    private final String groupName;
    private final EditGroupDescriptor editGroupDescriptor;

    /**
     * @param groupName of the group in the filtered group list to edit
     * @param editGroupDescriptor details to edit the group with
     */
    public AddMemberToGroupCommand(String groupName, EditGroupDescriptor editGroupDescriptor) {
        requireNonNull(groupName);
        requireNonNull(editGroupDescriptor);

        //this.index = index;
        this.groupName = groupName.trim();
        this.editGroupDescriptor = new EditGroupDescriptor(editGroupDescriptor);

    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<String> keywords = new ArrayList<>();
        keywords.add(groupName);
        model.updateFilteredGroupList(new GroupNameEqualsKeywordPredicate(keywords));
        List<Group> filteredGroupList = model.getFilteredGroupList();
        Optional<Group> optionalGroup = filteredGroupList.stream().findFirst();

        if (optionalGroup.isEmpty()) {
            throw new CommandException(GroupCliSyntax.MESSAGE_INVALID_GROUP_NAME);
        }

        Group groupToEdit = optionalGroup.get();
        Group editedGroup = createEditedGroup(groupToEdit, editGroupDescriptor);


        model.setGroup(groupToEdit, editedGroup);
        model.updateFilteredGroupList(PREDICATE_SHOW_ALL_GROUPS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedGroup));
    }

    /**
     * Creates and returns a {@code Group} with the details of {@code groupToEdit}
     * edited with {@code editGroupDescriptor}.
     */
    private static Group createEditedGroup(Group groupToEdit, EditGroupDescriptor editGroupDescriptor) {
        assert groupToEdit != null;

        GroupName updatedGroupName = editGroupDescriptor.getGroupName().orElse(groupToEdit.getGroupName());
        Set<ContactId> updatedMemberIds = new HashSet<>();
        if (editGroupDescriptor.getMemberIds().isPresent()) {
            updatedMemberIds.addAll(editGroupDescriptor.getMemberIds().get());
        }
        updatedMemberIds.addAll(groupToEdit.getMemberIds());
        //Set<MemberId> updatedMemberIds = editGroupDescriptor.getMemberIds().orElse(groupToEdit.getMemberIds());
        //Set<Tag> updatedTags = editGroupDescriptor.getTags().orElse(groupToEdit.getTags());

        return new Group(updatedGroupName, updatedMemberIds);
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
        return groupName.equals(e.groupName)
                && editGroupDescriptor.equals(e.editGroupDescriptor);
    }

    /**
     * Stores the details to edit the group with. Each non-empty field value will replace the
     * corresponding field value of the group.
     */
    public static class EditGroupDescriptor {
        private GroupName groupName;
        private Set<ContactId> memberIds;
        //private Set<Tag> tags;

        public EditGroupDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditGroupDescriptor(EditGroupDescriptor toCopy) {
            setGroupName(toCopy.groupName);
            setMemberIds(toCopy.memberIds);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(groupName, memberIds);
        }

        public void setGroupName(GroupName groupName) {
            this.groupName = groupName;
        }

        public Optional<GroupName> getGroupName() {
            return Optional.ofNullable(groupName);
        }

        //public void setPhone(Phone phone) {
        //    this.phone = phone;
        //}

        //public Optional<Phone> getPhone() {
        //    return Optional.ofNullable(phone);
        //}

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setMemberIds(Set<ContactId> memberIds) {
            this.memberIds = (memberIds != null) ? new HashSet<>(memberIds) : null;
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void addMemberIds(Set<ContactId> memberIds) {
            this.memberIds = (memberIds != null) ? new HashSet<>(memberIds) : null;
        }
        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<ContactId>> getMemberIds() {
            return (memberIds != null) ? Optional.of(Collections.unmodifiableSet(memberIds)) : Optional.empty();
        }
        ///**
        // * Sets {@code tags} to this object's {@code tags}.
        // * A defensive copy of {@code tags} is used internally.
        // */
        //public void setTags(Set<Tag> tags) {
        //    this.tags = (tags != null) ? new HashSet<>(tags) : null;
        //}

        ///**
        // * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
        // * if modification is attempted.
        // * Returns {@code Optional#empty()} if {@code tags} is null.
        // */
        //public Optional<Set<Tag>> getTags() {
        //    return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        //}

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

            return getGroupName().equals(e.getGroupName())
                    && getMemberIds().equals(e.getMemberIds());
                    //&& getTags().equals(e.getTags());
        }
    }
}
