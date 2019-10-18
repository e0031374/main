package tagline.model.group;

import static java.util.Objects.requireNonNull;
import static tagline.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import tagline.commons.core.GuiSettings;
import tagline.commons.core.LogsCenter;
import tagline.model.ReadOnlyUserPrefs;
import tagline.model.UserPrefs;
import tagline.model.group.Group;

/**
 * Represents the in-memory model of the address book data.
 */
public class GroupModelManager implements GroupModel {
    private static final Logger logger = LogsCenter.getLogger(GroupModelManager.class);

    private final GroupBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Group> filteredGroups;

    /**
     * Initializes a GroupModelManager with the given addressBook and userPrefs.
     */
    public GroupModelManager(ReadOnlyGroupBook addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new GroupBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredGroups = new FilteredList<>(this.addressBook.getGroupList());
    }

    public GroupModelManager() {
        this(new GroupBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getGroupBookFilePath() {
        return userPrefs.getGroupBookFilePath();
    }

    @Override
    public void setGroupBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setGroupBookFilePath(addressBookFilePath);
    }

    //=========== GroupBook ================================================================================

    @Override
    public void setGroupBook(ReadOnlyGroupBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyGroupBook getGroupBook() {
        return addressBook;
    }

    @Override
    public boolean hasGroup(Group group) {
        requireNonNull(group);
        return addressBook.hasGroup(group);
    }

    @Override
    public void deleteGroup(Group target) {
        addressBook.removeGroup(target);
    }

    @Override
    public void addGroup(Group group) {
        addressBook.addGroup(group);
        updateFilteredGroupList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setGroup(Group target, Group editedGroup) {
        requireAllNonNull(target, editedGroup);

        addressBook.setGroup(target, editedGroup);
    }

    //=========== Filtered Group List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Group} backed by the internal list of
     * {@code versionedGroupBook}
     */
    @Override
    public ObservableList<Group> getFilteredGroupList() {
        return filteredGroups;
    }

    @Override
    public void updateFilteredGroupList(Predicate<Group> predicate) {
        requireNonNull(predicate);
        filteredGroups.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof GroupModelManager)) {
            return false;
        }

        // state check
        GroupModelManager other = (GroupModelManager) obj;
        return addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredGroups.equals(other.filteredGroups);
    }

}
