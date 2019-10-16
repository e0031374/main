package tagline.testutil;

import static tagline.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static tagline.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static tagline.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static tagline.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static tagline.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static tagline.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static tagline.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static tagline.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static tagline.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static tagline.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tagline.model.AddressBook;
import tagline.model.group.Group;
import tagline.model.group.GroupBook;
import tagline.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalGroups {

    //public static final Group sthgrouop = new GroupBuilder().with;

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253")
            .withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street").withTags("friends").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withAddress("michegan ave").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withAddress("little tokyo").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withAddress("4th street").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalGroups() {} // prevents instantiation

    /**
     * Returns an {@code GroupBook} with all the typical persons.
     */
    public static GroupBook getTypicalGroupBook() {
        GroupBook ab = new GroupBook();
        for (Group group : getTypicalGroups()) {
            ab.addGroup(group);
        }
        return ab;
    }

    public static List<Group> getTypicalGroups() {
        //return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
