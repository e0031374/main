package tagline.model.group.member;

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
import tagline.model.group.member.Member;

/**
 * Represents the in-memory model of the address book data.
 */
public class MemberModelManager implements MemberModel {
    private static final Logger logger = LogsCenter.getLogger(MemberModelManager.class);

    private final MemberBook memberBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Member> filteredMembers;

    /**
     * Initializes a MemberModelManager with the given memberBook and userPrefs.
     */
    public MemberModelManager(ReadOnlyMemberBook memberBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(memberBook, userPrefs);

        logger.fine("Initializing with address book: " + memberBook + " and user prefs " + userPrefs);

        this.memberBook = new MemberBook(memberBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredMembers = new FilteredList<>(this.memberBook.getMemberList());
    }

    public MemberModelManager() {
        this(new MemberBook(), new UserPrefs());
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
    public Path getMemberBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setMemberBookFilePath(Path memberBookFilePath) {
        requireNonNull(memberBookFilePath);
        //userPrefs.setMemberBookFilePath(memberBookFilePath);
    }

    //=========== MemberBook ================================================================================

    @Override
    public void setMemberBook(ReadOnlyMemberBook memberBook) {
        this.memberBook.resetData(memberBook);
    }

    @Override
    public ReadOnlyMemberBook getMemberBook() {
        return memberBook;
    }

    @Override
    public boolean hasMember(Member person) {
        requireNonNull(person);
        return memberBook.hasMember(person);
    }

    @Override
    public void deleteMember(Member target) {
        memberBook.removeMember(target);
    }

    @Override
    public void addMember(Member person) {
        memberBook.addMember(person);
        updateFilteredMemberList(PREDICATE_SHOW_ALL_MEMBERS);
    }

    @Override
    public void setMember(Member target, Member editedMember) {
        requireAllNonNull(target, editedMember);

        memberBook.setMember(target, editedMember);
    }

    //=========== Filtered Member List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Member} backed by the internal list of
     * {@code versionedMemberBook}
     */
    @Override
    public ObservableList<Member> getFilteredMemberList() {
        return filteredMembers;
    }

    @Override
    public void updateFilteredMemberList(Predicate<Member> predicate) {
        requireNonNull(predicate);
        filteredMembers.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof MemberModelManager)) {
            return false;
        }

        // state check
        MemberModelManager other = (MemberModelManager) obj;
        return memberBook.equals(other.memberBook)
                && userPrefs.equals(other.userPrefs)
                && filteredMembers.equals(other.filteredMembers);
    }

}
