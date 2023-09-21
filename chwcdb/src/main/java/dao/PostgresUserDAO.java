package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import appexception.*;
import entity.User;
import entity.enums.UserRole;
import javafx.util.Pair;

public class PostgresUserDAO implements IDAO<User>
{
    private Connection connection;

    protected Optional<User> getOneOptUser(ResultSet resultSet) throws SQLException
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
                "select * from users where login = '%s' and hashed_pswd = '%s';",
                entity.getLogin(),
                entity.getHashedPassword()
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
                    "PostgresUserDAO.get(User): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<List<User>> get(final List<User> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserDAO.get(List<User>): method is not implemented"
        );
    }

    public Optional<List<User>> get(String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserDAO.get(String, String): method is not implemented"
        );
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
            statement.executeUpdate(query);
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
        throw new CHWCDBDataAccessException(
            "PostgresUserDAO.create(List<User>): method is not implemented"
        );
    }

    public void update(final User entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserDAO.update(User): method is not implemented"
        );
    }

    public void update(final List<User> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserDAO.update(List<User>): method is not implemented"
        );
    }

    public void update(final User entity, String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserDAO.update(User, String, String): method is not implemented"
        );
    }

    public void update(final User entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserDAO.update(User, List<Pair<String, String>>): method is not implemented"
        );
    }
    
    public void update(final User entity, String attributeName, final int delta) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserDAO.update(User, String, int): method is not implemented"
        );
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
            statement.executeUpdate(query);
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
