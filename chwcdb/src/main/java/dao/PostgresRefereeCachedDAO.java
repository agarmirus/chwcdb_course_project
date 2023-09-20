package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.MapOptions.WriteMode;
import org.redisson.api.map.MapWriter;

import appexception.*;
import entity.Referee;
import javafx.util.Pair;

public class PostgresRefereeCachedDAO extends PostgresRefereeDAO
{
    private Connection connection;
    private RMapCache<String, String> cache;
    private long ttl;

    public PostgresRefereeCachedDAO(
        String url,
        String user,
        String pswd,
        final RedissonClient client,
        final long ttl
    ) throws Exception
    {
        super(url, user, pswd);

        connection = DriverManager.getConnection(url, user, pswd);

        MapWriter<String, String> mapWriter = new MapWriter<String, String>()
        {
            @Override
            public void write(Map<String, String> map)
            {
                try
                {
                    PreparedStatement preparedStatement = connection.prepareStatement("insert into referees values (?, ?, ?, ?, ?, ?)");

                    for (var entry: map.entrySet())
                    {
                        var id = Integer.parseInt(entry.getKey().split(":")[1]);

                        var jsonObject = new JSONObject(entry.getValue());

                        preparedStatement.setInt(1, id);
                        preparedStatement.setString(2, jsonObject.getString("firstName"));
                        preparedStatement.setString(3, jsonObject.getString("secondName"));
                        preparedStatement.setString(4, jsonObject.getString("thirdName"));
                        preparedStatement.setString(5, jsonObject.getString("birthDate"));
                        preparedStatement.setString(6, jsonObject.getString("country"));

                        preparedStatement.addBatch();
                    }

                    preparedStatement.executeBatch();
                    preparedStatement.close();
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                    return;
                }
            }

            @Override
            public void delete(Collection<String> keys)
            {
                try
                {
                    PreparedStatement preparedStatement = connection.prepareStatement("delete from referees where id = ?");

                    for (var key: keys)
                    {
                        Integer id = Integer.parseInt(key.split(":")[1]);
                        
                        preparedStatement.setInt(1, id);
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
                        .writeMode(WriteMode.WRITE_THROUGH);
        cache = client.getMapCache("cache", options);

        this.ttl = ttl;
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
                    "PostgresRefereeCachedDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    @Override
    public Optional<Referee> get(final Referee entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeCachedDAO.get(Referee): no connection to data base"
                );
            }

            String key = String.format("referee:%d", entity.getId());
            String value = cache.get(key);

            if (value == null)
                return Optional.empty();

            JSONObject jsonObject = new JSONObject(value);

            var referee = new Referee(
                jsonObject.getInt("id"),
                jsonObject.getString("firstName"),
                jsonObject.getString("secondName"),
                jsonObject.getString("thirdName"),
                new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("birthDate")),
                jsonObject.getString("country")
            );

            return Optional.of(referee);
        }
        catch (Exception e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresRefereeCachedDAO.get(Referee): %s",
                    e.getMessage()
                )
            );
        }
    }

    @Override
    public Optional<List<Referee>> get(final List<Referee> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeCachedDAO.get(List<Referee>): method is not implemented"
        );
    }

    @Override
    public Optional<List<Referee>> get(String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeCachedDAO.get(String, String): method is not implemented"
        );
    }

    @Override
    public void create(final Referee entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeCachedDAO.create(Referee): no connection to data base"
                );
            }

            String key = String.format("referee:%d", entity.getId());

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("firstName", entity.getFirstName());
            jsonObject.put("secondName", entity.getSecondName());
            jsonObject.put("thirdName", entity.getThirdName());
            jsonObject.put("birthDate", new SimpleDateFormat("yyyy-MM-dd").format(entity.getBirthDate()));
            jsonObject.put("country", entity.getCountry());

            cache.put(key, jsonObject.toString(), ttl, TimeUnit.MILLISECONDS);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresRefereeCachedDAO.create(Referee): %s",
                    e.getMessage()
                )
            );
        }
    }

    @Override
    public void create(final List<Referee> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeCachedDAO.create(List<Referee>): method is not implemented"
        );
    }

    @Override
    public void update(final Referee entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeCachedDAO.update(Referee): method is not implemented"
        );
    }

    @Override
    public void update(final List<Referee> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeCachedDAO.update(List<Referee>): method is not implemented"
        );
    }

    @Override
    public void update(final Referee entity, String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeCachedDAO.update(Referee, String, String): method is not implemented"
        );
    }

    @Override
    public void update(final Referee entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeCachedDAO.update(Referee, List<Pair<String, String>>): method is not implemented"
        );
    }

    @Override
    public void update(final Referee entity, String attributeName, final int delta) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresRefereeCachedDAO.update(Referee, String, int): method is not implemented"
        );
    }

    @Override
    public void delete(final Referee entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresRefereeCachedDAO.delete(Referee): no connection to data base"
                );
            }

            String key = String.format("referee:%d", entity.getId());

            cache.remove(key);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresRefereeCachedDAO.delete(Referee): %s",
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
                    "PostgresRefereeCachedDAO.close(): %s",
                    e.getMessage()
                )
            );
        }
    }
}
