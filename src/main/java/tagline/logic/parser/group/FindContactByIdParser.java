package tagline.logic.parser.group;

import static tagline.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import tagline.logic.commands.group.FindContactByIdCommand;
import tagline.logic.parser.Parser;
import tagline.logic.parser.exceptions.ParseException;
import tagline.model.group.ContactIdEqualsIdPredicate;

/**
 * Parses input arguments and creates a new FindContactByIdCommand object
 */
public class FindContactByIdParser implements Parser<FindContactByIdCommand> {

    public static final String VALIDATION_REGEX = "\\d+";
    public static final String MESSAGE_INVALID_SEARCH_TERM = "Contact Id search numbers should only "
        + "contain numbers.";

    /**
     * Parses the given {@code String} of arguments in the context of the FindContactByIdCommand
     * and returns a FindContactByIdCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindContactByIdCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindContactByIdCommand.MESSAGE_USAGE));
        }

        if (!isValidContactId(trimmedArgs)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_SEARCH_TERM, FindContactByIdCommand.MESSAGE_USAGE));
        }

        //String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindContactByIdCommand(new ContactIdEqualsIdPredicate(trimmedArgs));
    }

    public static boolean isValidContactId(String stringId) {
        return stringId.matches(VALIDATION_REGEX);
    }

}
