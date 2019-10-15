package tagline.logic.parser.group;

import static java.util.Objects.requireNonNull;
import static tagline.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tagline.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tagline.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tagline.logic.parser.CliSyntax.PREFIX_NAME;
import static tagline.logic.parser.CliSyntax.PREFIX_PHONE;
import static tagline.logic.parser.CliSyntax.PREFIX_TAG;
import static tagline.logic.parser.group.GroupCliSyntax.PREFIX_CONTACTID;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import tagline.commons.core.index.Index;
import tagline.logic.commands.group.AddMemberToGroupCommand;
import tagline.logic.commands.group.AddMemberToGroupCommand.EditGroupDescriptor;
import tagline.logic.parser.ArgumentMultimap;
import tagline.logic.parser.ArgumentTokenizer;
import tagline.logic.parser.Parser;
import tagline.logic.parser.ParserUtil;
import tagline.logic.parser.exceptions.ParseException;
import tagline.model.group.MemberId;
import tagline.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddMemberToGroupCommand object
 */
public class AddMemberToGroupCommandParser implements Parser<AddMemberToGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddMemberToGroupCommand
     * and returns an AddMemberToGroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddMemberToGroupCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        String targetGroupName;
        targetGroupName = argMultimap.getPreamble(); //GroupParserUtil.parseIndex(argMultimap.getPreamble());

        //try {
        //    targetGroupName = argMultimap.getPreamble(); //GroupParserUtil.parseIndex(argMultimap.getPreamble());
        //} catch (ParseException pe) {
        //    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMemberToGroupCommand.MESSAGE_USAGE), pe);
        //}

        EditGroupDescriptor editGroupDescriptor = new EditGroupDescriptor();
        //if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
        //    editGroupDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        //}
        //parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editGroupDescriptor::setTags);
        parseMemberIdsForEdit(argMultimap.getAllValues(PREFIX_CONTACTID)).ifPresent(editGroupDescriptor::setMemberIds);

        if (!editGroupDescriptor.isAnyFieldEdited()) {
            throw new ParseException(AddMemberToGroupCommand.MESSAGE_NOT_EDITED);
        }

        return new AddMemberToGroupCommand(targetGroupName, editGroupDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<MemberId>> parseMemberIdsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(GroupParserUtil.parseMemberIds(tagSet));
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
