package server.utility;

import common.exceptions.DatabaseHandlingException;
import common.models.*;
import common.util.TicketRaw;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.NavigableSet;
import java.util.TreeSet;

public class DatabaseCollectionHandler {
    private Logger logger = LoggerFactory.getLogger("DatabaseCollectionHandler");

    //ticket table
    private final String SELECT_ALL_TICKETS = "SELECT * FROM " + DatabaseHandler.TICKET_TABLE;
    private final String SELECT_TICKET_BY_ID = SELECT_ALL_TICKETS + " WHERE " + DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_TICKET_BY_ID_AND_USER_ID = SELECT_TICKET_BY_ID + " AND " +
            DatabaseHandler.TICKET_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_TICKET = "INSERT INTO " +
            DatabaseHandler.TICKET_TABLE + " (" +
            // DatabaseHandler.TICKET_TABLE_ID_COLUMN + ", " +
            DatabaseHandler.TICKET_TABLE_TICKET_NAME_COLUMN + ", " +
            DatabaseHandler.TICKET_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseHandler.TICKET_COORDINATES_X_COLUMN + ", " +
            DatabaseHandler.TICKET_COORDINATES_Y_COLUMN + ", " +
            DatabaseHandler.TICKET_TABLE_PRICE_COLUMN + ", " +
            DatabaseHandler.TICKET_TABLE_DISCOUNT_COLUMN + ", " +
            DatabaseHandler.TICKET_TABLE_REFUND_COLUMN + ", " +
            DatabaseHandler.TICKET_TABLE_TICKET_TYPE_COLUMN + ", " +
            DatabaseHandler.TICKET_PERSON_DATE_OF_BIRTH_COLUMN + ", " +
            DatabaseHandler.TICKET_PERSON_HEIGHT_COLUMN + ", " +
            DatabaseHandler.TICKET_PERSON_WEIGHT_COLUMN + ", " +
            DatabaseHandler.TICKET_PERSON_COUNTRY_COLUMN + ", " +
            DatabaseHandler.TICKET_TABLE_USER_ID_COLUMN +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String DELETE_TICKET_BY_ID = "DELETE FROM " + DatabaseHandler.TICKET_TABLE +
            " WHERE " + DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_TICKET_NAME_BY_ID = "UPDATE " + DatabaseHandler.TICKET_TABLE + " SET " +
            DatabaseHandler.TICKET_TABLE_TICKET_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_TICKET_COORDINATE_X = "UPDATE " + DatabaseHandler.TICKET_TABLE + " SET " +
            DatabaseHandler.TICKET_COORDINATES_X_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_TICKET_COORDINATE_Y = "UPDATE " + DatabaseHandler.TICKET_TABLE + " SET " +
            DatabaseHandler.TICKET_COORDINATES_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_TICKET_PRICE_BY_ID = "UPDATE " + DatabaseHandler.TICKET_TABLE + " SET " +
            DatabaseHandler.TICKET_TABLE_PRICE_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";

    private final String UPDATE_TICKET_DISCOUNT_PRICE_BY_ID = "UPDATE " + DatabaseHandler.TICKET_TABLE + " SET " +
            DatabaseHandler.TICKET_TABLE_DISCOUNT_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_TICKET_REFUND_PRICE_BY_ID = "UPDATE " + DatabaseHandler.TICKET_TABLE + " SET " +
            DatabaseHandler.TICKET_TABLE_REFUND_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";

    private final String UPDATE_TICKET_TICKET_TYPE_BY_ID = "UPDATE " + DatabaseHandler.TICKET_TABLE + " SET " +
            DatabaseHandler.TICKET_TABLE_TICKET_TYPE_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";

    private final String UPDATE_TICKET_PERSON_BIRTHDAY_BY_ID = "UPDATE " + DatabaseHandler.TICKET_TABLE + " SET " +
            DatabaseHandler.TICKET_TABLE_TICKET_TYPE_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_TICKET_PERSON_HEIGHT_BY_ID = "UPDATE " + DatabaseHandler.TICKET_TABLE + " SET " +
            DatabaseHandler.TICKET_TABLE_TICKET_TYPE_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";

    private final String UPDATE_TICKET_PERSON_WEIGHT_BY_ID = "UPDATE " + DatabaseHandler.TICKET_TABLE + " SET " +
            DatabaseHandler.TICKET_TABLE_TICKET_TYPE_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";

    private final String UPDATE_TICKET_PERSON_NATIONALITY_BY_ID = "UPDATE " + DatabaseHandler.TICKET_TABLE + " SET " +
            DatabaseHandler.TICKET_TABLE_TICKET_TYPE_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.TICKET_TABLE_ID_COLUMN + " = ?";


    private DatabaseHandler databaseHandler;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionHandler(DatabaseHandler databaseHandler, DatabaseUserManager databaseUserManager){
        this.databaseHandler = databaseHandler;
        this.databaseUserManager = databaseUserManager;
    }

    private Ticket createTicket(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt(DatabaseHandler.TICKET_TABLE_ID_COLUMN);
        String name = resultSet.getString(DatabaseHandler.TICKET_TABLE_TICKET_NAME_COLUMN);
        LocalDateTime localDate = resultSet.getTimestamp(DatabaseHandler.TICKET_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        int price = resultSet.getInt(DatabaseHandler.TICKET_TABLE_PRICE_COLUMN);
        long discount = resultSet.getLong(DatabaseHandler.TICKET_TABLE_DISCOUNT_COLUMN);
        Boolean refundable = resultSet.getBoolean(DatabaseHandler.TICKET_TABLE_REFUND_COLUMN);
        TicketType type = TicketType.valueOf(resultSet.getString(DatabaseHandler.TICKET_TABLE_TICKET_TYPE_COLUMN));

        User owner = databaseUserManager.getUserById(resultSet.getInt(DatabaseHandler.TICKET_TABLE_USER_ID_COLUMN));
        return new Ticket(
                id,
                name,
                new Coordinates(
                        resultSet.getFloat(DatabaseHandler.TICKET_COORDINATES_X_COLUMN),
                        resultSet.getFloat(DatabaseHandler.TICKET_COORDINATES_Y_COLUMN)
                ),
                localDate,
                price,
                discount,
                refundable,
                type,
                new Person(
                        resultSet.getDate(DatabaseHandler.TICKET_PERSON_DATE_OF_BIRTH_COLUMN).toLocalDate(),
                        resultSet.getFloat(DatabaseHandler.TICKET_PERSON_HEIGHT_COLUMN),
                        resultSet.getFloat(DatabaseHandler.TICKET_PERSON_WEIGHT_COLUMN),
                        Country.valueOf(resultSet.getString(DatabaseHandler.TICKET_PERSON_COUNTRY_COLUMN))
                ),
                owner.username()

        );
    }

    public Ticket insertTicket(TicketRaw ticketRaw, User user) throws DatabaseHandlingException {
        Ticket ticket;
        PreparedStatement preparedInsertTicketStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();


            LocalDateTime creationDate = LocalDateTime.now();
            preparedInsertTicketStatement = databaseHandler.getPreparedStatement(INSERT_TICKET, true);




            preparedInsertTicketStatement.setString(1, ticketRaw.getName());
            preparedInsertTicketStatement.setTimestamp(2, Timestamp.valueOf(creationDate));
            preparedInsertTicketStatement.setFloat(3, ticketRaw.getCoordinates().getX());
            preparedInsertTicketStatement.setFloat(4, ticketRaw.getCoordinates().getY());

            preparedInsertTicketStatement.setDouble(5, ticketRaw.getPrice());
            preparedInsertTicketStatement.setFloat(6, ticketRaw.getDiscount());
            preparedInsertTicketStatement.setBoolean(7, ticketRaw.getRefundable());
            preparedInsertTicketStatement.setString(8, ticketRaw.getType().toString());
            preparedInsertTicketStatement.setDate(9, Date.valueOf(ticketRaw.getPerson().getBirthday()));
            preparedInsertTicketStatement.setFloat(10, ticketRaw.getPerson().getHeight());
            preparedInsertTicketStatement.setFloat(11, ticketRaw.getPerson().getWeight());
            preparedInsertTicketStatement.setString(12, String.valueOf(ticketRaw.getPerson().getNationality()));


            preparedInsertTicketStatement.setInt(8, databaseUserManager.getUserIdByUsername(user));

            if (preparedInsertTicketStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedMarineKeys = preparedInsertTicketStatement.getGeneratedKeys();

            int ticketId;
            if (generatedMarineKeys.next()) {
                ticketId = generatedMarineKeys.getInt(8);
            } else throw new SQLException();
            logger.debug("Request INSERT_TICKET is completed.");


            ticket = new Ticket(
                    ticketId,
                    ticketRaw.getName(),
                    ticketRaw.getCoordinates(),
                    creationDate,
                    ticketRaw.getPrice(),
                    ticketRaw.getDiscount(),
                    ticketRaw.getRefundable(),
                    ticketRaw.getType(),
                    ticketRaw.getPerson(),
                    user.username()
            );

            databaseHandler.commit();
            return ticket;
        } catch (SQLException exception) {
            logger.error(exception.getMessage());
            logger.error("An error occurred while executing requests to add a new object");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedInsertTicketStatement);
            databaseHandler.setNormalMode();
        }
    }


    public ArrayDeque<Ticket> getCollection() throws DatabaseHandlingException{
        ArrayDeque<Ticket> tickets = new ArrayDeque<>();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = databaseHandler.getPreparedStatement(SELECT_ALL_TICKETS, false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                tickets.add(new Ticket(
                        resultSet.getInt("id"),
                        resultSet.getString("ticket_name"),
                        new Coordinates(
                                resultSet.getFloat("x"),
                                resultSet.getFloat("y")
                        ),
                        resultSet.getTimestamp("creation_date").toLocalDateTime(),
                        resultSet.getInt("price"),
                        resultSet.getLong("discount"),
                        resultSet.getBoolean("refundable"),
                        TicketType.valueOf(resultSet.getString("ticket_type")),
                        new Person(
                                resultSet.getDate("person_birthday").toLocalDate(),
                                resultSet.getFloat("person_height"),
                                resultSet.getFloat("person_weight"),
                                Country.valueOf(resultSet.getString("person_nationality"))
                        ),
                        resultSet.getString("user_id")
                ));
            }
        }catch (SQLException E){
            throw new DatabaseHandlingException();
        }finally {
            databaseHandler.closePreparedStatement(preparedStatement);
        }
        return tickets;
    }
    public void updateTicketById(int ticketId, TicketRaw ticketRaw) throws DatabaseHandlingException {
        PreparedStatement preparedUpdateTicketNameByIdStatement = null;
        PreparedStatement preparedUpdateTicketCoordinatesXByIdStatement = null;
        PreparedStatement preparedUpdateTicketCoordinatesYByIdStatement = null;

        PreparedStatement preparedUpdateTicketPriceByIdStatement = null;
        PreparedStatement preparedUpdateTicketDiscountByIdStatement = null;
        PreparedStatement preparedUpdateTicketRefundableByIdStatement = null;
        PreparedStatement preparedUpdateTicketTypeByIdStatement = null;
        PreparedStatement preparedUpdateTicketPersonBirthdayByIdStatement = null;
        PreparedStatement preparedUpdateTicketPersonHeightByIdStatement = null;
        PreparedStatement preparedUpdateTicketPersonWeightByIdStatement = null;
        PreparedStatement preparedUpdateTicketPersonNationalityByIdStatement = null;



        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            preparedUpdateTicketNameByIdStatement = databaseHandler.getPreparedStatement(UPDATE_TICKET_NAME_BY_ID, false);
            preparedUpdateTicketCoordinatesXByIdStatement = databaseHandler.getPreparedStatement(UPDATE_TICKET_COORDINATE_X, false);
            preparedUpdateTicketCoordinatesYByIdStatement = databaseHandler.getPreparedStatement(UPDATE_TICKET_COORDINATE_Y, false);

            preparedUpdateTicketPriceByIdStatement = databaseHandler.getPreparedStatement(UPDATE_TICKET_PRICE_BY_ID, false);
            preparedUpdateTicketDiscountByIdStatement = databaseHandler.getPreparedStatement(UPDATE_TICKET_DISCOUNT_PRICE_BY_ID, false);
            preparedUpdateTicketRefundableByIdStatement = databaseHandler.getPreparedStatement(UPDATE_TICKET_REFUND_PRICE_BY_ID, false);
            preparedUpdateTicketTypeByIdStatement = databaseHandler.getPreparedStatement(UPDATE_TICKET_TICKET_TYPE_BY_ID, false);
            preparedUpdateTicketPersonBirthdayByIdStatement = databaseHandler.getPreparedStatement(UPDATE_TICKET_PERSON_BIRTHDAY_BY_ID, false);
            preparedUpdateTicketPersonHeightByIdStatement = databaseHandler.getPreparedStatement(UPDATE_TICKET_PERSON_HEIGHT_BY_ID, false);
            preparedUpdateTicketPersonWeightByIdStatement = databaseHandler.getPreparedStatement(UPDATE_TICKET_PERSON_WEIGHT_BY_ID, false);
            preparedUpdateTicketPersonNationalityByIdStatement = databaseHandler.getPreparedStatement(UPDATE_TICKET_PERSON_NATIONALITY_BY_ID, false);






            if (ticketRaw.getName() != null) {
                preparedUpdateTicketNameByIdStatement.setString(1, ticketRaw.getName());
                preparedUpdateTicketNameByIdStatement.setInt(2, ticketId);
                if (preparedUpdateTicketNameByIdStatement.executeUpdate() == 0) throw new SQLException();
                logger.debug("Request completed: UPDATE_TICKET_NAME_BY_ID.");
            }
            if (ticketRaw.getCoordinates() != null) {
                preparedUpdateTicketCoordinatesXByIdStatement.setFloat(1, ticketRaw.getCoordinates().getX());
                preparedUpdateTicketCoordinatesYByIdStatement.setFloat(2, ticketRaw.getCoordinates().getY());
                if (preparedUpdateTicketCoordinatesXByIdStatement.executeUpdate() == 0) throw new SQLException();
                if (preparedUpdateTicketCoordinatesYByIdStatement.executeUpdate() == 0) throw new SQLException();

                logger.debug("Request completed: UPDATE_COORDINATES_BY_TICKET_ID.");
            }
            if (ticketRaw.getPrice() != -1) {
                preparedUpdateTicketPriceByIdStatement.setDouble(1, ticketRaw.getPrice());
                preparedUpdateTicketPriceByIdStatement.setInt(2, ticketId);
                if (preparedUpdateTicketPriceByIdStatement.executeUpdate() == 0) throw new SQLException();
                logger.debug("Request completed: UPDATE_TICKET_PRICE_BY_ID.");
            }

            if (ticketRaw.getDiscount() != -1) {
                preparedUpdateTicketDiscountByIdStatement.setDouble(1, ticketRaw.getDiscount());
                preparedUpdateTicketDiscountByIdStatement.setInt(2, ticketId);
                if (preparedUpdateTicketDiscountByIdStatement.executeUpdate() == 0) throw new SQLException();
                logger.debug("Request completed: UPDATE_TICKET_DISCOUNT_BY_ID.");
            }

            if (ticketRaw.getRefundable() != null) {
                preparedUpdateTicketRefundableByIdStatement.setBoolean(1, ticketRaw.getRefundable());
                preparedUpdateTicketRefundableByIdStatement.setInt(2, ticketId);
                if (preparedUpdateTicketRefundableByIdStatement.executeUpdate() == 0) throw new SQLException();
                logger.debug("Request completed: UPDATE_TICKET_REFUNDABLE_BY_ID.");
            }
            if (ticketRaw.getType() != null) {
                preparedUpdateTicketTypeByIdStatement.setString(1, ticketRaw.getType().toString());
                preparedUpdateTicketTypeByIdStatement.setInt(2, ticketId);
                if (preparedUpdateTicketTypeByIdStatement.executeUpdate() == 0) throw new SQLException();
                logger.debug("Request completed: UPDATE_TICKET_TYPE_BY_ID.");
            }
            if (ticketRaw.getPerson() != null) {

                preparedUpdateTicketPersonBirthdayByIdStatement.setDate(1, Date.valueOf(ticketRaw.getPerson().getBirthday()));
                preparedUpdateTicketPersonHeightByIdStatement.setFloat(2, ticketRaw.getPerson().getHeight());
                preparedUpdateTicketPersonWeightByIdStatement.setFloat(3, ticketRaw.getPerson().getWeight());
                preparedUpdateTicketPersonNationalityByIdStatement.setString(4, String.valueOf(ticketRaw.getPerson().getNationality()));
                if (preparedUpdateTicketPersonBirthdayByIdStatement.executeUpdate() == 0) throw new SQLException();
                if (preparedUpdateTicketPersonHeightByIdStatement.executeUpdate() == 0) throw new SQLException();
                if (preparedUpdateTicketPersonWeightByIdStatement.executeUpdate() == 0) throw new SQLException();
                if (preparedUpdateTicketPersonNationalityByIdStatement.executeUpdate() == 0) throw new SQLException();

                logger.debug("Request completed: UPDATE_PERSON_BY_ID.");
            }

            databaseHandler.commit();
        } catch (SQLException exception) {
            logger.error("Mistake occurred while trying to update data");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedUpdateTicketNameByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateTicketCoordinatesXByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateTicketCoordinatesYByIdStatement);

            databaseHandler.closePreparedStatement(preparedUpdateTicketPriceByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateTicketDiscountByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateTicketRefundableByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateTicketTypeByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateTicketPersonBirthdayByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateTicketPersonHeightByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateTicketPersonWeightByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateTicketPersonNationalityByIdStatement);

            databaseHandler.setNormalMode();
        }
    }

    public void deleteTicketById(int ticketId) throws DatabaseHandlingException {
        PreparedStatement preparedDeleteTicketByIdStatement = null;
        try {
            preparedDeleteTicketByIdStatement = databaseHandler.getPreparedStatement(DELETE_TICKET_BY_ID, false);
            if (preparedDeleteTicketByIdStatement.executeUpdate() == 0) throw new SQLException();
            logger.debug("Request completed: DELETE_TICKET_BY_ID.");
        } catch (SQLException exception) {
            logger.error("Mistake occurred while doing request: DELETE_TICKET_BY_ID!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedDeleteTicketByIdStatement);
        }
    }


    public boolean checkTicketUserId(int ticketId, User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectTicketByIdAndUserIdStatement = null;
        try {
            preparedSelectTicketByIdAndUserIdStatement = databaseHandler.getPreparedStatement(SELECT_TICKET_BY_ID_AND_USER_ID, false);
            preparedSelectTicketByIdAndUserIdStatement.setInt(1, ticketId);
            preparedSelectTicketByIdAndUserIdStatement.setInt(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedSelectTicketByIdAndUserIdStatement.executeQuery();
            logger.debug("Request completed: SELECT_TICKET_BY_ID_AND_USER_ID.");
            return !resultSet.next();
        } catch (SQLException exception) {
            logger.error("Mistake occurred while doing request: SELECT_MARINE_BY_ID_AND_USER_ID!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectTicketByIdAndUserIdStatement);
        }
    }

    /**
     * Clear the collection.
     *
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public void clearCollection() throws DatabaseHandlingException {
        ArrayDeque<Ticket> tickets = getCollection();
        for (Ticket ticket : tickets) {
            deleteTicketById(ticket.getId());
        }
    }

}