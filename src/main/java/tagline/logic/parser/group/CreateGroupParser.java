package tagline.logic.parser.group;

import static tagline.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tagline.logic.parser.group.GroupCliSyntax.PREFIX_CONTACTID;
import static tagline.logic.parser.group.GroupCliSyntax.PREFIX_GROUPDESCRIPTION;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import tagline.logic.commands.group.CreateGroupCommand;
import tagline.logic.parser.ArgumentMultimap;
import tagline.logic.parser.ArgumentTokenizer;
import tagline.logic.parser.Parser;
import tagline.logic.parser.Prefix;
import tagline.logic.parser.exceptions.ParseException;
import tagline.model.contact.ContactId;
import tagline.model.group.Group;
import tagline.model.group.GroupDescription;
import tagline.model.group.GroupName;

/**
 * Parses input arguments and creates a new CreateGroupCommand object
 */
public class CreateGroupParser implements Parser<CreateGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CreateGroupCommand
     * and returns an CreateGroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CreateGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CONTACTID);

        if (!arePrefixesPresent(argMultimap, PREFIX_CONTACTID)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateGroupCommand.MESSAGE_USAGE));
        }

        //Set<Tag> tagList = GroupParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        GroupName groupName = GroupParserUtil.parseGroupName(argMultimap.getPreamble());
        GroupDescription groupDescription = GroupParserUtil.parseGroupDescription(
            argMultimap.getValue(PREFIX_GROUPDESCRIPTION).orElse(""));
        //Set<ContactId> memberIds = GroupParserUtil.parseMemberIds(argMultimap.getAllValues(PREFIX_CONTACTID));
        List<String> memberList = argMultimap.getAllValues(PREFIX_CONTACTID);
        Set<ContactId> memberIds = new HashSet<>();
        Group group = new Group(groupName, groupDescription, memberIds);

        return new CreateGroupCommand(group, memberList);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
