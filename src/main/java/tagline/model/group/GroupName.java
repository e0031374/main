package tagline.model.group;

import static java.util.Objects.requireNonNull;
import static tagline.commons.util.AppUtil.checkArgument;

/**
 * Represents a Group's name in the group book.
 * Guarantees: immutable; is valid as declared in {@link #isValidGroupName(String)}
 */
public class GroupName {

    public static final String MESSAGE_CONSTRAINTS =
            "GroupNames should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * Non-empty string
     */
    //https://stackoverflow.com/questions/4448829/regular-expression-for-not-empty
    public static final String VALIDATION_REGEX = ".+";

    public final String value;

    /**
     * Constructs a {@code GroupName}.
     *
     * @param name A valid name.
     */
    public GroupName(String name) {
        requireNonNull(name);
        checkArgument(isValidGroupName(name), MESSAGE_CONSTRAINTS);
        value = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidGroupName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns true if a given GroupName has the same value ignoring case.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof GroupName // instanceof handles nulls
                && value.equalsIgnoreCase(((GroupName) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
