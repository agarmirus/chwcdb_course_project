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

import entity.Referee;

public class PostgresRefereeDAO implements IDAO<Referee>
{
    private Connection connection;

    private Optional<Referee> getOneOptReferee(ResultSet resultSet) throws SQLException
    {
        Referee referee = null;

        if (resultSet.next())
            referee = new Referee(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("second_name"),
                resultSet.getString("third_name"),
                new Date(((java.sql.Date)resultSet.getObject("birth_date")).getTime()),
                resultSet.getString("country")
            );
        
        return Optional.ofNullable(referee);
    }

    private Optional<List<Referee>> fromResultSetToOptionalList(ResultSet resultSet) throws SQLException
    {
        List<Referee> lst = new ArrayList<Referee>();

        while (resultSet.next())
        {
            Referee referee = new Referee(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("second_name"),
                resultSet.getString("third_name"),
                new Date(((java.sql.Date)resultSet.getObject("birth_date")).getTime()),
                resultSet.getString("country")
            );

            lst.add(referee);
        }

        if (lst.isEmpty())
            return Optional.empty();
        
        return Optional.of(lst);
    }

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
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeDAO.get(Referee): no connection to data base"
                );
            }

            String query = String.format(
                "select * from referees where id = %d;",
                entity.getId()
            );
            
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            var result = getOneOptReferee(queryResult);

            return result;
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresRefereeDAO.get(Referee): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<List<Referee>> get(final List<Referee> entities) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeDAO.get(List<Referee>): no connection to data base"
                );
            }

            var iter = entities.listIterator();

            String query = String.format(
                "select * from referees where id = %d",
                iter.next().getId()
            );

            while (iter.hasNext())
                query += String.format(
                    " union select * from referees where id = %d",
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
                    "PostgresRefereeDAO.get(List<Referee>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<List<Referee>> get(String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeDAO.get(String, String): no connection to data base"
                );
            }

            String query = String.format(
                "select * from referees where %s = '%s';",
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
                    "PostgresRefereeDAO.get(String, String): %s",
                    e.getMessage()
                )
            );
        }
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
            statement.executeQuery(query);
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
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeDAO.create(List<Referee>): no connection to data base"
                );
            }

            var iter = entities.listIterator();
            var obj = iter.next();

            String query = String.format(
                "insert into referees values (%d, '%s', '%s', '%s', '%s', '%s')",
                obj.getId(),
                obj.getFirstName(),
                obj.getSecondName(),
                obj.getThirdName(),
                new SimpleDateFormat("yyyy-MM-dd").format(obj.getBirthDate()),
                obj.getCountry()
            );

            while (iter.hasNext())
            {
                obj = iter.next();

                query += String.format(
                    ", (%d, '%s', '%s', '%s', '%s', '%s')",
                    obj.getId(),
                    obj.getFirstName(),
                    obj.getSecondName(),
                    obj.getThirdName(),
                    new SimpleDateFormat("yyyy-MM-dd").format(obj.getBirthDate()),
                    obj.getCountry()
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
                    "PostgresRefereeDAO.create(List<Referee>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final Referee entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeDAO.update(Referee): no connection to data base"
                );
            }

            String query = String.format(
                "update referees set first_name = '%s', second_name = '%s', third_name = '%s', birth_date = '%s', country = '%s' where id = %d;",
                entity.getFirstName(),
                entity.getSecondName(),
                entity.getThirdName(),
                new SimpleDateFormat("yyyy-MM-dd").format(entity.getBirthDate()),
                entity.getCountry(),
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresRefereeDAO.update(Referee): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final List<Referee> entities) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBookDAO.update(List<Referee>): no connection to data base"
                );
            }
            
            String query = "begin;";

            for (var entity: entities)
            {
                query += String.format(
                    "update referees set first_name = '%s', second_name = '%s', third_name = '%s', birth_date = '%s', country = '%s' where id = %d;",
                    entity.getFirstName(),
                    entity.getSecondName(),
                    entity.getThirdName(),
                    new SimpleDateFormat("yyyy-MM-dd").format(entity.getBirthDate()),
                    entity.getCountry(),
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
                    "PostgresRefereeDAO.update(List<Referee>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final Referee entity, String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeDAO.update(Referee, String, String): no connection to data base"
                );
            }

            String query = String.format(
                "update referees set %s = '%s' where id = %d;",
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
                    "PostgresRefereeDAO.update(Referee, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final Referee entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeDAO.update(Referee, List<Pair<String, String>>): no connection to data base"
                );
            }

            var iter = updates.listIterator();
            var update = iter.next();

            String query = String.format(
                "update referees set %s = '%s'",
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

            query += String.format(" where id = '%s';", entity.getId());

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresRefereeDAO.update(Referee, List<Pair<String, String>>): %s",
                    e.getMessage()
                )
            );
        }
    }
    
    public void update(final Referee entity, String attributeName, final int delta) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeDAO.update(Referee, String, int): no connection to data base"
                );
            }

            String query = String.format(
                "update referees set %s = %s + %s where id = %d;",
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
                    "PostgresRefereeDAO.update(Referee, String, int): %s",
                    e.getMessage()
                )
            );
        }
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
            statement.executeQuery(query);
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
