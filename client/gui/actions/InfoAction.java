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

public class InfoAction extends Action{
    public InfoAction(User user, ClientManager client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ServerResponse response = null;
        try {
            response = client.processRequestToServer(new ClientRequest("info", "", user, GuiManager.getLocale()));
        } catch (ConnectionErrorException | NotDeclaredLimitsException ex) {
            throw new RuntimeException(ex);
        }
        if(response.getResponseCode() == ResponseCode.OK) JOptionPane.showMessageDialog(null, response.getMessage(), resourceBundle.getString("Result"), JOptionPane.PLAIN_MESSAGE);
        else JOptionPane.showMessageDialog(null, resourceBundle.getString("NoResult"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
    }
}
