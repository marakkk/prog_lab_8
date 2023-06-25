package client.commandLine.forms;

import client.commandLine.*;
import client.utility.*;
import common.exceptions.ExceptionInFileMode;
import common.models.Country;
import common.models.Person;
import common.util.ConsoleColors;

import java.time.LocalDate;

public class PersonForm extends Form<Person>{
    private final Printable console;
    private final UserInput scanner;

    public PersonForm(Printable console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент класса {@link Person}
     * @return объект класса {@link Person}
     */
    @Override
    public Person build() {
        console.println(ConsoleColors.toColor("Создание объекта админа", ConsoleColors.PURPLE));
        Person person = new Person(
                askBirthday(),
                askHeight(),
                askWeight(),
                askNationality()
        );
        console.println(ConsoleColors.toColor("Создание объекта админа окончено успешно", ConsoleColors.PURPLE));
        return person;
    }

    private LocalDate askBirthday(){
        LocalDate birthday;
        while (true){
            console.println(ConsoleColors.toColor("Введите имя", ConsoleColors.GREEN));
            birthday = LocalDate.parse(scanner.nextLine().trim());
            if (birthday == null){
                console.printError("Имя не может быть пустым");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
            else{
                return birthday;
            }
        }
    }

    private Float askWeight(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите количество студентов", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Float.parseFloat(input);
            } catch (NumberFormatException exception) {
                console.printError("Число студентов должно быть числом типа long");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }
    private Float askHeight(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите количество студентов", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Float.parseFloat(input);
            } catch (NumberFormatException exception) {
                console.printError("Число студентов должно быть числом типа long");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }

    private Country askNationality(){
        return new NationalityForm(console).build();
    }

}
