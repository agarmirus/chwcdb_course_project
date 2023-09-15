package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import appexception.*;
import javafx.util.Pair;

import entity.Game;
import entity.enums.GameResult;

public class PostgresGameDAO implements IDAO<Game>
{
    private Connection connection;

    private Optional<Game> getOneOptGame(ResultSet resultSet) throws SQLException
    {
        Game game = null;

        if (resultSet.next())
            game = new Game(
                resultSet.getInt("id"),
                resultSet.getInt("round"),
                resultSet.getInt("duration"),
                resultSet.getInt("number"),
                GameResult.values()[resultSet.getInt("result")],
                new Date(((java.sql.Date)resultSet.getObject("date")).getTime()),
                resultSet.getInt("referee_id"),
                resultSet.getInt("first_player_id"),
                resultSet.getInt("second_player_id")
            );
        
        return Optional.ofNullable(game);
    }

    public PostgresGameDAO(
        String url,
        String game,
        String pswd
    ) throws Exception
    {
        connection = DriverManager.getConnection(url, game, pswd);
    }

    public void setConnection(String url, String game, String pswd) throws CHWCDBException
    {
        try
        {
            connection = DriverManager.getConnection(url, game, pswd);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<Game> get(final Game entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameDAO.get(Game): no connection to data base"
                );
            }

            String query = String.format(
                "select * from games where id = %d;",
                entity.getId()
            );
            
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            var result = getOneOptGame(queryResult);

            return result;
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.get(Game): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<List<Game>> get(final List<Game> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameDAO.get(List<Game>): method is not implemented"
        );
    }

    public Optional<List<Game>> get(String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameDAO.get(List<Game>): method is not implemented"
        );
    }

    public void create(final Game entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameDAO.create(Game): no connection to data base"
                );
            }

            String query = String.format(
                "insert into games values (%d, %d, %d, %d, %d, '%s', %d, %d, %d);",
                entity.getId(),
                entity.getRound(),
                entity.getDuration(),
                entity.getNumber(),
                entity.getResult().ordinal(),
                new SimpleDateFormat("yyyy-MM-dd").format(entity.getDate()),
                entity.getRefereeId(),
                entity.getFirstPlayerId(),
                entity.getSecondPlayerId()
            );
            
            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.create(Game): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void create(final List<Game> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameDAO.get(List<List<Game>>): method is not implemented"
        );
    }

    public void update(final Game entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameDAO.update(Game): method is not implemented"
        );
    }

    public void update(final List<Game> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameDAO.update(List<Game>): method is not implemented"
        );
    }

    public void update(final Game entity, String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameDAO.update(Game, String, String): method is not implemented"
        );
    }

    public void update(final Game entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameDAO.update(Game, List<Pair<String, String>>): no connection to data base"
                );
            }

            var iter = updates.listIterator();
            var update = iter.next();

            String query = String.format(
                "update games set %s = '%s'",
                update.getKey(),
                update.getValue()
            );

            while (iter.hasNext())
            {
                update = iter.next();

                query += String.format(
                    ", %s = '%s'",
                    update.getKey(),
                    update.getValue()
                );
            }

            query += String.format(" where id = %d;", entity.getId());

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.update(Game, List<Pair<String, String>>): %s",
                    e.getMessage()
                )
            );
        }
    }
    
    public void update(final Game entity, String attributeName, final int delta) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameDAO.update(Game, String, int): method is not implemented"
        );
    }

    public void delete(final Game entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameDAO.delete(Game): no connection to data base"
                );
            }

            String query = String.format(
                "delete from games where id = %d;",
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.delete(Game): %s",
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
