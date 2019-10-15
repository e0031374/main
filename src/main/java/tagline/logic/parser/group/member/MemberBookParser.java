package tagline.logic.parser.group.member;

import static tagline.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tagline.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tagline.logic.commands.Command;
import tagline.logic.commands.HelpCommand;
import tagline.logic.commands.ListCommand;
import tagline.logic.parser.AddMemberCommandParser;
import tagline.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class MemberBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final String ADD_MEMBER_COMMAND = "ADD_MEMBER_COMMAND";
    private static final String DELETE_MEMBER_COMMAND = "DELETE_MEMBER_COMMAND";
    private static final String FIND_MEMBER_COMMAND = "FIND_MEMBER_COMMAND";
    private static final String LIST_MEMBER_COMMAND = "LIST_MEMBER_COMMAND";

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput, Contact contactArg) throws ParseException {
        // userInput has to be one of the above XXX_MEMBER_COMMAND
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case ADD_MEMBER_COMMAND:
            return new AddMemberCommandParser().parse(contactArg);

        case DELETE_MEMBER_COMMAND:
            return new DeleteMemberCommandParser().parse(contactArg);

        case FIND_MEMBER_COMMAND:
            // Contact -> Member, to support DELETE_MEMBER_COMMAND find then delete
            return new FindMemberCommandParser().parse(contactArg);

        case LIST_MEMBER_COMMAND:
            return new ListCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
