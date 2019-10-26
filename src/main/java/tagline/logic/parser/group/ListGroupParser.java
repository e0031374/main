package tagline.logic.parser.group;

import static java.util.Objects.requireNonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tagline.logic.commands.group.ListGroupCommand;
import tagline.logic.commands.group.ListGroupCommand.Filter;
import tagline.logic.commands.group.ListGroupCommand.Filter.FilterType;
import tagline.logic.parser.Parser;
import tagline.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListGroupCommand object
 */
public class ListGroupParser implements Parser<ListGroupCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the ListGroupCommand
     * and returns an EditGroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListGroupCommand parse(String args) throws ParseException {
        requireNonNull(args);
        return new ListGroupCommand(GroupFilterUtil.generateFilter(args));
    }

    /**
     * Contains utility methods used for generating the *Filter classes from the argument string.
     */
    private static class GroupFilterUtil {
        private static final String HASHTAG_FILTER_FORMAT = "#";
        private static final String CONTACT_FILTER_FORMAT = "@";
        private static final String GROUP_FILTER_FORMAT = "%";

        private static final Pattern TAG_FILTER_FORMAT = Pattern.compile("["
                + HASHTAG_FILTER_FORMAT + CONTACT_FILTER_FORMAT + GROUP_FILTER_FORMAT + "].*");

        /**
         * Parses {@code argString} into a {@code Filter} and returns it. Leading and trailing whitespaces will be
         * trimmed.
         */
        public static Filter generateFilter(String argString) {
            String trimmedArg = argString.strip();

            // No filter provided, list all groups
            if (trimmedArg.isEmpty()) {
                return null;
            }

            Matcher filterMatcher = TAG_FILTER_FORMAT.matcher(trimmedArg);

            if (filterMatcher.matches()) {
                return null;
                /* TO ADD WHEN TAG IMPLEMENTED */
                // generateTagFilter(trimmedArg);
            } else {
                return generateKeywordFilter(trimmedArg);
            }
        }

        private static Filter generateKeywordFilter(String keyword) {
            return new Filter(keyword, FilterType.KEYWORD);
        }
    }
}
