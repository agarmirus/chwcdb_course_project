package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.MapOptions.WriteMode;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;

import appexception.*;
import entity.User;
import entity.enums.UserRole;
import javafx.util.Pair;

public class PostgresUserCachedDAO extends PostgresUserDAO
{
    private Connection connection;
    private RLocalCachedMap<String, String> cache;

    public PostgresUserCachedDAO(
        String url,
        String user,
        String pswd,
        final RedissonClient client
    ) throws Exception
    {
        super(url, user, pswd);

        connection = DriverManager.getConnection(url, user, pswd);

        MapLoader<String, String> mapLoader = new MapLoader<String,String>()
        {
            @Override
            public Iterable<String> loadAllKeys()
            {
                return null;
            }

            @Override
            public String load(String key)
            {
                try
                {
                    Statement statement = connection.createStatement();
                    String login = key.split(":")[1];

                    String query = String.format(
                        "select * from users where login = '%s';",
                        login
                    );
                    
                    ResultSet queryResult = statement.executeQuery(query);

                    var optResult = getOneOptUser(queryResult);

                    if (optResult.isEmpty())
                        return null;

                    var result = optResult.get();
                    
                    statement.close();

                    var jsonObject = new JSONObject();

                    jsonObject.put("login", result.getLogin());
                    jsonObject.put("role", result.getRole().name());

                    return jsonObject.toString();
                }
                catch (Exception e)
                {
                    return null;
                }
            }
        };

        MapWriter<String, String> mapWriter = new MapWriter<String, String>()
        {
            @Override
            public void write(Map<String, String> map)
            {
                try
                {
                    PreparedStatement preparedStatement = connection.prepareStatement("insert into users values (?, ?, ?)");

                    for (var entry: map.entrySet())
                    {
                        var jsonObject = new JSONObject(entry.getValue());

                        preparedStatement.setString(1, jsonObject.getString("login"));
                        preparedStatement.setString(2, jsonObject.getString("hashedPswd"));
                        preparedStatement.setInt(3, jsonObject.getInt("role"));

                        preparedStatement.addBatch();
                    }

                    preparedStatement.executeBatch();
                    preparedStatement.close();
                }
                catch (Exception e)
                {
                    return;
                }
            }

            @Override
            public void delete(Collection<String> keys)
            {
                try
                {
                    PreparedStatement preparedStatement = connection.prepareStatement("delete from users where login = ?");

                    for (var key: keys)
                    {
                        preparedStatement.setString(1, key);
                        preparedStatement.addBatch();
                    }

                    preparedStatement.executeBatch();
                    preparedStatement.close();
                }
                catch (Exception e)
                {
                    return;
                }
            }
        };

        var options = LocalCachedMapOptions.<String, String>defaults()
                        .writer(mapWriter)
                        .writeMode(WriteMode.WRITE_THROUGH)
                        .loader(mapLoader);
        cache = client.getLocalCachedMap("cache", options);
    }

    @Override
    public void setConnection(String url, String user, String pswd) throws CHWCDBException
    {
        try
        {
            super.setConnection(url, user, pswd);
            connection = DriverManager.getConnection(url, user, pswd);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserCachedDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    @Override
    public Optional<User> get(final User entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserCachedDAO.get(User): no connection to data base"
                );
            }

            String key = String.format("user:%s", entity.getLogin());
            String value = cache.get(key);

            if (value == null)
                return Optional.empty();

            JSONObject jsonObject = new JSONObject(value);

            var user = new User(
                jsonObject.getString("login"),
                null,
                UserRole.values()[jsonObject.getInt("role")]
            );

            return Optional.of(user);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserCachedDAO.get(User): %s",
                    e.getMessage()
                )
            );
        }
    }

    @Override
    public Optional<List<User>> get(final List<User> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserCachedDAO.get(List<User>): method is not implemented"
        );
    }

    @Override
    public Optional<List<User>> get(String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserCachedDAO.get(String, String): method is not implemented"
        );
    }

    @Override
    public void create(final User entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserCachedDAO.create(User): no connection to data base"
                );
            }

            String key = String.format("user:%s", entity.getLogin());

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("login", entity.getLogin());
            jsonObject.put("hashedPswd", entity.getHashedPassword());
            jsonObject.put("role", entity.getRole().ordinal());

            cache.put(key, jsonObject.toString());
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserCachedDAO.create(User): %s",
                    e.getMessage()
                )
            );
        }
    }

    @Override
    public void create(final List<User> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserCachedDAO.create(List<User>): method is not implemented"
        );
    }

    @Override
    public void update(final User entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserCachedDAO.update(User): method is not implemented"
        );
    }

    @Override
    public void update(final List<User> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserCachedDAO.update(List<User>): method is not implemented"
        );
    }

    @Override
    public void update(final User entity, String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserCachedDAO.update(User, String, String): method is not implemented"
        );
    }

    @Override
    public void update(final User entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserCachedDAO.update(User, List<Pair<String, String>>): method is not implemented"
        );
    }

    @Override
    public void update(final User entity, String attributeName, final int delta) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresUserCachedDAO.update(User, String, int): method is not implemented"
        );
    }

    @Override
    public void delete(final User entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresUserCachedDAO.delete(User): no connection to data base"
                );
            }

            String key = String.format("user:%s", entity.getLogin());

            cache.remove(key);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserCachedDAO.delete(User): %s",
                    e.getMessage()
                )
            );
        }
    }

    @Override
    public void close() throws CHWCDBException
    {
        try
        {
            super.close();

            if (!connection.isClosed())
                connection.close();
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresUserCachedDAO.close(): %s",
                    e.getMessage()
                )
            );
        }
    }
}
