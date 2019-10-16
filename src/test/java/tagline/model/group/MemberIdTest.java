package tagline.model.group;

//import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tagline.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

class MemberIdTest {
    @Test
    public void constructor_invalidMemberId_throwsIllegalArgumentException() {
        String invalidMemberId1 = "";
        assertThrows(IllegalArgumentException.class, () -> new MemberId(invalidMemberId1));

        String invalidMemberId2 = "12asd";
        assertThrows(IllegalArgumentException.class, () -> new MemberId(invalidMemberId2));

        String invalidMemberId3 = "one";
        assertThrows(IllegalArgumentException.class, () -> new MemberId(invalidMemberId3));
    }

    @Test
    public void isValidNote() {
        // null phone number
        assertThrows(NullPointerException.class, () -> MemberId.isValidMemberId(null));

        // invalid noteId numbers
        assertFalse(MemberId.isValidMemberId("")); // empty string
        assertFalse(MemberId.isValidMemberId(" ")); // spaces only
        //assertFalse(MemberId.isValidMemberId("91")); // less than 3 numbers
        assertFalse(MemberId.isValidMemberId("phone")); // non-numeric
        assertFalse(MemberId.isValidMemberId("9011p041")); // alphabets within digits
        assertFalse(MemberId.isValidMemberId("9312 1534")); // spaces within digits

        // valid noteId numbers
        assertTrue(MemberId.isValidMemberId("911")); // exactly 3 numbers
        assertTrue(MemberId.isValidMemberId("93121534"));
        assertTrue(MemberId.isValidMemberId("124293842033123")); // long phone numbers
    }
    //@Test
    //public void toString_test() {

    //    MemberId one = new MemberId();
    //    assertTrue(one.toString().equals("00001"));
    //    //assertEquals("[00001]", one.toString());

    //    MemberId two = new MemberId();
    //    assertTrue(two.toString().equals("00002"));
    //    //assertEquals("[00002]", two.toString());

    //    MemberId fifty = new MemberId("00050");
    //    assertTrue(fifty.toString().equals("00050"));
    //    //assertEquals("[00050]", fifty.toString());

    //    MemberId hundred = new MemberId(100);
    //    assertTrue(hundred.toString().equals("00100"));
    //    //assertEquals("[00100]", hundred.toString());

    //    MemberId twoThousand = new MemberId(2000);
    //    assertTrue(twoThousand.toString().equals("02000"));
    //    //assertEquals("[02000]", twoThousand.toString());

    //    MemberId fortyTwoThousand = new MemberId(42000);
    //    assertTrue(fortyTwoThousand.toString().equals("42000"));
    //    //assertEquals("[42000]", fortyTwoThousand.toString());

    //    MemberId fiveNines = new MemberId(99999);
    //    assertTrue(fiveNines.toString().equals("99999"));
    //    //assertEquals("[99999]", fiveNines.toString());

    //    MemberId countdown = new MemberId(54321);
    //    assertTrue(countdown.toString().equals("54321"));
    //    //assertEquals("[54321]", countdown.toString());

    //}

    @Test
    public void equals_test() {

        MemberId one = new MemberId("10");
        assertTrue(one.equals(new MemberId("10")));

        MemberId two = new MemberId("2");
        assertTrue(two.equals(new MemberId("2")));

        MemberId fifty = new MemberId("00050");
        assertTrue(fifty.equals(new MemberId("50")));

    }
}