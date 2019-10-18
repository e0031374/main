package tagline.model.group;

import java.util.List;
import java.util.function.Predicate;

import tagline.commons.util.StringUtil;
import tagline.model.contact.Contact;

/**
 * Tests that a {@code Contact}'s {@code Id} matches the searchId given.
 */
public class ContactIdEqualsIdPredicate implements Predicate<Contact> {
    private final String searchId;

    public ContactIdEqualsIdPredicate(String searchId) {
        // this ensures padded front zeroes are removed
        // does it?? test this
        this.searchId = Long.valueOf(searchId).toString();
    }

    public ContactIdEqualsIdPredicate(int searchId) {
        // this ensures padded front zeroes are removed
        // does it?? test this
        this(String.valueOf(searchId));
    }

    @Override
    public boolean test(Contact contact) {
        return contact.getContactId().toInteger().toString().equals(searchId);
        //return keywords.stream()
        //        .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(String.valueOf(contact.getDigit()), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ContactIdEqualsIdPredicate // instanceof handles nulls
                && searchId.equals(((ContactIdEqualsIdPredicate) other).searchId)); // state check
    }

}
