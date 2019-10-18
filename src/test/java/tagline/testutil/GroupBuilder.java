package tagline.testutil;

import java.util.HashSet;
import java.util.Set;

import tagline.model.group.Group;
import tagline.model.group.GroupDescription;
import tagline.model.group.GroupName;
import tagline.model.group.ContactId;
import tagline.model.person.Address;
import tagline.model.person.Email;
import tagline.model.person.Name;
import tagline.model.person.Phone;
import tagline.model.tag.Tag;
import tagline.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class GroupBuilder {

    public static final String DEFAULT_GROUPNAME = "";
    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private GroupName groupName;
    private Set<ContactId> memberIds;
    private GroupDescription groupDescription;

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;

    public GroupBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the GroupBuilder with the data of {@code personToCopy}.
     */
    public GroupBuilder(Group groupToCopy) {
        groupName = groupToCopy.getGroupName();
        memberIds = new HashSet<>(groupToCopy.getMemberIds());
        //groupDescription = groupToCopy.getGroupDescription();

        //tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code GroupName} of the {@code Group} that we are building.
     */
    public GroupBuilder withGroupName(String groupName) {
        this.groupName = new GroupName(groupName);
        return this;
    }

    /**
     * Sets the {@code GroupDescription} of the {@code Group} that we are building.
     */
    public GroupBuilder withGroupDescription(String groupDescription) {
        this.groupDescription = new GroupDescription(groupDescription);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public GroupBuilder withMemberIds(String ... memberIds) {
        this.memberIds = SampleGroupDataUtil.getMemberIds(memberIds);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public GroupBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public GroupBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public GroupBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public GroupBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public GroupBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Group build() {
        //return new Person(name, phone, email, address, tags);
        //return new Group(groupName, memberIds, groupDescription);
        return new Group(groupName, memberIds);
    }

}
