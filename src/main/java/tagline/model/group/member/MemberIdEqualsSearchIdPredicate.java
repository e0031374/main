package tagline.model.group.member;

import java.util.List;
import java.util.function.Predicate;

import tagline.commons.util.StringUtil;

/**
 * Tests that a {@code Member}'s {@code Name} matches any of the keywords given.
 */
public class MemberIdEqualsSearchIdPredicate implements Predicate<Member> {
    private final List<String> keywords;

    public MemberIdEqualsSearchIdPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Member member) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(member.getMemberIdAsString(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MemberIdEqualsSearchIdPredicate // instanceof handles nulls
                && keywords.equals(((MemberIdEqualsSearchIdPredicate) other).keywords)); // state check
    }

}
