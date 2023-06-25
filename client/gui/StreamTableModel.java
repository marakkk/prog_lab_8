package client.gui;

import client.gui.FilterTicket;
import client.gui.GuiManager;
import common.models.Ticket;


import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTableModel extends AbstractTableModel {
    private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, GuiManager.getLocale());
    private String[] columnNames;
    private ArrayList<Ticket> allData;
    private ArrayList<Ticket> filteredData;
    private Integer sortingColumn = 0;
    private boolean reversed = false;
    private FilterTicket filterWorker;

    public StreamTableModel(String[] columnNames, int rowCount, FilterTicket filterWorker) {
        this.columnNames = columnNames;
        this.filterWorker = filterWorker;
    }

    public void setDataVector(ArrayList<Ticket> data, String[] columnNames){
        this.allData = data;
        this.columnNames = columnNames;
        this.filteredData = actFiltration(data);
    }

    public void performSorting(int column){
        this.reversed = sortingColumn == column && !reversed;
        this.sortingColumn = column;
        this.filteredData = actFiltration(this.allData);
    }

    public void performFiltration(){
        this.filteredData = actFiltration(this.allData);
    }

    @Override
    public int getRowCount() {
        return this.filteredData.size() - 1;
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.getValueAtRow(this.filteredData.get(rowIndex), columnIndex);
    }

    private ArrayList<Ticket> actFiltration(ArrayList<Ticket> allData){
        if(Objects.isNull(this.sortingColumn)) return allData;

        ArrayList<Ticket> sorted = new ArrayList<>(allData.stream()
                .sorted(Comparator.comparing(o -> this.sortingColumn < 0
                        ? (float)o.getId()
                        : this.getSortedFiledFloat(o, this.sortingColumn)))
                .filter(filterWorker.getPredicate())
                .toList());
        if(reversed) Collections.reverse(sorted);
        return sorted;
    }

    public Object getValueAtRow(Ticket o, int row){
        return switch (row){
            case 0 -> o.getId();
            case 1 -> o.getName();
            case 2 -> o.getCoordinates().getX();
            case 3 -> o.getCoordinates().getY();
            case 4 -> o.getCreationDate();
            case 5 -> o.getPrice();
            case 6 -> o.getDiscount();
            case 7 -> o.getRefundable();
            case 8 -> o.getType();
            case 9 -> o.getPerson().getBirthday();
            case 10 -> o.getPerson().getHeight();
            case 11 -> o.getPerson().getWeight();
            case 12 -> o.getPerson().getNationality();
            case 13 -> o.getUserLogin();
            default -> throw new IllegalStateException("Unexpected value: " + row);
        };
    }

    public float getSortedFiledFloat(Ticket o, int column){
        return switch (column){
            case 0 -> o.getId();
            case 1 -> o.getName().length();
            case 2 -> o.getCoordinates().getX();
            case 3 -> o.getCoordinates().getY();
            case 4 -> o.getCreationDate().toEpochSecond(ZoneOffset.UTC);
            case 5 -> o.getPrice();
            case 6 -> o.getDiscount();
            case 7 -> o.getRefundable()? 1.0f : 0.0f;
            case 8 -> o.getType().ordinal();
            case 9 -> o.getPerson().getBirthday().getDayOfMonth();
            case 10 -> o.getPerson().getHeight();
            case 11 -> o.getPerson().getWeight();
            case 12 -> o.getPerson().getNationality().ordinal();
            case 13 -> o.getUserLogin().length();
            default -> throw new IllegalStateException("Unexpected value: " + column);
        };
    }

    public Ticket getRow(int row) {
        try {
            return this.filteredData.get(row);
        } catch (IndexOutOfBoundsException e) {
            return this.filteredData.get(0);
        }
    }

    public ArrayList<Ticket> getAllData() {
        return allData;
    }
}