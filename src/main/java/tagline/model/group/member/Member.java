package tagline.model.group.member;

import static tagline.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import tagline.model.tag.Tag;

/**
 * Represents a Member in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Member {

    // Identity fields
    private final Id memberId;

    // Data fields
    //private final MemberDescription description;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    //public Member(Id memberId, MemberDescription description) {
    //    requireAllNonNull(memberId, description);
    //    this.memberId = memberId;
    //    this.description = description;
    //}

    public Member(Id memberId) {
        requireAllNonNull(memberId);
        this.memberId = memberId;
        //this.description = new MemberDescription("");
    }

    public Id getMemberId() {
        return memberId;
    }

    public String getMemberIdAsString() {
        return memberId.getInteger().toString();
    }

    //public boolean hasId(String stringId) {
    //    return stringId.equals(memberId.getInteger());
    //}

    //public MemberDescription getMemberDescription() {
    //    return description;
    //}

    /**
     * Returns true if both persons of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameMember(Member otherMember) {
        if (otherMember == this) {
            return true;
        }

        return otherMember != null
                && otherMember.getMemberId().equals(getMemberId());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Member)) {
            return false;
        }

        Member otherMember = (Member) other;
        return otherMember.getMemberId().equals(getMemberId());
            //&& otherMember.getMemberDescription().equals(getMemberDescription());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        //return Objects.hash(memberId, description);
        return Objects.hash(memberId);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getMemberId());
                //.append(" Desription: ")
                //.append(getMemberDescription())
        //builder.append(getName())
        //        .append(" Phone: ")
        //        .append(getPhone())
        //        .append(" Email: ")
        //        .append(getEmail())
        //        .append(" Address: ")
        //        .append(getAddress())
        //        .append(" Tags: ");
        //getTags().forEach(builder::append);
        return builder.toString();
    }

}
