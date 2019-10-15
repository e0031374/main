package tagline.model.group.member;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import tagline.commons.core.GuiSettings;
import tagline.model.ReadOnlyUserPrefs;
import tagline.model.group.member.Member;

/**
 * The API of the MemberModel component.
 */
public interface MemberModel {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Member> PREDICATE_SHOW_ALL_MEMBERS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getMemberBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setMemberBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setMemberBook(ReadOnlyMemberBook addressBook);

    /** Returns the MemberBook */
    ReadOnlyMemberBook getMemberBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasMember(Member person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deleteMember(Member target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addMember(Member person);

    /**
     * Replaces the given person {@code target} with {@code editedMember}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedMember} must not be the same as another existing person in the address book.
     */
    void setMember(Member target, Member editedMember);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Member> getFilteredMemberList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredMemberList(Predicate<Member> predicate);
}
