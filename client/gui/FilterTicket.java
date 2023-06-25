package client.gui;

import common.models.Ticket;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;

public class FilterTicket {
    private final LinkedHashMap<Integer, Predicate<Ticket>> predicates = new LinkedHashMap<>();

    public void addPredicate(int row, Predicate<Ticket> predicate){
        predicates.put(row, predicate);
    }

    public Predicate<Ticket> getPredicate(){
        return predicates.values().stream().reduce(ticket -> true, Predicate::and);
    }

    public void clearPredicates(){
        this.predicates.clear();
    }

    public void parsePredicate(int row, List<?> values){
        switch (row) {
            case 0 -> this.addPredicate(0, (o) -> values.contains(o.getId()));
            case 1 -> this.addPredicate(1, (o) -> values.contains(o.getName()));
            case 2 -> this.addPredicate(2, (o) -> values.contains(o.getCoordinates().getX()));
            case 3 -> this.addPredicate(3, (o) -> values.contains(o.getCoordinates().getX()));

            case 4 -> this.addPredicate(4, (o) -> values.contains(o.getCreationDate()));
            case 5 -> this.addPredicate(5, (o) -> values.contains(o.getPrice()));
            case 6 -> this.addPredicate(6, (o) -> values.contains(o.getDiscount()));
            case 7 -> this.addPredicate(7, (o) -> values.contains(o.getRefundable()));
            case 8 -> this.addPredicate(8, (o) -> values.contains(o.getType()));
            case 9 -> this.addPredicate(9, (o) -> values.contains(o.getPerson().getBirthday()));
            case 10 -> this.addPredicate(10, (o) -> values.contains(o.getPerson().getHeight()));
            case 11 -> this.addPredicate(11, (o) -> values.contains(o.getPerson().getWeight()));
            case 12 -> this.addPredicate(12, (o) -> values.contains(o.getPerson().getNationality()));
            case 13 -> this.addPredicate(13, (o) -> values.contains(o.getUserLogin()));
        }
    }
}
