package common.util;

import java.io.Serializable;

public record User(String username, String password) implements Serializable {
    @Override
    public String toString() {
        return username + ":" + password;
    }


}
