package client.gui.actions;

import client.gui.GuiManager;
import client.utility.ClientManager;
import common.util.User;

import java.awt.event.ActionEvent;

public class GroupCountingAction extends Action{
    public GroupCountingAction(User user, ClientManager client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
