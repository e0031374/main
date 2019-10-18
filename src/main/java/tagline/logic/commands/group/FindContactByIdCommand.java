package tagline.logic.commands.group;

import static java.util.Objects.requireNonNull;

import tagline.commons.core.Messages;
import tagline.logic.commands.Command;
import tagline.logic.commands.CommandResult;
import tagline.model.Model;
import tagline.model.contact.ContactModel;
import tagline.model.group.ContactIdEqualsIdPredicate;

/**
 * Finds and lists all contacts in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindContactByIdCommand extends GroupCommand {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds contact whose id equals "
            + "the specified id (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD\n"
            + "Example: " + COMMAND_WORD + " 62353";

    private final ContactIdEqualsIdPredicate predicate;

    public FindContactByIdCommand(ContactIdEqualsIdPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredContactList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_CONTACTS_LISTED_OVERVIEW, model.getFilteredContactList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindContactByIdCommand // instanceof handles nulls
                && predicate.equals(((FindContactByIdCommand) other).predicate)); // state check
    }
}
