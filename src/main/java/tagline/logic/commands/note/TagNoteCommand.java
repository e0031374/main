package tagline.logic.commands.note;

import static java.util.Objects.requireNonNull;
import static tagline.logic.parser.note.NoteCliSyntax.PREFIX_TAG;
import static tagline.model.note.NoteModel.PREDICATE_SHOW_ALL_NOTES;

import java.util.List;
import java.util.Optional;

import tagline.commons.core.Messages;
import tagline.logic.commands.CommandResult;
import tagline.logic.commands.exceptions.CommandException;
import tagline.model.Model;
import tagline.model.note.Note;
import tagline.model.note.NoteId;
import tagline.model.tag.Tag;

/**
 * Tags a note with several tags.
 */
public class TagNoteCommand extends NoteCommand {
    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_TAG_NOTE_SUCCESS = "Tagged Note: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Tags a note identified "
            + "by the note index number with several tags."
            + "Parameters: NOTE_ID (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG ]+ \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + " #tagline ";

    public static final String TAGS_HAVE_BEEN_TAGGED = "Some tags in the parameter have been tagged to the note";

    private final NoteId noteId;
    private final List<Tag> tags;

    public TagNoteCommand(NoteId noteId, List<Tag> tags) {
        requireNonNull(noteId);
        requireNonNull(tags);

        assert (tags.size() > 0) : "At least one tag to be provided.";

        this.noteId = noteId;
        this.tags = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check for invalid note id
        Optional<Note> noteFound = model.findNote(noteId);

        if (noteFound.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_INVALID_NOTE_INDEX);
        }

        // A round of validation on all tags.
        var existingTags = noteFound.get().getTags();
        for (Tag tag : tags) {
            if (!tag.isValidInModel(model)) {
                throw new CommandException(Messages.NON_EXISTING_TAG);
            }

            if (existingTags.contains(tag)) {
                throw new CommandException(TAGS_HAVE_BEEN_TAGGED);
            }
        }

        for (Tag tag : tags) {
            Tag registeredTag = model.createOrFindTag(tag);

            model.tagNote(noteId, registeredTag);
        }

        model.refreshFilteredNoteList();
        model.updateFilteredNoteList(PREDICATE_SHOW_ALL_NOTES);

        Note targetNote = noteFound.get();
        return new CommandResult(String.format(MESSAGE_TAG_NOTE_SUCCESS, targetNote), CommandResult.ViewType.NOTE);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagNoteCommand)) {
            return false;
        }

        // state check
        TagNoteCommand otherCommand = (TagNoteCommand) other;
        return noteId.equals(otherCommand.noteId) && tags.equals(otherCommand.tags);
    }
}
