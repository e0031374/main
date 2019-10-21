package tagline.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static tagline.logic.commands.note.ListNoteCommand.MESSAGE_KEYWORD_EMPTYLIST;

import javafx.collections.ObservableList;
import tagline.commons.core.Messages;

import tagline.logic.commands.CommandResult;
import tagline.logic.commands.exceptions.CommandException;
import tagline.model.Model;
import tagline.model.contact.Contact;
import tagline.model.group.ContactIdEqualsSearchIdsPredicate;
import tagline.model.group.Group;
import tagline.model.group.GroupDescription;
import tagline.model.group.GroupName;
import tagline.model.group.GroupNameEqualsKeywordPredicate;
import tagline.model.group.MemberId;
import tagline.model.util.SampleDataUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindGroupCommand extends GroupCommand {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_KEYWORD_EMPTYLIST = "No groups matching keyword";
    //public static final String MESSAGE_KEYWORD_EMPTYLIST = "No groups matching keyword: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final GroupNameEqualsKeywordPredicate predicate;

    public FindGroupCommand(GroupNameEqualsKeywordPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        //Group targetGroup = optionalGroup.get();
        Group targetGroup = findOneGroupWithPredicate(model, predicate);

        // set Group members
        GroupName updatedGroupName = targetGroup.getGroupName();
        GroupDescription updatedGroupDescription = targetGroup.getGroupDescription();
        Set<MemberId> verifiedGroupMemberIds = verifyMemberIdWithModel(model, targetGroup);

        Group editedGroup = new Group(updatedGroupName, updatedGroupDescription,
            verifiedGroupMemberIds);
        model.setGroup(targetGroup, editedGroup);

        return new CommandResult(
            String.format(Messages.MESSAGE_GROUP_MEMBERS_OVERVIEW, model.getFilteredContactList().size()));
    }


    /**
     * Finds and returns a {@code Group} with the GroupName of {@code groupName}
     * from {@code Model}.
     */
    private static Group findOneGroupWithPredicate(Model model, GroupNameEqualsKeywordPredicate predicate)
        throws CommandException {

        model.updateFilteredGroupList(predicate);
        List<Group> filteredGroupList = model.getFilteredGroupList();
        Optional<Group> optionalGroup = filteredGroupList.stream().findFirst();

        if (optionalGroup.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_INVALID_GROUP_NAME);
        }

        return optionalGroup.get();
    }

    /**
     * Checks and returns a {@code Set<MemberId>} with the MemberId of {@code targetGroup}
     * that can be found as {@code ContactId} in {@code Model}, side effect of setting ContactList to
     * show all found MemberIds of the Group.
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
                || (other instanceof FindGroupCommand // instanceof handles nulls
                && predicate.equals(((FindGroupCommand) other).predicate)); // state check
    }
}
