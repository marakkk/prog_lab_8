package client.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FilterListener implements ActionListener {
    private int row;
    private StreamTableModel tableModel;
    private FilterTicket filterTicket;
    private JTable table;

    public FilterListener(int row, StreamTableModel tableModel, JTable table,  FilterTicket filterTicket){
        this.row = row;
        this.tableModel = tableModel;
        this.filterTicket = filterTicket;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JList<?> jList = new JList<>(tableModel.getAllData().stream()
                .map(o -> tableModel.getValueAtRow(o, row))
                .distinct()
                .toArray());
        JOptionPane.showMessageDialog(null, jList);
        if(jList.getSelectedValuesList().isEmpty()) return;
        filterTicket.parsePredicate(row, jList.getSelectedValuesList());
        tableModel.performFiltration();
        table.repaint();
    }
}
