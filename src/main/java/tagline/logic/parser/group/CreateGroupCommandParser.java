package tagline.logic.parser.group;

import static tagline.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tagline.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tagline.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tagline.logic.parser.CliSyntax.PREFIX_NAME;
import static tagline.logic.parser.CliSyntax.PREFIX_PHONE;
import static tagline.logic.parser.CliSyntax.PREFIX_TAG;
import static tagline.logic.parser.group.GroupCliSyntax.PREFIX_CONTACT;

import java.util.Set;
import java.util.stream.Stream;

import tagline.logic.commands.group.CreateGroupCommand;
import tagline.logic.parser.ArgumentMultimap;
import tagline.logic.parser.ArgumentTokenizer;
import tagline.logic.parser.Parser;
import tagline.logic.parser.Prefix;
import tagline.logic.parser.exceptions.ParseException;
import tagline.model.person.Address;
import tagline.model.person.Email;
import tagline.model.person.Name;
import tagline.model.person.Person;
import tagline.model.person.Phone;
import tagline.model.tag.Tag;

/**
 * Parses input arguments and creates a new CreateGroupCommand object
 */
public class CreateGroupCommandParser implements Parser<CreateGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CreateGroupCommand
     * and returns an CreateGroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CreateGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap2 =
                ArgumentTokenizer.tokenize(args, PREFIX_CONTACT);

        if (!arePrefixesPresent(argMultimap2, PREFIX_CONTACT)
                || !argMultimap2.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateGroupCommand.MESSAGE_USAGE));
        }



        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateGroupCommand.MESSAGE_USAGE));
        }

        Name name = GroupParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = GroupParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = GroupParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = GroupParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Set<Tag> tagList = GroupParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Person person = new Person(name, phone, email, address, tagList);

        return new CreateGroupCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
