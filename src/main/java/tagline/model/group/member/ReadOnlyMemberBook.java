package tagline.model.group.member;

import javafx.collections.ObservableList;
import tagline.model.group.member.Member;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyMemberBook {

    /**
     * Returns an unmodifiable view of the groups list.
     * This list will not contain any duplicate groups.
     */
    ObservableList<Member> getMemberList();

}
