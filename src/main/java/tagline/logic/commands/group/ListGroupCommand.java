package tagline.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static tagline.logic.commands.group.ListGroupCommand.Filter.FilterType.MEMBER;
import static tagline.model.group.GroupModel.PREDICATE_SHOW_ALL_GROUPS;

import java.util.Arrays;
import java.util.List;

import tagline.logic.commands.CommandResult;
import tagline.logic.commands.exceptions.CommandException;
import tagline.model.Model;
import tagline.model.group.GroupNameEqualsKeywordPredicate;

/**
 * Lists all contacts in the address book to the user.
 */
public class ListGroupCommand extends GroupCommand {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all groups";
    public static final String MESSAGE_KEYWORD_SUCCESS = "Listed groups for keyword: %1$s";
    public static final String MESSAGE_KEYWORD_EMPTYLIST = "No groups matching keyword: %1$s";
    public static final String MESSAGE_INVALID_COMMAND = "INVALID COMMAND";

    private final Filter filter;

    /**
     * @param filter to list groups by
     */
    public ListGroupCommand(Filter filter) {
        this.filter = filter;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (filter == null) { // No filter, list all groups
            model.updateFilteredGroupList(PREDICATE_SHOW_ALL_GROUPS);
            return new CommandResult(MESSAGE_SUCCESS);
        } else if (filter.filterType.equals(MEMBER)) {
            // list if group contains members (filter.filterType.equals(MEMEBER))
            return filterAndListByMember(model);
        } else {
            throw new CommandException(MESSAGE_INVALID_COMMAND);
        }

        /* TODO Implement filter by tag */
    }

    /**
     * Filter group list by String keyword
     */
    private CommandResult filterAndListByMember(Model model) throws CommandException {
        model.updateFilteredGroupList(PREDICATE_SHOW_ALL_GROUPS);

        List<String> keywordList = Arrays.asList(filter.filterValue);
        GroupNameEqualsKeywordPredicate predicate = new GroupNameEqualsKeywordPredicate(keywordList);

        model.updateFilteredGroupList(predicate);

        if (model.getFilteredGroupList().size() == 0) {
            throw new CommandException(String.format(MESSAGE_KEYWORD_EMPTYLIST, filter.filterValue));
        }

        return new CommandResult(String.format(MESSAGE_KEYWORD_SUCCESS, filter.filterValue));
    }

    //@@author shiweing
    /**
     * Stores filter for group listing.
     */
    public static class Filter {
        /**
         * Represents the type of filter to list groups by.
         */
        public enum FilterType {
            KEYWORD, MEMBER
        }

        private final String filterValue;
        private final FilterType filterType;

        public Filter(String filterValue, FilterType filterType) {
            this.filterValue = filterValue;
            this.filterType = filterType;
        }

        public String getFilterValue() {
            return filterValue;
        }

        public FilterType getFilterType() {
            return filterType;
        }
    }
}
