package common.util;

import common.models.Ticket;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

/**
 * Class for get request value.
 */
public class ClientRequest implements Serializable {
    private String commandName;
    private String commandArguments = "";
    private Ticket objectArgument = null;
    private User user;
    private Locale locale;

    public ClientRequest(String commandName, String commandArguments, Ticket objectArgument, User user, Locale locale) {
        this.commandName = commandName.trim();
        this.commandArguments = commandArguments;
        this.objectArgument = objectArgument;
        this.user = user;
        this.locale = locale;
    }
    public ClientRequest(String commandName, String commandStringArgument, User user, Locale locale) {
        this(commandName, commandStringArgument, null, user, locale);
    }

    public ClientRequest(User user, Locale locale) {
        this("", "", user, locale);
    }
    public String getCommandName() {
        return commandName;
    }

    public String getCommandArguments() {
        return commandArguments;
    }

    public Ticket getObjectArgument() {
        return objectArgument;
    }

    public User getUser(){
        return user;
    }
    public boolean isEmpty(){
        return commandName.isEmpty() && commandArguments.isEmpty() && objectArgument==null;
    }
    public Locale getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return "Request[" + commandName + ',' + user +
                (commandArguments.isEmpty()
                        ? ""
                        : "," + commandArguments ) +
                ((objectArgument == null)
                        ? "]"
                        : "," + objectArgument + "]");
    }
}
