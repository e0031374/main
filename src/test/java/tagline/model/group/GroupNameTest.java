package tagline.model.group;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tagline.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class GroupNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new GroupName(null));
    }

    @Test
    public void constructor_invalidGroupName_throwsIllegalArgumentException() {
        String invalidGroupName = "";
        assertThrows(IllegalArgumentException.class, () -> new GroupName(invalidGroupName));
    }

    @Test
    public void isValidGroupName() {
        // null name
        assertThrows(NullPointerException.class, () -> GroupName.isValidGroupName(null));

        // invalid name
        assertFalse(GroupName.isValidGroupName("")); // empty string

        // valid name
        assertTrue(GroupName.isValidGroupName(" ")); // spaces only
        assertTrue(GroupName.isValidGroupName("  ")); // spaces only
        assertTrue(GroupName.isValidGroupName("^")); // only non-alphanumeric characters
        assertTrue(GroupName.isValidGroupName("peter*")); // contains non-alphanumeric characters
        assertTrue(GroupName.isValidGroupName("peter jack")); // alphabets only
        assertTrue(GroupName.isValidGroupName("12345")); // numbers only
        assertTrue(GroupName.isValidGroupName("peter the 2nd")); // alphanumeric characters
        assertTrue(GroupName.isValidGroupName("Capital Tan")); // with capital letters
        assertTrue(GroupName.isValidGroupName("David Roger Jackson Ray Jr 2nd")); // long names
    }
}
