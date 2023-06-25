package client.gui.actions;

import client.gui.GuiManager;
import client.utility.ClientManager;
import common.exceptions.ConnectionErrorException;
import common.exceptions.NotDeclaredLimitsException;
import common.util.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Locale;

public class ChangeLanguageAction extends Action{
    public ChangeLanguageAction(User user, ClientManager client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox languages = new JComboBox(new Object[]{
                new Locale("ru", "RU"),
                new Locale("be", "BY"),
                new Locale("es", "GT"),
                new Locale("pl", "PL")
        });
        JOptionPane.showMessageDialog(null,
                languages,
                "Choose language",
                JOptionPane.INFORMATION_MESSAGE);
        try {
            guiManager.setLocale((Locale) languages.getSelectedItem());
        } catch (ConnectionErrorException | NotDeclaredLimitsException ex) {
            throw new RuntimeException(ex);
        }
    }
}
