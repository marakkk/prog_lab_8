package server.utility;

import client.commandLine.BlankConsole;
import client.commandLine.Printable;
import common.exceptions.DatabaseHandlingException;
import common.models.Ticket;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Operates the collection.
 */

public class CollectionManager {
    private ReentrantLock lock = new ReentrantLock();

    private Printable console = new BlankConsole();
    private ArrayDeque<Ticket> collection = new ArrayDeque<>();

    private DatabaseCollectionHandler databaseCollectionHandler;

    public CollectionManager(DatabaseCollectionHandler databaseCollectionHandler) throws IOException {
        this.databaseCollectionHandler = databaseCollectionHandler;
        loadCollection();
    }

    public ArrayDeque<Ticket> getCollection(){
        try {
            lock.lock();
            return collection;
        }finally {
            lock.unlock();
        }
    }

    public void clearCollection() {
        collection.clear();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize(){
        return collection.size();
    }

    /**
     * @return The first element of the collection or null if collection is empty.
     */
   public Ticket getFirst(){
        return collection.stream().findFirst().orElse(null);
    }


    /**
     * @return A ticket by his ID or null if ticket isn't found.
     */
    public Ticket getById(int id) {
        for (Ticket element : collection) {
            if (element.getId() == id) return element;
        }
        return null;
    }

    /**
     * @param ticket A ticket which value will be found.
     * @return A marine by his value or null if marine isn't found.
     */
    public Ticket getByValue(Ticket ticket){
        for (Ticket tickets : collection){
            if (tickets.equals(ticket)) return tickets;
        }
        return null;
    }

    /**
     * Removes greater.
     */
    public void removeGreater(Ticket ticketToCompare) {
        collection.removeIf(ticket -> ticket.compareTo(ticketToCompare) > 0);
    }

    /**
     * Adds a new ticket to collection.
     * @param element A ticket to add.
     */
    public void addToCollection(Ticket element) {
        collection.add(element);
    }

    /**
     * Removes a new ticket to collection.
     * @param element A marine to remove.
     */
    public void removeFromCollection(Ticket element) {
        collection.remove(element);
    }

    /**
     * Exits the program
     */
    public Object exit() throws IOException {
        console.println("Work of program is ended");
        System.exit(0);

        return true;
    }

    /**
     * Loads the collection from file.
     */
    public ArrayDeque<Ticket> loadCollection() throws IOException {
        try{
            collection = databaseCollectionHandler.getCollection();
            System.out.println("Collection is loaded");
            console.println("Collection is loaded");


        }catch (DatabaseHandlingException exception) {
            collection = new ArrayDeque<>();
            console.printError("Collection cannot be loaded");
        }
        return collection;
    }


    /**
     * Counting group by its creation date
     *
     * @return
     */
    public Object groupCountingByCrDate(){
            HashMap<LocalDateTime, TreeSet<Ticket>> groupMap = new HashMap<>();
        for (Ticket i : collection){
            if (groupMap.get((i).getCreationDate()) == null){
                TreeSet<Ticket> x = new TreeSet<>();
                x.add(i);
                groupMap.put((i).getCreationDate(), x);
            } else groupMap.get((i).getCreationDate()).add(i);
        }
        for (Map.Entry<LocalDateTime, TreeSet<Ticket>> entry : groupMap.entrySet()){
            ResponseOutputer.appendln("Elements created in " + entry.getKey() + " :\n");
            entry.getValue().forEach(CollectionManager::display);
            System.out.println("Elements created in " + entry.getKey() + " :\n");
            entry.getValue().forEach(CollectionManager::see);
        }
        return true;
    }
    /**
     * Display the info about created ticket
     */
    public static void display(Ticket ticket) {
        ResponseOutputer.appendln("ID of ticket:" + ticket.getId());
        ResponseOutputer.appendln("Name of ticket:" + ticket.getName());
        ResponseOutputer.appendln("Creation date of ticket:" + ticket.getCreationDate());
        ResponseOutputer.appendln("Coordinates:" + ticket.getCoordinates());
        ResponseOutputer.appendln("Price:" + ticket.getPrice());
        ResponseOutputer.appendln("Discount:" + ticket.getDiscount());
        ResponseOutputer.appendln("Refund:" + ticket.getRefundable());
        ResponseOutputer.appendln("Type of ticket:" + ticket.getType());
        ResponseOutputer.appendln("Human's info:" + ticket.getPerson());
    };
    public static void see(Ticket ticket){
        System.out.println("ID of ticket:" + ticket.getId());
        System.out.println("Name of ticket:" + ticket.getName());
        System.out.println("Creation date of ticket:" + ticket.getCreationDate());
        System.out.println("Coordinates:" + ticket.getCoordinates());
        System.out.println("Price:" + ticket.getPrice());
        System.out.println("Discount:" + ticket.getDiscount());
        System.out.println("Refund:" + ticket.getRefundable());
        System.out.println("Type of ticket:" + ticket.getType());
        System.out.println("Human's info:" + ticket.getPerson());
    }
    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";
        var last = getLast();

        StringBuilder info = new StringBuilder();
        for (Ticket studyGroup : collection) {
            info.append(studyGroup);
            if (studyGroup != last) info.append("\n\n");
        }
        return info.toString();
    }
    public Ticket getLast() {
            return collection.getLast();

    }
}
