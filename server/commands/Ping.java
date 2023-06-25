package server.commands;

import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;

public class Ping extends AbstractCommand{
    public Ping() {
        super("ping", ": пингануть сервер");
    }

    /**
     * Исполнить команду
     * @param request запрос клиента
     * @throws IllegalArguments неверные аргументы команды
     */
    @Override
    public ServerResponse execute(ClientRequest request) throws IllegalArgumentException {
        return new ServerResponse("pong", ResponseCode.OK);
    }
}
