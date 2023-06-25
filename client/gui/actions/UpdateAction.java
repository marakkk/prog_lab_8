package client.gui.actions;

import client.gui.GuiManager;
import client.utility.ClientManager;
import common.exceptions.ConnectionErrorException;
import common.exceptions.NotDeclaredLimitsException;
import common.models.*;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import common.util.User;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public class UpdateAction extends Action{
    public UpdateAction(User user, ClientManager client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    private Integer getSelectedId() {
        Integer[] userOwnedIds = guiManager.getCollection().stream()
                .filter((s) -> s.getUserLogin().equals(user.toString()))
                .map(Ticket::getId)
                .toArray(Integer[]::new);

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);
        JLabel question = new JLabel(resourceBundle.getString("SelectId"));
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

    private Ticket getObject(Integer id) {
        return guiManager.getCollection().stream()
                .filter((s) -> s.getId().equals(id))
                .toList().get(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Integer id = this.getSelectedId();
        try {
            updateJOptionWorker(id);
        } catch (ConnectionErrorException | NotDeclaredLimitsException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void updateJOptionWorker(Integer id) throws ConnectionErrorException, NotDeclaredLimitsException {
        if(id == null) JOptionPane.showMessageDialog(null, resourceBundle.getString("NoObjects"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);

        if(!guiManager.getCollection().stream()
                .filter((i) -> i.getId().equals(id))
                .toList()
                .get(0)
                .getUserLogin()
                .equals(user.toString())){
            JOptionPane.showMessageDialog(null,
                    resourceBundle.getString("ObjectNotYour"),
                    resourceBundle.getString("Error"),
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);


        JLabel mainLabel = new JLabel(resourceBundle.getString("CreationDate"));
        JLabel nameLabel = new JLabel(resourceBundle.getString("Name"));
        JLabel cordXLabel = new JLabel(resourceBundle.getString("CoordX"));
        JLabel cordYLabel = new JLabel(resourceBundle.getString("CoordY"));
        JLabel priceLabel = new JLabel(resourceBundle.getString("Price"));
        JLabel discountLabel = new JLabel(resourceBundle.getString("Discount"));
        JLabel refundableLabel = new JLabel(resourceBundle.getString("Refundable"));
        JLabel ticketTypeLabel = new JLabel(resourceBundle.getString("TicketType"));
        JLabel personLabel = new JLabel(resourceBundle.getString("PersonCreation"));
        JLabel personBirthdayLabel = new JLabel(resourceBundle.getString("Birthday"));
        JLabel personHeightLabel = new JLabel(resourceBundle.getString("PersonHeight"));
        JLabel personWeightLabel = new JLabel(resourceBundle.getString("PersonWeight"));
        JLabel personNationalityLabel = new JLabel(resourceBundle.getString("PersonNationality"));
        JFormattedTextField nameField;
        JFormattedTextField cordXField;
        JFormattedTextField cordYField;
        JFormattedTextField priceField;
        JFormattedTextField discountField;
        JFormattedTextField refundableField;
        JComboBox ticketTypeField;
        JFormattedTextField personBirthdayField;
        JFormattedTextField personHeightField;
        JFormattedTextField personWeightField;
        JComboBox personNationalityField;
        // Action Listeners
        {
            nameField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    if (text.trim().isEmpty()) {
                        throw new ParseException(resourceBundle.getString("FiledNotEmpty"), 0);
                    }
                    return super.stringToValue(text);
                }
            });
            cordXField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Float num;
                    try {
                        num = Float.parseFloat(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + "float", 0);
                    }
                    if (num <= -206) throw new ParseException(resourceBundle.getString("NumberMustBe") + " " + resourceBundle.getString("More") + " -206", 0);
                    return num;
                }
            });
            cordYField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Float num;
                    try {
                        num = Float.parseFloat(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "float", 0);
                    }
                    if (num > 463) throw new ParseException(resourceBundle.getString("MaxNum") + " 463", 0);
                    return num;
                }
            });
            priceField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    int num;
                    try {
                        num = Integer.parseInt(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "int", 0);
                    }
                    if (num <= 0) throw new ParseException(resourceBundle.getString("NumberMustBe") + resourceBundle.getString("More") + " 0", 0);
                    return num;
                }
            });
            discountField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Long num;
                    try {
                        num = Long.parseLong(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "long", 0);
                    }
                    if (num <= 0) throw new ParseException(resourceBundle.getString("NumberMustBe") + resourceBundle.getString("More") + " 0", 0);
                    return num;
                }
            });
            refundableField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Boolean num;
                    try {
                        num = Boolean.parseBoolean(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "long", 0);
                    }
                    return num;
                }
            });
            ticketTypeField = new JComboBox<>(TicketType.values());
            personBirthdayField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    if (text.trim().isEmpty()) {
                        throw new ParseException(resourceBundle.getString("FiledNotEmpty"), 0);
                    }
                    return super.stringToValue(text);
                }
            });
            personHeightField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Float num;
                    try {
                        num = Float.parseFloat(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "double", 0);
                    }
                    return num;
                }
            });
            personWeightField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Integer num;
                    try {
                        num = Integer.parseInt(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "int", 0);
                    }
                    if (num <= 0) throw new ParseException(resourceBundle.getString("NumberMustBe") + resourceBundle.getString("More") + " 0", 0);
                    return num;
                }
            });

            personNationalityField = new JComboBox(Country.values());


        }
        // Default Values
        {
            Ticket ticket = this.getObject(id);
            nameField.setValue(ticket.getName());
            cordXField.setValue(ticket.getCoordinates().getX());
            cordYField.setValue(ticket.getCoordinates().getY());
            priceField.setValue(ticket.getPrice());
            discountField.setValue(ticket.getDiscount());
            refundableField.setValue(ticket.getRefundable());
            personBirthdayField.setValue(ticket.getPerson().getBirthday());
            personHeightField.setValue(ticket.getPerson().getHeight());

            personWeightField.setValue(ticket.getPerson().getWeight());

        }
        // Group Layout
        {
            layout.setVerticalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                            .addComponent(mainLabel))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(nameLabel)
                            .addComponent(nameField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(cordXLabel)
                            .addComponent(cordXField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(cordYLabel)
                            .addComponent(cordYField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(priceLabel)
                            .addComponent(priceField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(discountLabel)
                            .addComponent(discountField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(refundableLabel)
                            .addComponent(refundableField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(ticketTypeLabel)
                            .addComponent(ticketTypeField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personLabel))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personBirthdayLabel)
                            .addComponent(personBirthdayField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personHeightLabel)
                            .addComponent(personHeightField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personWeightLabel)
                            .addComponent(personWeightField))

                    .addGroup(layout.createParallelGroup()
                            .addComponent(personNationalityLabel)
                            .addComponent(personNationalityField))


            );
            layout.setHorizontalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                            .addComponent(mainLabel)
                            .addComponent(nameLabel)
                            .addComponent(cordXLabel)
                            .addComponent(cordYLabel)
                            .addComponent(priceLabel)
                            .addComponent(discountLabel)
                            .addComponent(refundableLabel)
                            .addComponent(ticketTypeLabel)
                            .addComponent(personLabel)
                            .addComponent(personBirthdayLabel)
                            .addComponent(personHeightLabel)
                            .addComponent(personWeightLabel)
                            .addComponent(personNationalityLabel)

                    )
                    .addGroup(layout.createParallelGroup()
                            .addComponent(nameField)
                            .addComponent(cordXField)
                            .addComponent(cordYField)
                            .addComponent(priceField)
                            .addComponent(discountField)
                            .addComponent(refundableField)
                            .addComponent(ticketTypeField)
                            .addComponent(personBirthdayLabel)
                            .addComponent(personHeightLabel)

                            .addComponent(personWeightField)
                            .addComponent(personNationalityField)

                    ));
        }
        int result = JOptionPane.showOptionDialog(null, panel, resourceBundle.getString("Update"), JOptionPane.YES_OPTION,
                QUESTION_MESSAGE, null, new String[]{resourceBundle.getString("Update")}, resourceBundle.getString("Update"));
        if(result == OK_OPTION){
            Ticket ticket = new Ticket(
                    nameField.getText(),
                    new Coordinates(
                            Float.parseFloat(cordXField.getText()),
                            Float.parseFloat(cordYField.getText())
                    ),
                    LocalDateTime.now(),
                    Integer.parseInt(priceField.getText()),
                    Long.parseLong(discountField.getText()),
                    Boolean.parseBoolean(refundableField.getText()),
                    (TicketType) ticketTypeField.getSelectedItem(),
                    new Person(
                            LocalDate.parse(personBirthdayLabel.getText()),
                            Float.parseFloat(personHeightLabel.getText()),

                            Float.parseFloat(personWeightField.getText()),
                            (Country) personNationalityField.getSelectedItem()

                    ),
                    user.username()
            );
            if(!ticket.validate()) {
                JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotValid"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            ServerResponse response = client.processRequestToServer(new ClientRequest("update", id.toString(), ticket, user, GuiManager.getLocale()));
            if(response.getResponseCode() == ResponseCode.OK) JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectUpdated"), resourceBundle.getString("Ok"), JOptionPane.PLAIN_MESSAGE);
            else JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotChanged"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
