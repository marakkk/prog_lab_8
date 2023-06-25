package client.commandLine.forms;

import client.commandLine.*;
import client.utility.*;
import common.exceptions.ExceptionInFileMode;
import common.models.Country;
import common.util.ConsoleColors;

import java.util.Locale;

public class NationalityForm extends Form<Country>{
    private final Printable console;
    private final UserInput scanner;

    public NationalityForm(Printable console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент Enum {@link NationalityForm}
     * @return объект Enum {@link NationalityForm}
     */
    @Override
    public Country build() {
        console.println("Возможные страны: ");
        console.println(Country.names());
        while (true){
            console.println(ConsoleColors.toColor("Введите страну: ", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try{
                return Country.valueOf(input.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException exception){
                console.printError("Такой страны нет в списке");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }
}
