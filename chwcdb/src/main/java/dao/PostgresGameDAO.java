package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import appexception.*;
import javafx.util.Pair;

import entity.Game;
import entity.enums.GameResult;

public class PostgresGameDAO
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

    private Optional<List<Game>> fromResultSetToOptionalList(ResultSet resultSet) throws SQLException
    {
        List<Game> lst = new ArrayList<Game>();

        while (resultSet.next())
        {
            Game game = new Game(
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

            lst.add(game);
        }

        if (lst.isEmpty())
            return Optional.empty();
        
        return Optional.of(lst);
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
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameDAO.get(List<Game>): no connection to data base"
                );
            }

            var iter = entities.listIterator();

            String query = String.format(
                "select * from games where id = %d",
                iter.next().getId()
            );

            while (iter.hasNext())
                query += String.format(
                    " union select * from games where id = %d",
                    iter.next().getId()
                );
            
            query += ";";
            
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            var result = fromResultSetToOptionalList(queryResult);

            return result;
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.get(List<Game>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<List<Game>> get(String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameDAO.get(String, String): no connection to data base"
                );
            }

            String query = String.format(
                "select * from games where %s = '%s';",
                attributeName,
                value
            );
            
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            var result = fromResultSetToOptionalList(queryResult);

            return result;
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.get(String, String): %s",
                    e.getMessage()
                )
            );
        }
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
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameDAO.create(List<Game>): no connection to data base"
                );
            }

            var iter = entities.listIterator();
            var obj = iter.next();

            String query = String.format(
                "insert into games values (%d, %d, %d, %d, %d, '%s', %d, %d, %d)",
                obj.getId(),
                obj.getRound(),
                obj.getDuration(),
                obj.getNumber(),
                obj.getResult().ordinal(),
                new SimpleDateFormat("yyyy-MM-dd").format(obj.getDate()),
                obj.getRefereeId(),
                obj.getFirstPlayerId(),
                obj.getSecondPlayerId()
            );

            while (iter.hasNext())
            {
                obj = iter.next();

                query += String.format(
                    ", (%d, %d, %d, %d, %d, '%s', %d, %d, %d)",
                    obj.getId(),
                    obj.getRound(),
                    obj.getDuration(),
                    obj.getNumber(),
                    obj.getResult().ordinal(),
                    new SimpleDateFormat("yyyy-MM-dd").format(obj.getDate()),
                    obj.getRefereeId(),
                    obj.getFirstPlayerId(),
                    obj.getSecondPlayerId()
                );
            }
            
            query += ";";
            
            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.create(List<Game>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final Game entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameDAO.update(Game): no connection to data base"
                );
            }

            String query = String.format(
                "update games set round = %d, duration = %d, number = %d, result = %d, date = '%s', referee_id = %d, first_player_id = %d, second_player_id = %d where id = %d;",
                entity.getRound(),
                entity.getDuration(),
                entity.getNumber(),
                entity.getResult().ordinal(),
                new SimpleDateFormat("yyyy-MM-dd").format(entity.getDate()),
                entity.getRefereeId(),
                entity.getFirstPlayerId(),
                entity.getSecondPlayerId(),
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.update(Game): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final List<Game> entities) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBookDAO.update(List<Game>): no connection to data base"
                );
            }
            
            String query = "begin;";

            for (var entity: entities)
            {
                query += String.format(
                    "update games set round = %d, duration = %d, number = %d, result = %d, date = '%s', referee_id = %d, first_player_id = %d, second_player_id = %d where id = %d;",
                    entity.getRound(),
                    entity.getDuration(),
                    entity.getNumber(),
                    entity.getResult().ordinal(),
                    new SimpleDateFormat("yyyy-MM-dd").format(entity.getDate()),
                    entity.getRefereeId(),
                    entity.getFirstPlayerId(),
                    entity.getSecondPlayerId(),
                    entity.getId()
                );
            }

            query = query.concat("commit;");

            Statement statement = connection.createStatement();
            statement.execute(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.update(List<Game>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final Game entity, String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameDAO.update(Game, String, String): no connection to data base"
                );
            }

            String query = String.format(
                "update games set %s = '%s' where id = %d;",
                attributeName,
                value,
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.update(Game, String, String): %s",
                    e.getMessage()
                )
            );
        }
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
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameDAO.update(Game, String, int): no connection to data base"
                );
            }

            String query = String.format(
                "update games set %s = %s + %s where id = %d;",
                attributeName,
                attributeName,
                delta,
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameDAO.update(Game, String, int): %s",
                    e.getMessage()
                )
            );
        }
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
