package client.gui.actions;

import client.gui.GuiManager;
import client.utility.ClientManager;
import common.exceptions.ConnectionErrorException;
import common.exceptions.NotDeclaredLimitsException;
import common.models.Ticket;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import common.util.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RemoveAction extends Action{
    public RemoveAction(User user, ClientManager client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    private Integer getSelectedId() {
        Integer[] userOwnedIds = guiManager.getCollection().stream()
                .filter((s) -> s.getUserLogin().equals(user.toString()))
                .map(Ticket::getId)
                .toArray(Integer[]::new);

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);
        JLabel question = new JLabel(resourceBundle.getString("SelectIdForDelete"));
        JLabel idLabel = new JLabel(resourceBundle.getString("SelectId"));
        JComboBox idField = new JComboBox(userOwnedIds);

        layout.addLayoutComponent(question, BorderLayout.NORTH);
        layout.addLayoutComponent(idLabel, BorderLayout.WEST);
        layout.addLayoutComponent(idField, BorderLayout.EAST);

        JOptionPane.showMessageDialog(null,
                idField,
                resourceBundle.getString("Update"),
                JOptionPane.PLAIN_MESSAGE);
        return (Integer) idField.getSelectedItem();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Integer id = this.getSelectedId();
        if(id == null) JOptionPane.showMessageDialog(null, resourceBundle.getString("NoObjects"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        ServerResponse response = null;
        try {
            response = client.processRequestToServer(new ClientRequest("remove_by_id", id.toString(), user, GuiManager.getLocale()));
        } catch (ConnectionErrorException | NotDeclaredLimitsException ex) {
            throw new RuntimeException(ex);
        }
        if(response.getResponseCode() == ResponseCode.OK) {
            JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectDeleted"), resourceBundle.getString("Ok"), JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotDeleted"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
