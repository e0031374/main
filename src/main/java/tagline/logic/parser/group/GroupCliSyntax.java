package tagline.logic.parser.group;

import tagline.logic.parser.Prefix;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class GroupCliSyntax {

    /* Prefix definitions */
    public static final String GROUP_COMMAND_WORD = "group";
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_CONTACTID = new Prefix("--i");

}
