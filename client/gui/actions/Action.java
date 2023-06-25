package client.gui.actions;

import client.gui.GuiManager;
import client.utility.ClientManager;
import common.util.User;

import javax.swing.*;
import java.util.ResourceBundle;

public abstract class Action extends AbstractAction {
    protected ResourceBundle resourceBundle;

    protected User user;
    protected ClientManager client;
    protected GuiManager guiManager;

    public Action(User user, ClientManager client, GuiManager guiManager) {
        this.user = user;
        this.client = client;
        this.guiManager = guiManager;
        this.resourceBundle = ResourceBundle.getBundle("gui", guiManager.getLocale());
    }
}
