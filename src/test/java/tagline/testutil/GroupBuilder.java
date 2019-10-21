package tagline.testutil;

import java.util.HashSet;
import java.util.Set;

import tagline.model.group.MemberId;
import tagline.model.group.Group;
import tagline.model.group.GroupDescription;
import tagline.model.group.GroupName;
import tagline.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class GroupBuilder {

    public static final String DEFAULT_GROUPNAME = "SHIELD";
    public static final String DEFAULT_GROUPDESCRIPTION = "Strategic Homeland Intervention "
        + "Enforcement Logistics Divison";
    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private GroupName groupName;
    private GroupDescription groupDescription;
    private Set<MemberId> memberIds;

    public GroupBuilder() {
        groupName = new GroupName(DEFAULT_NAME);
        groupDescription = new GroupDescription(DEFAULT_NAME);
        memberIds = new HashSet<>();
    }

    /**
     * Initializes the GroupBuilder with the data of {@code personToCopy}.
     */
    public GroupBuilder(Group groupToCopy) {
        groupName = groupToCopy.getGroupName();
        memberIds = new HashSet<>(groupToCopy.getMemberIds());
        groupDescription = groupToCopy.getGroupDescription();
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
        this.memberIds = SampleDataUtil.getMemberIdSet(memberIds);
        return this;
    }

    public Group build() {
        return new Group(groupName, groupDescription, memberIds);
    }

}
