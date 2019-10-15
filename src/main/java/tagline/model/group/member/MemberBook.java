package tagline.model.group.member;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import tagline.model.group.member.Member;
import tagline.model.group.member.UniqueMemberList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameMember comparison)
 */
public class MemberBook implements ReadOnlyMemberBook {

    private final UniqueMemberList members;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        members = new UniqueMemberList();
    }

    public MemberBook() {}

    /**
     * Creates an MemberBook using the Members in the {@code toBeCopied}
     */
    public MemberBook(ReadOnlyMemberBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the member list with {@code members}.
     * {@code members} must not contain duplicate members.
     */
    public void setMembers(List<Member> members) {
        this.members.setMembers(members);
    }

    /**
     * Resets the existing data of this {@code MemberBook} with {@code newData}.
     */
    public void resetData(ReadOnlyMemberBook newData) {
        requireNonNull(newData);

        setMembers(newData.getMemberList());
    }

    //// member-level operations

    /**
     * Returns true if a member with the same identity as {@code member} exists in the address book.
     */
    public boolean hasMember(Member member) {
        requireNonNull(member);
        return members.contains(member);
    }

    /**
     * Adds a member to the address book.
     * The member must not already exist in the address book.
     */
    public void addMember(Member p) {
        members.add(p);
    }

    /**
     * Replaces the given member {@code target} in the list with {@code editedMember}.
     * {@code target} must exist in the address book.
     * The member identity of {@code editedMember} must not be the same as another existing member in the address book.
     */
    public void setMember(Member target, Member editedMember) {
        requireNonNull(editedMember);

        members.setMember(target, editedMember);
    }

    /**
     * Removes {@code key} from this {@code MemberBook}.
     * {@code key} must exist in the address book.
     */
    public void removeMember(Member key) {
        members.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return members.asUnmodifiableObservableList().size() + " members";
        // TODO: refine later
    }

    @Override
    public ObservableList<Member> getMemberList() {
        return members.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MemberBook // instanceof handles nulls
                && members.equals(((MemberBook) other).members));
    }

    @Override
    public int hashCode() {
        return members.hashCode();
    }
}
