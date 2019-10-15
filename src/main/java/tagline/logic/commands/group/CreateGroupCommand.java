package tagline.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static tagline.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tagline.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tagline.logic.parser.CliSyntax.PREFIX_NAME;
import static tagline.logic.parser.CliSyntax.PREFIX_PHONE;
import static tagline.logic.parser.CliSyntax.PREFIX_TAG;

import tagline.logic.commands.Command;
import tagline.logic.commands.CommandResult;
import tagline.logic.commands.exceptions.CommandException;
import tagline.model.Model;
import tagline.model.person.Person;
import tagline.model.group.Group;

/**
 * Adds a group to the address book.
 */
public class CreateGroupCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a group to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New group added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This group already exists in the address book";

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
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addGroup(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CreateGroupCommand // instanceof handles nulls
                && toAdd.equals(((CreateGroupCommand) other).toAdd));
    }
}
