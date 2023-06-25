package client.commandLine.forms;

import client.commandLine.*;
import client.utility.*;
import common.exceptions.ExceptionInFileMode;
import common.models.Coordinates;
import common.models.Person;
import common.models.Ticket;
import common.models.TicketType;
import common.util.ConsoleColors;

import java.time.LocalDateTime;

public class TicketForm extends Form<Ticket> {
    private final Printable console;

    private final UserInput scanner;

    public TicketForm(Printable console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент класса {@link Ticket}
     * @return объект класса {@link Ticket}
     */
    @Override
    public Ticket build(){
        return new Ticket(
                askName(),
                askCoordinates(),
                LocalDateTime.now(),
                askPrice(),
                askDiscount(),
                askRefund(),
                askTicketType(),
                askPerson()
        );

    }

    private String askName(){
        String name;
        while (true){
            console.println(ConsoleColors.toColor("Введите имя", ConsoleColors.GREEN));
            name = scanner.nextLine().trim();
            if (name.isEmpty()){
                console.printError("Имя не может быть пустым");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
            else{
                return name;
            }
        }
    }

    private Coordinates askCoordinates(){
        return new CoordinatesForm(console).build();
    }

    private Integer askPrice(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите количество студентов", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                console.printError("Число студентов должно быть числом типа int");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }

    private long askDiscount(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите количество отчисленных студентов", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException exception) {
                console.printError("Число студентов должно быть числом типа long");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }

    private Boolean askRefund(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите среднюю оценку", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Boolean.parseBoolean(input);
            } catch (NumberFormatException exception) {
                console.printError("Оценка должна быть числом типа long");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }

    private TicketType askTicketType(){
        return new TicketTypeForm(console).build();
    }

    private Person askPerson(){
        return new PersonForm(console).build();
    }
}
