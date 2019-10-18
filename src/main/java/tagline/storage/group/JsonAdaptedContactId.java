package tagline.storage.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import tagline.commons.exceptions.IllegalValueException;
import tagline.model.contact.ContactId;

/**
 * Jackson-friendly version of {@link ContactId}.
 */
class JsonAdaptedContactId {

    private final String idValue;

    /**
     * Constructs a {@code JsonAdaptedContactId} with the given {@code idValue}.
     */
    @JsonCreator
    public JsonAdaptedContactId(String idValue) {
        this.idValue = idValue;
    }

    /**
     * Converts a given {@code ContactId} into this class for Jackson use.
     */
    public JsonAdaptedContactId(ContactId source) {
        idValue = String.valueOf(source.toInteger());
    }

    @JsonValue
    public String getContactIdName() {
        return idValue;
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code ContactId} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public ContactId toModelType() throws IllegalValueException {
        if (!ContactId.isValidId(idValue)) {
            throw new IllegalValueException(ContactId.MESSAGE_CONSTRAINTS);
        }
        return new ContactId(idValue);
    }

}
