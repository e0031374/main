package tagline.logic.commands.group.member;

import static java.util.Objects.requireNonNull;

import tagline.commons.core.Messages;
import tagline.logic.commands.Command;
import tagline.logic.commands.CommandResult;
import tagline.model.Model;
import tagline.model.group.member.MemberIdEqualsSearchIdPredicate;
import tagline.model.group.member.MemberModel;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindMemberCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final MemberIdEqualsSearchIdPredicate predicate;

    public FindMemberCommand(MemberIdEqualsSearchIdPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(MemberModel model) {
        requireNonNull(model);
        model.updateFilteredMemberList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredMemberList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindMemberCommand // instanceof handles nulls
                && predicate.equals(((FindMemberCommand) other).predicate)); // state check
    }
}
