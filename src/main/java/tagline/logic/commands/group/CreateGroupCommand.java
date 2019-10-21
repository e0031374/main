package tagline.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static tagline.logic.parser.group.GroupCliSyntax.PREFIX_CONTACTID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import tagline.commons.core.Messages;
import tagline.logic.commands.CommandResult;
import tagline.logic.commands.exceptions.CommandException;
import tagline.model.Model;
import tagline.model.group.ContactIdEqualsSearchIdsPredicate;
import tagline.model.group.Group;
import tagline.model.group.GroupDescription;
import tagline.model.group.GroupName;
import tagline.model.group.GroupNameEqualsKeywordPredicate;
import tagline.model.group.MemberId;

/**
 * Adds a group to the address book.
 */
public class CreateGroupCommand extends GroupCommand {

    public static final String COMMAND_WORD = "create";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a group. "
            + "Parameters: GROUP_NAME "
            + "[" + PREFIX_CONTACTID + "CONTACT_ID]...\n"
            + "Example: " + COMMAND_WORD + " BTS_ARMY "
            + PREFIX_CONTACTID + "1077 "
            + PREFIX_CONTACTID + "1078";

    public static final String MESSAGE_SUCCESS = "New group added: %1$s";
    public static final String MESSAGE_DUPLICATE_GROUP = "This group already exists in the group book";

    private final Group toAdd;

    /**
     * Creates an CreateGroupCommand to add the specified {@code Group}
     */
    public CreateGroupCommand(Group group) {
        requireNonNull(group);
        toAdd = group;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasGroup(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_GROUP);
        }

        // look for the contacts and get their contactID, then edit the Group
        // ensures Group members are ContactIds that can be found in Model
        GroupName editedGroupName = toAdd.getGroupName();
        GroupDescription editedGroupDescription = toAdd.getGroupDescription();
        Set<MemberId> verifiedGroupMemberIds = verifyMemberIdWithModel(model, toAdd);

        Group verifiedGroup = new Group(editedGroupName, editedGroupDescription,
                verifiedGroupMemberIds);

        model.addGroup(verifiedGroup);
        return new CommandResult(String.format(MESSAGE_SUCCESS, verifiedGroup));
    }

    /**
     * Checks and returns a {@code Set<MemberId>} with the MemberId of {@code targetGroup}
     * that can be found as {@code ContactId} in {@code Model}.
     */
    private static Set<MemberId> verifyMemberIdWithModel(Model model, Group targetGroup) {
        List<String> members = targetGroup.getMemberIds()
                .stream()
                .map(member -> member.value)
                .collect(Collectors.toList());

        // to display all contacts which are Group members
        model.updateFilteredContactList(new ContactIdEqualsSearchIdsPredicate(members));

        // this bit to ensure groupmembers are as updated in case of storage error
        // done by getting all the MemberIds in the group, AddressBook
        // for those contacts, then make them into MemberIds
        Set<MemberId> updatedGroupMemberIds = model.getFilteredContactList()
                .stream()
                .map(contact -> contact.getContactId().toInteger().toString())
                .map(member -> new MemberId(member))
                .collect(Collectors.toSet());

        return updatedGroupMemberIds;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CreateGroupCommand // instanceof handles nulls
                && toAdd.equals(((CreateGroupCommand) other).toAdd));
    }
}
