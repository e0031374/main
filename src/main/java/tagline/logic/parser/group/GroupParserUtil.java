package tagline.logic.parser.group;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import tagline.commons.core.index.Index;
import tagline.commons.util.StringUtil;
import tagline.logic.parser.exceptions.ParseException;
import tagline.model.group.GroupDescription;
import tagline.model.group.GroupName;
import tagline.model.group.MemberId;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class GroupParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code GroupName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static GroupName parseGroupName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!GroupName.isValidGroupName(trimmedName)) {
            throw new ParseException(GroupName.MESSAGE_CONSTRAINTS);
        }
        return new GroupName(trimmedName);
    }

    /**
     * Parses a {@code String description} into a {@code GroupDescription}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code description} is invalid.
     */
    public static GroupDescription parseGroupDescription(String description) throws ParseException {
        requireNonNull(description);
        String trimmedDescription = description.trim();
        if (!GroupName.isValidGroupName(trimmedDescription)) {
            throw new ParseException(GroupDescription.MESSAGE_CONSTRAINTS);
        }
        return new GroupDescription(trimmedDescription);
    }

    // NOTE
    // how to get contacts?
    // create a IdEqualsPredicate (tagline.model.contact.) vs NameContainsPredicate
    // findContactByIdCommand (tagline.logic.commands.contact.) vs findContactCommand
    //      this takes in Model (ContactModel)
    // findContactByIdParser

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static MemberId parseMemberId(String id) throws ParseException {
        requireNonNull(id);
        String trimmedId = id.trim();
        if (!MemberId.isValidMemberId(trimmedId)) {
            throw new ParseException(MemberId.MESSAGE_CONSTRAINTS);
        }
        return new MemberId(trimmedId);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<MemberId> parseMemberIds(Collection<String> ids) throws ParseException {
        requireNonNull(ids);
        final Set<MemberId> memberSet = new HashSet<>();
        for (String id : ids) {
            memberSet.add(parseMemberId(id));
        }
        return memberSet;
    }
}