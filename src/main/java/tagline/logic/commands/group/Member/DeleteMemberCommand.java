package tagline.logic.commands.group.member;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import tagline.commons.core.Messages;
import tagline.commons.core.index.Index;
import tagline.logic.commands.CommandResult;
import tagline.logic.commands.exceptions.CommandException;
import tagline.model.group.member.Member;
import tagline.model.group.member.MemberModel;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteMemberCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Member: %1$s";

    private final String targetId;

    public DeleteMemberCommand(String targetId) {
        this.targetId = targetId;
    }

    @Override
    public CommandResult execute(MemberModel model) throws CommandException {
        requireNonNull(model);
        List<Member> lastShownList = model.getFilteredMemberList();

        Optional<Member> optionalPersonToDelete = lastShownList.stream()
                .filter(member -> member.hasId(targetId))
                .findFirst();//lastShownList.get(targetId);

        if (optionalPersonToDelete.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Member personToDelete = optionalPersonToDelete.get();

        model.deleteMember(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteMemberCommand // instanceof handles nulls
                && targetId.equals(((DeleteMemberCommand) other).targetId)); // state check
    }
}
