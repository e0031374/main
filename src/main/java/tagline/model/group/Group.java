package tagline.model.group;

import static tagline.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import tagline.logic.parser.group.member.MemberBookParser;
import tagline.model.group.member.MemberModel;

/**
 * Represents a Group in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Group {

    // Member Logic
    //private final MemberBookParser memberBookParser;

    // Identity fields
    private final GroupName groupName;
    //private final GroupDescription description;

    // Data fields
    //private final MemberModel members;
    private final Set<ContactId> memberIds = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Group(GroupName groupName, Set<ContactId> memberIds) {
        requireAllNonNull(groupName, memberIds);
        this.groupName = groupName;
        this.memberIds.addAll(memberIds);
        //this.tags.addAll(tags);
    }
    //public Group(GroupName groupName, Set<MemberId> memberIds, GroupDescription description) {
    //public Group(GroupName groupName, MemberModel members, GroupDescription description) {
    //    requireAllNonNull(groupName, members);
    //    this.groupName = groupName;
    //    this.members = members;
    //    this.description = description;
    //    this.memberBookParser = new MemberBookParser();
    //    //this.tags.addAll(tags);
    //}

    public GroupName getGroupName() {
        return groupName;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<ContactId> getMemberIds() {
        return Collections.unmodifiableSet(memberIds);
    }
    //public MemberModel getMembers() {
    //    return members; //Collections.unmodifiableSet(memberIds);
    //}

    //public GroupDescription getDescription() {
    //    return description;
    //}

    /**
     * Returns true if both persons of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameGroup(Group otherGroup) {
        if (otherGroup == this) {
            return true;
        }

        return otherGroup != null
                && otherGroup.getGroupName().equals(getGroupName());
                //&& otherGroup.getName().equals(getName())
                //&& (otherGroup.getPhone().equals(getPhone()) || otherGroup.getEmail().equals(getEmail()));
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

        if (!(other instanceof Group)) {
            return false;
        }

        Group otherGroup = (Group) other;
        return otherGroup.getGroupName().equals(getGroupName())
                && otherGroup.getMemberIds().equals(getMemberIds());
                //&& otherGroup.getMembers().equals(getMembers());
        //return otherGroup.getName().equals(getName())
                //&& otherGroup.getPhone().equals(getPhone())
                //&& otherGroup.getEmail().equals(getEmail())
                //&& otherGroup.getAddress().equals(getAddress())
                //&& otherGroup.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        //return Objects.hash(groupName, members, description);
        return Objects.hash(groupName, memberIds);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getGroupName())
                //.append(" Desription: ")
                //.append(getDescription())
                .append(" Members: ");
                //.append(getMembers());
        getMemberIds().forEach(builder::append);
        //getMemberIds().forEach(builder::append);
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
