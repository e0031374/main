package tagline.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static tagline.logic.parser.group.GroupCliSyntax.PREFIX_CONTACTID;
import static tagline.model.group.GroupModel.PREDICATE_SHOW_ALL_GROUPS;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import tagline.commons.util.CollectionUtil;

import tagline.logic.commands.CommandResult;
import tagline.logic.commands.CommandResult.ViewType;
import tagline.logic.commands.exceptions.CommandException;
import tagline.model.Model;
import tagline.model.group.Group;
import tagline.model.group.GroupDescription;
import tagline.model.group.GroupName;
import tagline.model.group.GroupNameEqualsKeywordPredicate;
import tagline.model.group.MemberId;

/**
 * Edits the details of an existing group in the address book.
 */
public class RemoveMemberFromGroupCommand extends GroupCommand {

    public static final String COMMAND_WORD = "remove";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Remove a contact to the group identified "
            + "by the group name and the contact ID number displayed in the contact list.\n "
            + "Parameters: GROUP_NAME (one word, cannot contain space) "
            + "[" + PREFIX_CONTACTID + " CONTACT_ID]...\n"
            + "Example: " + COMMAND_WORD + " BTS_ARMY "
            + PREFIX_CONTACTID + " 47337 ";

    public static final String MESSAGE_UI = "UI: now displaying all contacts in found group";
    public static final String MESSAGE_REMOVE_MEMBER_SUCCESS = "Removed contact to Group: %s%n" + MESSAGE_UI;
    public static final String MESSAGE_NOT_REMOVED = "At least one contactID to be removed must be provided.";

    //private final Group group;
    private final GroupNameEqualsKeywordPredicate predicate;
    private final EditGroupDescriptor editGroupDescriptor;

    /**
     * @param predicate of the group in the filtered group list to edit
     * @param editGroupDescriptor details to edit the group with
     */

    public RemoveMemberFromGroupCommand(GroupNameEqualsKeywordPredicate predicate,
        EditGroupDescriptor editGroupDescriptor) {

        requireNonNull(predicate);
        requireNonNull(editGroupDescriptor);

        //this.index = index;
        this.predicate = predicate;
        this.editGroupDescriptor = new EditGroupDescriptor(editGroupDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Group groupToEdit = findOneGroup(model, predicate);
        String membersNotFound = GroupCommand.notFoundString(notFound(groupToEdit, editGroupDescriptor.memberIds));

        // removes all user-input contactIds as members of this Group checks deferred
        Group editedGroup = createRemovedMemberGroup(groupToEdit, editGroupDescriptor);

        // check to ensure Group members are ContactIds that can be found in Model
        // this Group should only have contactId of contacts found in ContactList after calling setGroup
        Group verifiedGroup = GroupCommand.verifyGroupWithModel(model, editedGroup);

        model.setGroup(groupToEdit, verifiedGroup);

        model.updateFilteredGroupList(PREDICATE_SHOW_ALL_GROUPS);
        return new CommandResult(String.format(MESSAGE_REMOVE_MEMBER_SUCCESS + membersNotFound, verifiedGroup),
               ViewType.CONTACT);
    }

    /**
     * Creates and returns a {@code Group} with the details of {@code groupToEdit}
     * edited with {@code editGroupDescriptor}.
     */
    private static Group createRemovedMemberGroup(Group groupToEdit, EditGroupDescriptor editGroupDescriptor) {
        assert groupToEdit != null;

        GroupName updatedGroupName = editGroupDescriptor.getGroupName().orElse(groupToEdit.getGroupName());
        GroupDescription updatedGroupDescription = editGroupDescriptor.getGroupDescription()
            .orElse(groupToEdit.getGroupDescription());
        Set<MemberId> updatedMemberIds = new HashSet<>();
        if (editGroupDescriptor.getMemberIds().isPresent()) {
            updatedMemberIds.addAll(groupToEdit.getMemberIds().stream()
                .filter(member -> !editGroupDescriptor.getMemberIds().get().contains(member))
                .collect(Collectors.toSet())
            );
        } else {
            // if no memberIds found in editGroupDescriptor, do not remove any groups
            updatedMemberIds.addAll(groupToEdit.getMemberIds());
        }

        return new Group(updatedGroupName, updatedGroupDescription, updatedMemberIds);
    }

    /**
     * Checks and returns a set of {@code MemberId} which cannot be found as members of {@code Group}
     */
    private static Set<MemberId> notFound(Group group, Collection<MemberId> toRemove) {
        return toRemove.stream()
            .filter(target -> !group.getMemberIds().stream()
                .map(members -> members.value)
                .anyMatch(members -> members.equalsIgnoreCase(target.value)))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemoveMemberFromGroupCommand)) {
            return false;
        }

        // state check
        RemoveMemberFromGroupCommand e = (RemoveMemberFromGroupCommand) other;
        return predicate.equals(e.predicate)
                && editGroupDescriptor.equals(e.editGroupDescriptor);
    }

    /**
     * Stores the details to edit the group with. Each non-empty field value will replace the
     * corresponding field value of the group.
     */
    public static class EditGroupDescriptor {
        private GroupName groupName;
        private GroupDescription description;
        private Set<MemberId> memberIds;
        //private Set<Tag> tags;

        public EditGroupDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditGroupDescriptor(EditGroupDescriptor toCopy) {
            setGroupName(toCopy.groupName);
            setGroupDescription(toCopy.description);
            setMemberIds(toCopy.memberIds);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(groupName, description, memberIds);
        }

        public void setGroupName(GroupName groupName) {
            this.groupName = groupName;
        }

        public Optional<GroupName> getGroupName() {
            return Optional.ofNullable(groupName);
        }

        public void setGroupDescription(GroupDescription description) {
            this.description = description;
        }

        public Optional<GroupDescription> getGroupDescription() {
            return Optional.ofNullable(description);
        }

        /**
         * Sets {@code memberIds} to this object's {@code memberIds}.
         * A defensive copy of {@code memberIds} is used internally.
         */
        public void setMemberIds(Set<MemberId> memberIds) {
            this.memberIds = (memberIds != null) ? new HashSet<>(memberIds) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<MemberId>> getMemberIds() {
            return (memberIds != null) ? Optional.of(Collections.unmodifiableSet(memberIds)) : Optional.empty();
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

            return getGroupName().equals(e.getGroupName())
                    && getMemberIds().equals(e.getMemberIds());
        }
    }
}
