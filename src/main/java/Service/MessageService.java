package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null; // Invalid message text
        }
        if (message.getPosted_by() <= 0 || messageDAO.getMessageById(message.getPosted_by()) == null) {
            return null; // Invalid user ID
        }
        return messageDAO.insertMessage(message);
    }

    public Message updateMessage(int messageId, Message updatedMessage) {
        if (updatedMessage.getMessage_text() == null || updatedMessage.getMessage_text().isBlank() || updatedMessage.getMessage_text().length() > 255) {
            return null; // Invalid message text
        }
        Message existingMessage = messageDAO.getMessageById(messageId);
        if (existingMessage != null) {
            existingMessage.setMessage_text(updatedMessage.getMessage_text());
            existingMessage.setPosted_by(updatedMessage.getPosted_by());
            return messageDAO.updateMessage(existingMessage);
        }
        return null; // Message not found
    }

    public Message updateMessageText(int messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return null;
        }
        Message existingMessage = messageDAO.getMessageById(messageId);
        if (existingMessage != null) {
            existingMessage.setMessage_text(newMessageText);
            return messageDAO.updateMessage(existingMessage);
        }
        return null;
    }

    public Message deleteMessage(int messageId) {
        Message deletedMessage = messageDAO.getMessageById(messageId);
        if (deletedMessage != null) {
            messageDAO.deleteMessage(messageId);
            return deletedMessage;
        }
        return null;
    }

    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }
}