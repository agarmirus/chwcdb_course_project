package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import appexception.*;
import entity.User;
import entity.enums.UserRole;
import javafx.util.Pair;

public class PostgresUserDAO implements IDAO<User>
{
    private Connection connection;

    private Optional<User> getOneOptBook(ResultSet resultSet) throws SQLException
    {
        User user = null;

        if (resultSet.next())
            user = new User(
                resultSet.getString("login"),
                resultSet.getString("hashed_pswd"),
                UserRole.values()[resultSet.getInt("role")]
            );
        
        return Optional.ofNullable(user);
    }

    private Optional<List<User>> fromResultSetToOptionalList(ResultSet resultSet) throws SQLException
    {
        List<User> lst = new ArrayList<User>();

        while (resultSet.next())
        {
            User user = new User(
                resultSet.getString("login"),
                resultSet.getString("hashed_pswd"),
                UserRole.values()[resultSet.getInt("role")]
            );

            lst.add(user);
        }

        if (lst.isEmpty())
            return Optional.empty();
        
        return Optional.of(lst);
    }

    public PostgresUserDAO(
        String url,
        String user,
        String pswd
    ) throws Exception
    {
        connection = DriverManager.getConnection(url, user, pswd);
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
                    "PostgresUserDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<User> get(final User entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserDAO.get(User): no connection to data base"
                );
            }

            String query = String.format(
                "select * from users where login = '%s';",
                entity.getLogin()
            );
            
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            var result = getOneOptBook(queryResult);

            return result;
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserDAO.get(User): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<List<User>> get(final List<User> entities) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserDAO.get(List<User>): no connection to data base"
                );
            }

            var iter = entities.listIterator();

            String query = String.format(
                "select * from users where login = '%s'",
                iter.next().getLogin()
            );

            while (iter.hasNext())
                query += String.format(
                    " union select * from users where login = '%s'",
                    iter.next().getLogin()
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
                    "PostgresUserDAO.get(List<User>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<List<User>> get(String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserDAO.get(String, String): no connection to data base"
                );
            }

            String query = String.format(
                "select * from users where %s = '%s';",
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
                    "PostgresUserDAO.get(String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void create(final User entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserDAO.create(User): no connection to data base"
                );
            }

            String query = String.format(
                "insert into users values ('%s', '%s', %d);",
                entity.getLogin(),
                entity.getHashedPassword(),
                entity.getRole().ordinal()
            );
            
            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserDAO.create(User): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void create(final List<User> entities) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserDAO.create(List<User>): no connection to data base"
                );
            }

            var iter = entities.listIterator();
            var obj = iter.next();

            String query = String.format(
                "insert into users values ('%s', '%s', %d)",
                obj.getLogin(),
                obj.getHashedPassword(),
                obj.getRole().ordinal()
            );

            while (iter.hasNext())
            {
                obj = iter.next();

                query += String.format(
                    ", ('%s', '%s', %d)",
                    obj.getLogin(),
                    obj.getHashedPassword(),
                    obj.getRole().ordinal()
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
                    "PostgresUserDAO.create(List<User>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final User entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserDAO.update(User): no connection to data base"
                );
            }

            String query = String.format(
                "update users set hashed_pswd = '%s', role = '%d' where login = '%s';",
                entity.getHashedPassword(),
                entity.getRole().ordinal(),
                entity.getLogin()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserDAO.update(User): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final List<User> entities) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBookDAO.update(List<User>): no connection to data base"
                );
            }
            
            String query = "begin;";

            for (var entity: entities)
            {
                query += String.format(
                    "update users set hashed_pswd = '%s', role = '%d' where login = '%s';",
                    entity.getHashedPassword(),
                    entity.getRole().ordinal(),
                    entity.getLogin()
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
                    "PostgresUserDAO.update(List<User>): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final User entity, String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserDAO.update(User, String, String): no connection to data base"
                );
            }

            String query = String.format(
                "update users set %s = '%s' where login = '%s';",
                attributeName,
                value,
                entity.getLogin()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserDAO.update(User, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final User entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserDAO.update(User, List<Pair<String, String>>): no connection to data base"
                );
            }

            var iter = updates.listIterator();
            var update = iter.next();

            String query = String.format(
                "update users set %s = '%s'",
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

            query += String.format(" where login = '%s';");

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserDAO.update(User, List<Pair<String, String>>): %s",
                    e.getMessage()
                )
            );
        }
    }
    
    public void update(final User entity, String attributeName, final int delta) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserDAO.update(User, String, int): no connection to data base"
                );
            }

            String query = String.format(
                "update users set %s = %s + %s where login = '%s';",
                attributeName,
                attributeName,
                delta,
                entity.getLogin()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserDAO.update(User, String, int): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void delete(final User entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserDAO.delete(User): no connection to data base"
                );
            }

            String query = String.format(
                "delete from users where login = '%s';",
                entity.getLogin()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserDAO.delete(User): %s",
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
