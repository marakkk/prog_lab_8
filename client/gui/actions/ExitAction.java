package client.gui.actions;

import client.gui.GuiManager;
import client.utility.ClientManager;
import common.util.User;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public class ExitAction extends Action{
    public ExitAction(User user, ClientManager client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showOptionDialog(null,
                resourceBundle.getString("AreYouSure"),
                resourceBundle.getString("Confirmation"),
                JOptionPane.YES_NO_OPTION,
                QUESTION_MESSAGE,
                null,
                new String[]{resourceBundle.getString("Yes"), resourceBundle.getString("No")},
                resourceBundle.getString("No"));
        if(result == OK_OPTION){
            System.exit(0);
        }
    }
}
