package tagline.logic.parser.contact;

import tagline.logic.parser.Prefix;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class ContactCliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("--n ");
    public static final Prefix PREFIX_PHONE = new Prefix("--p ");
    public static final Prefix PREFIX_EMAIL = new Prefix("--e ");
    public static final Prefix PREFIX_ADDRESS = new Prefix("--a ");
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("--d ");

}
