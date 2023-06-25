package client.gui.actions;

import client.gui.GuiManager;
import client.utility.ClientManager;
import common.exceptions.ConnectionErrorException;
import common.exceptions.NotDeclaredLimitsException;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import common.util.User;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public class ClearAction extends Action{
    public ClearAction(User user, ClientManager client, GuiManager guiManager) {
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
            ServerResponse response = null;
            try {
                response = client.processRequestToServer(new ClientRequest("clear", "", user, GuiManager.getLocale()));
            } catch (ConnectionErrorException | NotDeclaredLimitsException ex) {
                throw new RuntimeException(ex);
            }
            if(response.getResponseCode() == ResponseCode.OK) JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectsDeleted"), resourceBundle.getString("Result"), JOptionPane.PLAIN_MESSAGE);
            else JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotValid"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
