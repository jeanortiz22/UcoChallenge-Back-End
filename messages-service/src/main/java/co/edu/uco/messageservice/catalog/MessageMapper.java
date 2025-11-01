package co.edu.uco.messageservice.catalog;

import co.edu.uco.messageservice.catalog.persistence.MessageDocument;

final class MessageMapper {

    private MessageMapper() {
    }

    static Message toDomain(MessageDocument document) {
        if (document == null) {
            return null;
        }
        return new Message(document.getKey(), document.getTemplate());
    }

    static MessageDocument toDocument(Message message) {
        if (message == null) {
            return null;
        }
        return new MessageDocument(message.getKey(), message.getTemplate());
    }
}