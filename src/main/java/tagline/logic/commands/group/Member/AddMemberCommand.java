package tagline.logic.commands.group.member;

import static java.util.Objects.requireNonNull;
import static tagline.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tagline.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tagline.logic.parser.CliSyntax.PREFIX_NAME;
import static tagline.logic.parser.CliSyntax.PREFIX_PHONE;
import static tagline.logic.parser.CliSyntax.PREFIX_TAG;

import tagline.logic.commands.Command;
import tagline.logic.commands.CommandResult;
import tagline.logic.commands.exceptions.CommandException;
import tagline.model.group.member.Member;
import tagline.model.group.member.MemberModel;

/**
 * Adds a person to the address book.
 */
public class AddMemberCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
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

    public static final String MESSAGE_SUCCESS = "New member added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This member already exists in the member book";

    private final Member toAdd;

    /**
     * Creates an AddMemberCommand to add the specified {@code Member}
     */
    public AddMemberCommand(Member person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(MemberModel model) throws CommandException {
        requireNonNull(model);

        if (model.hasMember(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addMember(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddMemberCommand // instanceof handles nulls
                && toAdd.equals(((AddMemberCommand) other).toAdd));
    }
}
