package tagline.model.group;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import tagline.model.contact.Contact;

/**
 * Tests that a {@code Contact}'s {@code Name} matches any of the keywords given.
 */
public class GroupMembersContainsSearchIdsPredicate implements Predicate<Group> {
    private final List<String> keywords;

    public GroupMembersContainsSearchIdsPredicate(List<String> keywords) {
        this.keywords = keywords.stream()
            .mapToLong(Long::valueOf)
            .mapToObj(String::valueOf)
            .collect(Collectors.toList());
    }

    @Override
    public boolean test(Group group) {
        return keywords.stream()
                .anyMatch(keyword -> group.getMemberIds()
                    .stream()
                    .map(member -> member.value)
                    .mapToLong(Long::valueOf)
                    .mapToObj(String::valueOf)
                    .anyMatch(member -> keyword.equalsIgnoreCase(member))
                );
        //.anyMatch(keyword -> StringUtil.containsWordIgnoreCase(contact.getName().fullName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof GroupMembersContainsSearchIdsPredicate // instanceof handles nulls
                && keywords.equals(((GroupMembersContainsSearchIdsPredicate) other).keywords)); // state check
    }

}
