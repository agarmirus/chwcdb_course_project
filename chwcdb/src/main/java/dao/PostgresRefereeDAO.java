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

import entity.Referee;

public class PostgresRefereeDAO implements IDAO<Referee>
{
    private Connection connection;

    public PostgresRefereeDAO(
        String url,
        String referee,
        String pswd
    ) throws Exception
    {
        connection = DriverManager.getConnection(url, referee, pswd);
    }

    public void setConnection(String url, String referee, String pswd) throws CHWCDBException
    {
        try
        {
            connection = DriverManager.getConnection(url, referee, pswd);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresRefereeDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<Referee> get(final Referee entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeDAO.get(Referee): method is not implemented"
        );
    }

    public Optional<List<Referee>> get(final List<Referee> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeDAO.get(List<Referee>): method is not implemented"
        );
    }

    public Optional<List<Referee>> get(String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeDAO.get(String, String): method is not implemented"
        );
    }

    public void create(final Referee entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeDAO.create(Referee): no connection to data base"
                );
            }

            String query = String.format(
                "insert into referees values (%d, '%s', '%s', '%s', '%s', '%s');",
                entity.getId(),
                entity.getFirstName(),
                entity.getSecondName(),
                entity.getThirdName(),
                new SimpleDateFormat("yyyy-MM-dd").format(entity.getBirthDate()),
                entity.getCountry()
            );
            
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresRefereeDAO.create(Referee): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void create(final List<Referee> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeDAO.create(List<Referee>): method is not implemented"
        );
    }

    public void update(final Referee entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeDAO.update(Referee): method is not implemented"
        );
    }

    public void update(final List<Referee> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeDAO.update(List<Referee>): method is not implemented"
        );
    }

    public void update(final Referee entity, String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeDAO.update(Referee, String, String): method is not implemented"
        );
    }

    public void update(final Referee entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeDAO.update(Referee, List<Pair<String, String>>): method is not implemented"
        );
    }
    
    public void update(final Referee entity, String attributeName, final int delta) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeDAO.update(Referee, String, int): method is not implemented"
        );
    }

    public void delete(final Referee entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeDAO.delete(Referee): no connection to data base"
                );
            }

            String query = String.format(
                "delete from referees where id = %d;",
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresRefereeDAO.delete(Referee): %s",
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
