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

import entity.Player;

public class PostgresPlayerDAO implements IDAO<Player>
{
    private Connection connection;

    private Optional<Player> getOneOptUser(ResultSet resultSet) throws SQLException
    {
        Player player = null;

        if (resultSet.next())
            player = new Player(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("second_name"),
                resultSet.getString("third_name"),
                new Date(((java.sql.Date)resultSet.getObject("birth_date")).getTime()),
                resultSet.getString("country"),
                resultSet.getInt("raiting")
            );
        
        return Optional.ofNullable(player);
    }

    private Optional<List<Player>> fromResultSetToOptionalList(ResultSet resultSet) throws SQLException
    {
        List<Player> lst = new ArrayList<Player>();

        while (resultSet.next())
        {
            Player player = new Player(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("second_name"),
                resultSet.getString("third_name"),
                new Date(((java.sql.Date)resultSet.getObject("birth_date")).getTime()),
                resultSet.getString("country"),
                resultSet.getInt("raiting")
            );

            lst.add(player);
        }

        if (lst.isEmpty())
            return Optional.empty();
        
        return Optional.of(lst);
    }

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
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresPlayerDAO.get(Player): no connection to data base"
                );
            }

            String query = String.format(
                "select * from players where id = %d;",
                entity.getId()
            );
            
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            var result = getOneOptUser(queryResult);

            return result;
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresPlayerDAO.get(Player): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<List<Player>> get(final List<Player> entities) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresPlayerDAO.get(List<Player>): no connection to data base"
                );
            }

            var iter = entities.listIterator();

            String query = String.format(
                "select * from players where id = %d",
                iter.next().getId()
            );

            while (iter.hasNext())
                query += String.format(
                    " union select * from players where id = %d",
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
                    "PostgresPlayerDAO.get(List<Player>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<List<Player>> get(String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresPlayerDAO.get(String, String): no connection to data base"
                );
            }

            String query = String.format(
                "select * from players where %s = '%s';",
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
                    "PostgresPlayerDAO.get(String, String): %s",
                    e.getMessage()
                )
            );
        }
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
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresPlayerDAO.create(List<Player>): no connection to data base"
                );
            }

            var iter = entities.listIterator();
            var obj = iter.next();

            String query = String.format(
                "insert into players values (%d, '%s', '%s', '%s', '%s', '%s', %d)",
                obj.getId(),
                obj.getFirstName(),
                obj.getSecondName(),
                obj.getThirdName(),
                new SimpleDateFormat("yyyy-MM-dd").format(obj.getBirthDate()),
                obj.getCountry(),
                obj.getRaiting()
            );

            while (iter.hasNext())
            {
                obj = iter.next();

                query += String.format(
                    ", (%d, '%s', '%s', '%s', '%s', '%s', %d)",
                    obj.getId(),
                    obj.getFirstName(),
                    obj.getSecondName(),
                    obj.getThirdName(),
                    new SimpleDateFormat("yyyy-MM-dd").format(obj.getBirthDate()),
                    obj.getCountry(),
                    obj.getRaiting()
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
                    "PostgresPlayerDAO.create(List<Player>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final Player entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresPlayerDAO.update(Player): no connection to data base"
                );
            }

            String query = String.format(
                "update players set first_name = '%s', second_name = '%s', third_name = '%s', birth_date = '%s', country = '%s', raiting = '%s' where id = %d;",
                entity.getFirstName(),
                entity.getSecondName(),
                entity.getThirdName(),
                new SimpleDateFormat("yyyy-MM-dd").format(entity.getBirthDate()),
                entity.getCountry(),
                entity.getRaiting(),
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresPlayerDAO.update(Player): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final List<Player> entities) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBookDAO.update(List<Player>): no connection to data base"
                );
            }
            
            String query = "begin;";

            for (var entity: entities)
            {
                query += String.format(
                    "update players set first_name = '%s', second_name = '%s', third_name = '%s', birth_date = '%s', country = '%s', raiting = '%s' where id = %d;",
                    entity.getFirstName(),
                    entity.getSecondName(),
                    entity.getThirdName(),
                    new SimpleDateFormat("yyyy-MM-dd").format(entity.getBirthDate()),
                    entity.getCountry(),
                    entity.getRaiting(),
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
                    "PostgresPlayerDAO.update(List<Player>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final Player entity, String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresPlayerDAO.update(Player, String, String): no connection to data base"
                );
            }

            String query = String.format(
                "update players set %s = '%s' where id = %d;",
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
                    "PostgresPlayerDAO.update(Player, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final Player entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresPlayerDAO.update(Player, List<Pair<String, String>>): no connection to data base"
                );
            }

            var iter = updates.listIterator();
            var update = iter.next();

            String query = String.format(
                "update players set %s = '%s'",
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
                    "PostgresPlayerDAO.update(Player, List<Pair<String, String>>): %s",
                    e.getMessage()
                )
            );
        }
    }
    
    public void update(final Player entity, String attributeName, final int delta) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresPlayerDAO.update(Player, String, int): no connection to data base"
                );
            }

            String query = String.format(
                "update players set %s = %s + %s where id = %d;",
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
                    "PostgresPlayerDAO.update(Player, String, int): %s",
                    e.getMessage()
                )
            );
        }
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
