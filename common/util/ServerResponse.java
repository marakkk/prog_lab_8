package common.util;

import common.models.Ticket;

import java.io.Serializable;
import java.util.*;

/**
 * Class for get response value.
 */
public class ServerResponse implements Serializable {
    private String message = "";
    private final ResponseCode responseCode;
    private Collection<Ticket> collection;

    public ServerResponse(String message, ResponseCode responseCode, Collection<Ticket> collection) {
        this.message = message;
        this.responseCode = responseCode;
        this.collection = collection.stream()
                .sorted(Comparator.comparing(Ticket::getId))
                .toList();
    }
    public ServerResponse(String message, ResponseCode responseCode) {
        this.message = message.trim();
        this.responseCode = responseCode;

    }
    public Collection<Ticket> getCollection() {
        return collection;
    }
    public String getMessage() {
        return message;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    @Override
    public String toString() {
        return "ServerResponse{"
                + " message='" + message + '\''
                + ", executeCode=" + responseCode+
                ", collection=" + collection
                + '}';
    }
}
