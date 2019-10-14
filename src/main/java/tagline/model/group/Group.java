package tagline.model.group;

import static tagline.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import tagline.model.tag.Tag;

/**
 * Represents a Group in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Group {

    // Identity fields
    private final GroupName groupName;
    private final GroupDescription description;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Id> memberIds = new HashSet<>();
    //private final Set<GroupName> gn = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Group(GroupName groupName, Set<Id> memberIds, GroupDescription description, Set<Tag> tags) {
        requireAllNonNull(groupName, memberIds);
        this.groupName = groupName;
        this.memberIds.addAll(memberIds);
        this.description = description;

        this.tags.addAll(tags);
        //this.gn.add(groupName);
    }

    public GroupName getGroupName() {
        return groupName;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Id> getMemberIds() {
        return Collections.unmodifiableSet(memberIds);
    }

    public GroupDescription getDescription() {
        return description;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

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
        //return otherGroup.getName().equals(getName())
                //&& otherGroup.getPhone().equals(getPhone())
                //&& otherGroup.getEmail().equals(getEmail())
                //&& otherGroup.getAddress().equals(getAddress())
                //&& otherGroup.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(groupName, memberIds, description);
        //return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getGroupName())
                //.append(" Desription: ")
                //.append(getDescription())
                .append(" Members: ");
        getMemberIds().forEach(builder::append);
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
