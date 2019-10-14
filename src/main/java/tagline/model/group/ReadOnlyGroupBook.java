package tagline.model.group;

import javafx.collections.ObservableList;
import tagline.model.group.Group;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyGroupBook {

    /**
     * Returns an unmodifiable view of the groups list.
     * This list will not contain any duplicate groups.
     */
    ObservableList<Group> getGroupList();

}
