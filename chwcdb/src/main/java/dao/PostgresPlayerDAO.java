package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import appexception.*;
import javafx.util.Pair;

import entity.Player;

public class PostgresPlayerDAO implements IDAO<Player>
{
    private Connection connection;

    public PostgresPlayerDAO(
        String url,
        String player,
        String pswd
    ) throws Exception
    {
        connection = DriverManager.getConnection(url, player, pswd);
    }

    public void setConnection(String url, String player, String pswd) throws CHWCDBException
    {
        try
        {
            connection = DriverManager.getConnection(url, player, pswd);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresPlayerDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<Player> get(final Player entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresPlayerDAO.get(Player): method is not implemented"
        );
    }

    public Optional<List<Player>> get(final List<Player> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresPlayerDAO.get(List<Player>): method is not implemented"
        );
    }

    public Optional<List<Player>> get(String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresPlayerDAO.get(String, String): method is not implemented"
        );
    }

    public void create(final Player entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresPlayerDAO.create(Player): no connection to data base"
                );
            }

            String query = String.format(
                "insert into players values (%d, '%s', '%s', '%s', '%s', '%s', %d);",
                entity.getId(),
                entity.getFirstName(),
                entity.getSecondName(),
                entity.getThirdName(),
                new SimpleDateFormat("yyyy-MM-dd").format(entity.getBirthDate()),
                entity.getCountry(),
                entity.getRaiting()
            );
            
            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresPlayerDAO.create(Player): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void create(final List<Player> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresPlayerDAO.create(List<Player>): method is not implemented"
        );
    }

    public void update(final Player entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresPlayerDAO.update(Player): method is not implemented"
        );
    }

    public void update(final List<Player> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresPlayerDAO.update(List<Player>): method is not implemented"
        );
    }

    public void update(final Player entity, String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresPlayerDAO.update(Player, String, String): method is not implemented"
        );
    }

    public void update(final Player entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresPlayerDAO.update(Player, List<Pair<String, String>>): method is not implemented"
        );
    }
    
    public void update(final Player entity, String attributeName, final int delta) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresPlayerDAO.update(Player, String, int): method is not implemented"
        );
    }

    public void delete(final Player entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresPlayerDAO.delete(Player): no connection to data base"
                );
            }

            String query = String.format(
                "delete from players where id = %d;",
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresPlayerDAO.delete(Player): %s",
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
