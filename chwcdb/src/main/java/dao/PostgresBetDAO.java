package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import appexception.*;
import javafx.util.Pair;

import entity.Bet;
import entity.enums.BetType;

public class PostgresBetDAO implements IDAO<Bet>
{
    private Connection connection;

    public PostgresBetDAO(
        String url,
        String bet,
        String pswd
    ) throws Exception
    {
        connection = DriverManager.getConnection(url, bet, pswd);
    }

    public void setConnection(String url, String user, String pswd) throws CHWCDBException
    {
        try
        {
            connection = DriverManager.getConnection(url, user, pswd);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<Bet> get(final Bet entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.get(Bet): method is not implemented"
        );
    }

    public Optional<List<Bet>> get(final List<Bet> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.get(List<Bet>): method is not implemented"
        );
    }

    public Optional<List<Bet>> get(String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.get(String, String): method is not implemented"
        );
    }

    public void create(final Bet entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBetDAO.create(Bet): no connection to data base"
                );
            }

            String query = String.format(
                "insert into bets values (%d, %d, '%s', %f, %d, %d);",
                entity.getId(),
                entity.getType().ordinal(),
                entity.getCondition(),
                entity.getCoefficient(),
                entity.getStatus().ordinal(),
                entity.getGameId()
            );

            
            if (entity.getType() != BetType.ELEMENTARY)
            {
                String betEnclosuresQuery = "";

                for (var enclBet: entity.getBets())
                {
                    if (betEnclosuresQuery.isEmpty())
                        betEnclosuresQuery = String.format(
                            "insert into bet_enclosures values (%d, %d)",
                            entity.getId(),
                            enclBet.getId()
                        );
                    else
                        betEnclosuresQuery += String.format(
                            ", (%d, %d)",
                            entity.getId(),
                            enclBet.getId()
                        );
                }

                query = "begin;" + query + betEnclosuresQuery + ";commit;";
            }

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetDAO.create(Bet): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void create(final List<Bet> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.get(String, String): method is not implemented"
        );
    }

    public void update(final Bet entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.update(Bet): method is not implemented"
        );
    }

    public void update(final List<Bet> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.update(List<Bet>): method is not implemented"
        );
    }

    public void update(final Bet entity, String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBetDAO.update(Bet, String, String): no connection to data base"
                );
            }

            String query = String.format(
                "update bets set %s = '%s' where id = %d;",
                attributeName,
                value,
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetDAO.update(Bet, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final Bet entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.update(Bet, List<Pair<String, String>>): method is not implemented"
        );
    }
    
    public void update(final Bet entity, String attributeName, final int delta) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.update(Bet, String, int): method is not implemented"
        );
    }

    public void delete(final Bet entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBetDAO.delete(Bet): no connection to data base"
                );
            }

            String query = String.format(
                "delete from bets where id = %d;",
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetDAO.delete(Bet): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void close() throws CHWCDBException
    {
        try
        {
            if (!connection.isClosed())
                connection.close();
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBookDAO.close(): %s",
                    e.getMessage()
                )
            );
        }
    }
}
