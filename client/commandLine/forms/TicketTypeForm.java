package client.commandLine.forms;

import client.commandLine.*;
import client.utility.*;
import common.exceptions.ExceptionInFileMode;
import common.models.TicketType;
import common.util.ConsoleColors;

import java.util.Locale;

public class TicketTypeForm extends Form<TicketType>{
    private final Printable console;
    private final UserInput scanner;

    public TicketTypeForm(Printable console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент Enum {@link common.models.TicketType}
     * @return объект Enum {@link common.models.TicketType}
     */
    @Override
    public TicketType build() {
        console.println("Возможные формы обучения: ");
        console.println(TicketType.names());
        while (true){
            console.println(ConsoleColors.toColor("Введите форму обучения: ", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try{
                return TicketType.valueOf(input.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException exception){
                console.printError("Такой формы обучения нет в списке");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }
}
