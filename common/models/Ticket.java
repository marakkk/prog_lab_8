package common.models;

import common.util.ConsoleColors;
import common.util.User;

import java.io.Serializable;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Ticket implements Comparable<Ticket>, Serializable, Validator {


    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int price; //Значение поля должно быть больше 0
    private long discount; //Значение поля должно быть больше 0, Максимальное значение поля: 100
    private Boolean refundable; //Поле не может быть null
    private TicketType type; //Поле может быть null
    private Person person; //Поле не может быть null
    private String userLogin;


    public Ticket(int id,
                  String name,
                  Coordinates coordinates,
                  LocalDateTime creationDate,
                  int price,
                  long discount,
                  Boolean refundable,
                  TicketType type,
                  Person person,
                  String userLogin
    ) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.discount = discount;
        this.refundable = refundable;
        this.type = type;
        this.person = person;
        this.userLogin = userLogin;
    }

    public Ticket(
            String name,
            Coordinates coordinates,
            LocalDateTime creationDate,
            int price,
            long discount,
            Boolean refundable,
            TicketType type,
            Person person,
            String userLogin
    ) {
        this.id = 0;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.discount = discount;
        this.refundable = refundable;
        this.type = type;
        this.person = person;
        this.userLogin = userLogin;
    }

    public Ticket(int id,
                  String name,
                  Coordinates coordinates,
                  LocalDateTime creationDate,
                  int price,
                  long discount,
                  Boolean refundable,
                  TicketType type,
                  Person person
    ) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.discount = discount;
        this.refundable = refundable;
        this.type = type;
        this.person = person;
    }

    public Ticket(
                  String name,
                  Coordinates coordinates,
                  LocalDateTime creationDate,
                  int price,
                  long discount,
                  Boolean refundable,
                  TicketType type,
                  Person person
    ) {
        this.id = 0;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.discount = discount;
        this.refundable = refundable;
        this.type = type;
        this.person = person;
    }
    @Override
    public int compareTo(Ticket ticket) {
        return getName().compareTo(ticket.getName());
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int getPrice() {
        return price;
    }

    public long getDiscount() {
        return discount;
    }

    public Boolean getRefundable() {
        return refundable;
    }

    public TicketType getType() {
        return type;
    }

    public Person getPerson() {
        return person;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
    @Override
    public String toString() {
        return "Ticket{" + '\n' +
                "\t" + ConsoleColors.toColor("id = ", ConsoleColors.CYAN) + id + '\n' +
                "\t" + ConsoleColors.toColor("ticket_name = ", ConsoleColors.CYAN) + name + '\n' +
                "\t" + ConsoleColors.toColor("coordinates = ", ConsoleColors.CYAN) + coordinates + '\n' +
                "\t" + ConsoleColors.toColor("creationDate = ", ConsoleColors.CYAN) + creationDate + '\n' +
                "\t" + ConsoleColors.toColor("price = ", ConsoleColors.CYAN) + price + '\n' +
                "\t" + ConsoleColors.toColor("discount = ", ConsoleColors.CYAN) + discount + '\n' +
                "\t" + ConsoleColors.toColor("refundable = ", ConsoleColors.CYAN) + refundable + '\n' +
                "\t" + ConsoleColors.toColor("ticketType = ", ConsoleColors.CYAN) + type + '\n' +
                "\t" + ConsoleColors.toColor("person = ", ConsoleColors.CYAN) + person + '\n' +
                "\t" + ConsoleColors.toColor("userId = ", ConsoleColors.CYAN) + userLogin + '\n' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCoordinates(), getCreationDate(), getPrice(), getDiscount(), getRefundable(), getType(), getPerson());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Ticket) {
            Ticket ticketObj = (Ticket) obj;
            return (
                    name.equals(ticketObj.getName()) &&
                            coordinates.equals(ticketObj.getCoordinates()) &&
                            creationDate.equals(ticketObj.getCreationDate()) &&
                            (price == ticketObj.getPrice()) &&
                            refundable.equals(ticketObj.getRefundable()) &&
                            type.equals(ticketObj.getType()) &&
                            person.equals(ticketObj.getPerson()));
        }
        return false;
    }
    @Override
    public boolean validate() {
        if (this.id < 0) return false;
        if (this.name == null || this.name.isEmpty()) return false;
        if (this.coordinates == null) return false;
        if (this.creationDate == null) return false;
        if (this.price <= 0) return false;
        if (this.discount <= 0) return false;
        if (this.refundable == null) return false;
        return this.person != null;
    }

}
