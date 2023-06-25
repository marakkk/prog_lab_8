package client.commandLine.forms;

import common.exceptions.InvalidForm;

public abstract class Form<T> {
    public abstract T build() throws InvalidForm;

}
