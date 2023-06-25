package client.gui;

import com.formdev.flatlaf.FlatDarculaLaf;

import client.gui.actions.*;
import client.utility.ClientManager;
import common.exceptions.ConnectionErrorException;
import common.exceptions.NotDeclaredLimitsException;
import common.models.Ticket;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import common.util.User;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.*;

import static javax.swing.JOptionPane.*;

public class GuiManager implements Runnable {
    private final ClientManager client;
    private static Locale locale = new Locale("ru");
    private final ClassLoader classLoader = this.getClass().getClassLoader();
    private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("gui", GuiManager.getLocale());
    private final JFrame frame;
    private Panel panel;
    private JTable table = null;
    private StreamTableModel tableModel = null;
    private CartesianPanel cartesianPanel = null;
    private ArrayList<Ticket> tableData = null;
    private ArrayList<Ticket> collection = null;
    private FilterTicket filterTicket = new FilterTicket();
    private Map<JButton, String> buttonsToChangeLocale = new LinkedHashMap<>();
    private User user;

    private final static Color RED_WARNING = Color.decode("#FF4040");
    private final static Color GREEN_OK = Color.decode("#00BD39");

    String[] columnNames = {
            "id",
            "ticket_name",
            "x",
            "y",
            "creation_date",
            "price",
            "discount",
            "refundable",
            "ticket_type",
            "person_birthday",
            "person_height",
            "person_weight",
            "person_nationality",
            "user_id"
    };

    public GuiManager(ClientManager client){
        this.client = client;

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.frame = new JFrame(resourceBundle.getString("LabWork8"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(this::run);


    }

    public GuiManager(ClientManager client, User user){
        this.client = client;
        this.user = user;
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.frame = new JFrame(resourceBundle.getString("LabWork8"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(this::run);



    }
    public void run(){
        panel = new Panel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        if(user == null) {
            try {
                this.loginAuth();
            } catch (ConnectionErrorException | NotDeclaredLimitsException e) {
                throw new RuntimeException(e);
            }
        }

        this.tableData = this.getTableDataTicket();

        this.tableModel = new StreamTableModel(columnNames, tableData.size(), filterTicket);
        System.out.println(tableData);
        this.tableModel.setDataVector(tableData, columnNames);
        this.table = new JTable(tableModel);
        frame.setJMenuBar(this.createMenuBar());

        JButton tableExecute = new JButton(resourceBundle.getString("Table"));
        JButton cartesianExecute = new JButton(resourceBundle.getString("Coordinates"));


        /**new Timer(500, (i) ->{
            try {
                this.timerTrigger();
            } catch (ConnectionErrorException | NotDeclaredLimitsException e) {
                throw new RuntimeException(e);
            }
        }).start();**/

        // Выбрать столбец для сортировки
        table.getTableHeader().setReorderingAllowed(false);
        table.setDragEnabled(false);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int column = table.columnAtPoint(point);
                tableModel.performSorting(column);
                table.repaint();
            }
        });
        // Выбрать строку для изменения
        this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Integer id = tableModel.getRow(table.getSelectedRow()).getId();
                try {
                    new UpdateAction(user, client, GuiManager.this).updateJOptionWorker(id);
                } catch (ConnectionErrorException | NotDeclaredLimitsException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        JScrollPane tablePane = new JScrollPane(table);
        try {
            this.cartesianPanel = new CartesianPanel(client, user, this);
        } catch (ConnectionErrorException | NotDeclaredLimitsException e) {
            throw new RuntimeException(e);
        }
        JPanel cardPanel = new JPanel();
        ImageIcon userIcon = new ImageIcon(new ImageIcon(classLoader.getResource("icons/user.png"))
                .getImage()
                .getScaledInstance(25, 25, Image.SCALE_AREA_AVERAGING));
        JLabel userLabel = new JLabel(user.toString());
        userLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        userLabel.setIcon(userIcon);

        CardLayout cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        cardPanel.add(tablePane, "Table");
        cardPanel.add(cartesianPanel, "Cartesian");

        tableExecute.addActionListener((actionEvent) -> {
            cardLayout.show(cardPanel, "Table");
        });
        cartesianExecute.addActionListener((actionEvent) -> {
            this.cartesianPanel.reanimate();
            cardLayout.show(cardPanel, "Cartesian");
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(cardPanel)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(tableExecute)
                                .addComponent(cartesianExecute)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(userLabel)
                                .addGap(5))));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(cardPanel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(tableExecute)
                        .addComponent(cartesianExecute)
                        .addComponent(userLabel)
                        .addGap(5)));
        frame.add(panel);
        frame.setVisible(true);
    }

    public ArrayList<Ticket> getTableDataTicket(){
        ServerResponse response = null;
        try {
            response = client.processRequestToServer(new ClientRequest("show", "", user, GuiManager.getLocale()));
        } catch (ConnectionErrorException | NotDeclaredLimitsException e) {
            System.out.println(e.getMessage());
        }
        assert response != null;
        if(response.getResponseCode() != ResponseCode.OK) return null;
        this.collection = new ArrayList<>(response.getCollection());
        //System.out.println(response);
        return new ArrayList<>(response.getCollection());
    }

    private JMenuBar createMenuBar(){
        int iconSize = 40;

        JMenuBar menuBar = new JMenuBar();
        JMenu actions = new JMenu(resourceBundle.getString("Actions"));
        JMenuItem add = new JMenuItem(resourceBundle.getString("Add"));
        JMenuItem addIfMin = new JMenuItem(resourceBundle.getString("AddIfMin"));
        JMenuItem clear = new JMenuItem(resourceBundle.getString("Clear"));
        JMenuItem countByCreationDate = new JMenuItem(resourceBundle.getString("CountByCreationDate"));
        JMenuItem averageDiscount = new JMenuItem(resourceBundle.getString("AverageDiscount"));
        JMenuItem executeScript = new JMenuItem(resourceBundle.getString("ExecuteScript"));
        JMenuItem exit = new JMenuItem(resourceBundle.getString("Exit"));
        JMenuItem info = new JMenuItem(resourceBundle.getString("Info"));
        JMenuItem removeGreater = new JMenuItem(resourceBundle.getString("RemoveGreater"));
        JMenuItem update = new JMenuItem(resourceBundle.getString("Update"));
        JMenuItem remove = new JMenuItem(resourceBundle.getString("Remove"));
        JMenuItem language = new JMenuItem(resourceBundle.getString("Language"));

        add.addActionListener(new AddAction(user, client, this));
        update.addActionListener(new UpdateAction(user, client, this));
        remove.addActionListener(new RemoveAction(user, client, this));
        addIfMin.addActionListener(new AddIfMinAction(user, client, this));
        clear.addActionListener(new ClearAction(user, client, this));
        countByCreationDate.addActionListener(new GroupCountingAction(user, client, this));
        executeScript.addActionListener(new ExecuteScriptAction(user, client, this));
        averageDiscount.addActionListener(new AverageOfDiscountAction(user, client, this));
        exit.addActionListener(new ExitAction(user, client, this));
        info.addActionListener(new InfoAction(user, client, this));
        removeGreater.addActionListener(new RemoveGreaterAction(user, client, this));
        language.addActionListener(new ChangeLanguageAction(user, client, this));

        add.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/add.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        addIfMin.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/add_if_max.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        update.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/update.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        remove.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/remove.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        clear.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/clear.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        averageDiscount.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/count_by_average_mark.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        countByCreationDate.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/count_less_than_expelled.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        exit.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/exit.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        info.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/info.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));

        removeGreater.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/remove_greater.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        language.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/language.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        executeScript.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/execute.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));


        actions.add(add);
        actions.add(addIfMin);
        actions.addSeparator();
        actions.add(update);
        actions.addSeparator();
        actions.add(remove);
        actions.add(removeGreater);
        actions.add(clear);
        actions.addSeparator();
        actions.add(countByCreationDate);
        actions.add(averageDiscount);
        actions.add(info);
        actions.addSeparator();
        actions.add(language);
        actions.addSeparator();
        actions.add(executeScript);
        actions.add(exit);

        menuBar.add(actions);

        JMenuItem clearFilters = new JMenuItem(resourceBundle.getString("ClearFilter"));
        JMenuItem idFilter = new JMenuItem("id");
        JMenuItem ticketNameFilter = new JMenuItem("ticket_name");
        JMenuItem cordXFilter = new JMenuItem("x");
        JMenuItem cordYFilter = new JMenuItem("y");

        JMenuItem creationDateFilter = new JMenuItem("creation_date");
        JMenuItem priceFilter = new JMenuItem("price");
        JMenuItem discountFilter = new JMenuItem("discount");
        JMenuItem refundableFilter = new JMenuItem("refundable");
        JMenuItem ticketTypeFilter = new JMenuItem("ticket_type");
        JMenuItem personBirthdayFilter = new JMenuItem("person_birthday");
        JMenuItem personHeightFilter = new JMenuItem("person_height");
        JMenuItem personWeightFilter = new JMenuItem("person_weight");
        JMenuItem personNationalityFilter = new JMenuItem("nationality");
        JMenuItem ownerLoginFilter = new JMenuItem("user_id");

        clearFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTicket.clearPredicates();
                tableModel.performFiltration();
                table.repaint();
            }
        });
        idFilter.addActionListener(new FilterListener(0, tableModel, table, filterTicket));
        ticketNameFilter.addActionListener(new FilterListener(1, tableModel, table, filterTicket));
        cordXFilter.addActionListener(new FilterListener(2, tableModel, table, filterTicket));
        cordYFilter.addActionListener(new FilterListener(3, tableModel, table, filterTicket));

        creationDateFilter.addActionListener(new FilterListener(4, tableModel, table, filterTicket));
        priceFilter.addActionListener(new FilterListener(5, tableModel, table, filterTicket));
        discountFilter.addActionListener(new FilterListener(6, tableModel, table, filterTicket));
        refundableFilter.addActionListener(new FilterListener(7, tableModel, table, filterTicket));
        ticketTypeFilter.addActionListener(new FilterListener(8, tableModel, table, filterTicket));
        personBirthdayFilter.addActionListener(new FilterListener(9, tableModel, table, filterTicket));
        personHeightFilter.addActionListener(new FilterListener(10, tableModel, table, filterTicket));
        personWeightFilter.addActionListener(new FilterListener(11, tableModel, table, filterTicket));
        personNationalityFilter.addActionListener(new FilterListener(12, tableModel, table, filterTicket));
        ownerLoginFilter.addActionListener(new FilterListener(13, tableModel, table, filterTicket));

        JMenu filters = new JMenu(resourceBundle.getString("Filters"));



        filters.add(clearFilters);
        filters.add(idFilter);
        filters.add(ticketNameFilter);
        filters.add(cordXFilter);
        filters.add(cordYFilter);

        filters.add(creationDateFilter);
        filters.add(priceFilter);
        filters.add(discountFilter);
        filters.add(refundableFilter);
        filters.add(ticketTypeFilter);
        filters.add(personBirthdayFilter);
        filters.add(personHeightFilter);
        filters.add(personWeightFilter);
        filters.add(personNationalityFilter);
        filters.add(ownerLoginFilter);

        menuBar.add(filters);
        return menuBar;
    }

    public void loginAuth() throws ConnectionErrorException, NotDeclaredLimitsException {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        JLabel loginTextLabel = new JLabel(resourceBundle.getString("WriteLogin"));
        JTextField loginField = new JTextField();
        JLabel passwordTextLabel = new JLabel(resourceBundle.getString("EnterPass"));
        JPasswordField passwordField = new JPasswordField();
        JLabel errorLabel = new JLabel("");
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(loginTextLabel)
                        .addComponent(passwordTextLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(loginField)
                        .addComponent(passwordField)
                        .addComponent(errorLabel)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(loginTextLabel)
                        .addComponent(loginField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordTextLabel)
                        .addComponent(passwordField))
                .addComponent(errorLabel));
        while(true) {
            int result = JOptionPane.showOptionDialog(null, panel, resourceBundle.getString("Login"), JOptionPane.YES_NO_OPTION,
                    QUESTION_MESSAGE, null, new String[]{resourceBundle.getString("Login"), resourceBundle.getString("Register")}, resourceBundle.getString("Login"));
            if (result == OK_OPTION) {
                if (!checkFields(loginField, passwordField, errorLabel)) continue;
                ServerResponse response = (ServerResponse) client.processRequestToServer(
                        new ClientRequest(
                                "ping",
                                "",
                                new User(loginField.getText(), String.valueOf(passwordField.getPassword())),
                                GuiManager.getLocale()));
                if (response.getResponseCode() == ResponseCode.OK) {
                    errorLabel.setText(resourceBundle.getString("LoginAcc"));
                    errorLabel.setForeground(GREEN_OK);
                    this.user = new User(loginField.getText(), String.valueOf(passwordField.getPassword()));
                    return;
                } else {
                    errorLabel.setText(resourceBundle.getString("LoginNotAcc"));
                    errorLabel.setForeground(RED_WARNING);
                }
            } else if (result == NO_OPTION){
                if (!checkFields(loginField, passwordField, errorLabel)) continue;
                ServerResponse response = (ServerResponse) client.processRequestToServer(
                        new ClientRequest(
                                "register",
                                "",
                                new User(loginField.getText(), String.valueOf(passwordField.getPassword())),
                                GuiManager.getLocale()));
                if (response.getResponseCode() == ResponseCode.OK) {
                    errorLabel.setText(resourceBundle.getString("RegAcc"));
                    errorLabel.setForeground(GREEN_OK);
                    this.user = new User(loginField.getText(), String.valueOf(passwordField.getPassword()));
                    return;
                } else {
                    errorLabel.setText(resourceBundle.getString("RegNotAcc"));
                    errorLabel.setForeground(RED_WARNING);
                }
            } else if (result == CLOSED_OPTION) {
                System.exit(0);
            }
        }
    }

    private boolean checkFields(JTextField loginField, JPasswordField passwordField, JLabel errorLabel){
        if(loginField.getText().isEmpty()) {
            errorLabel.setText(resourceBundle.getString("LoginNotNull"));
            errorLabel.setForeground(RED_WARNING);
            return false;
        } else if(String.valueOf(passwordField.getPassword()).isEmpty()){
            errorLabel.setText(resourceBundle.getString("PassNotNull"));
            errorLabel.setForeground(RED_WARNING);
            return false;
        }
        return true;
    }

    public Collection<Ticket> getCollection() {
        return collection;
    }

    public static Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) throws ConnectionErrorException, NotDeclaredLimitsException {
        GuiManager.locale = locale;
        Locale.setDefault(locale);
        ResourceBundle.clearCache();
        resourceBundle = ResourceBundle.getBundle("gui", locale);
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
        this.buttonsToChangeLocale.forEach((i, j) -> i.setText(resourceBundle.getString(j)));
        this.tableData = this.getTableDataTicket();
        this.tableModel.setDataVector(this.tableData, columnNames);
        this.tableModel.fireTableDataChanged();
        this.frame.remove(panel);
        this.frame.setTitle(resourceBundle.getString("LabWork8"));
        this.run();
    }

    public void repaintNoAnimation() throws ConnectionErrorException, NotDeclaredLimitsException {
        ArrayList<Ticket> newTableData = this.getTableDataTicket();
        this.tableData = newTableData;
        this.tableModel.setDataVector(this.tableData, columnNames);
        this.tableModel.performFiltration();
        this.table.repaint();
        this.tableModel.fireTableDataChanged();
//        this.cartesianPanel.updateUserColors();
        this.cartesianPanel.reanimate(100);
    }

    public void timerTrigger() throws ConnectionErrorException, NotDeclaredLimitsException {
        ArrayList<Ticket> newTableData = this.getTableDataTicket();
        if(!(this.tableData.equals(newTableData))) {
            this.tableData = newTableData;
            this.tableModel.setDataVector(this.tableData, columnNames);
            this.tableModel.performFiltration();
            this.table.repaint();
            this.tableModel.fireTableDataChanged();
            this.cartesianPanel.updateUserColors();
            this.cartesianPanel.reanimate();
        }
    }
}
