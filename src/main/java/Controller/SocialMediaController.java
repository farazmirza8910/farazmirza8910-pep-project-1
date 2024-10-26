package Controller;

import Model.Message;
import Model.Account;
import Service.MessageService;
import Service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class SocialMediaController {
    private final MessageService messageService;
    private final AccountService accountService;

    public SocialMediaController() {
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.put("/messages/{message_id}", this::updateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        return app;
    }

    private void registerAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account createdAccount = accountService.registerAccount(account);
        if (createdAccount == null) {
            ctx.status(400);
        } else {
            ctx.status(200).json(createdAccount);
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.login(account);
        if (loggedInAccount != null) {
            ctx.status(200).json(loggedInAccount);
        } else {
            ctx.status(401);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage == null) {
            ctx.status(400);
        } else {
            ctx.status(200).json(createdMessage);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        ctx.status(200).json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.status(200).json(message);
        } else {
            ctx.status(200);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(messageId, message);
        if (updatedMessage == null) {
            ctx.status(400);
        } else {
            ctx.status(200).json(updatedMessage);
        }
    }

    private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        String newMessageText = message.getMessage_text();

        Message updatedMessage = messageService.updateMessageText(messageId, newMessageText);
        if (updatedMessage == null) {
            ctx.status(400);
        } else {
            ctx.status(200).json(updatedMessage);
        }
    }

    private void deleteMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        if (deletedMessage == null) {
            ctx.status(200);
        } else {
            ctx.status(200).json(deletedMessage);
        }
    }

    private void getMessagesByUserHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> userMessages = messageService.getMessagesByUser(accountId);
        ctx.status(200).json(userMessages);
    }
}