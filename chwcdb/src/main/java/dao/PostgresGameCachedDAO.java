package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.MapOptions.WriteMode;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;

import appexception.*;
import entity.Game;
import entity.enums.GameResult;
import javafx.util.Pair;

public class PostgresGameCachedDAO extends PostgresGameDAO
{
    private Connection connection;
    private RMapCache<String, String> cache;

    public PostgresGameCachedDAO(
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
                    Integer id = Integer.parseInt(key.split(":")[1]);

                    String query = String.format(
                        "select * from games where id = %d;",
                        id
                    );
                    
                    ResultSet queryResult = statement.executeQuery(query);

                    var optResult = getOneOptGame(queryResult);

                    if (optResult.isEmpty())
                        return null;

                    var result = optResult.get();
                    
                    statement.close();

                    var jsonObject = new JSONObject();

                    jsonObject.put("round", result.getRound());
                    jsonObject.put("duration", result.getDuration());
                    jsonObject.put("number", result.getNumber());
                    jsonObject.put("result", result.getResult().ordinal());
                    jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(result.getDate()));
                    jsonObject.put("refereeId", result.getRefereeId());
                    jsonObject.put("firstPlayerId", result.getFirstPlayerId());
                    jsonObject.put("secondPlayerId", result.getSecondPlayerId());

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
                    PreparedStatement preparedStatement = connection.prepareStatement("insert into games values (?, ?, ?, ?, ?, ?, ?, ?, ?) on conflict (id) update result = ?, duration = ?");

                    for (var entry: map.entrySet())
                    {
                        var id = Integer.parseInt(entry.getKey().split(":")[1]);

                        var jsonObject = new JSONObject(entry.getValue());

                        preparedStatement.setInt(1, id);
                        preparedStatement.setInt(2, jsonObject.getInt("round"));
                        preparedStatement.setInt(3, jsonObject.getInt("duration"));
                        preparedStatement.setInt(4, jsonObject.getInt("number"));
                        preparedStatement.setInt(4, jsonObject.getInt("result"));
                        preparedStatement.setString(5, jsonObject.getString("date"));
                        preparedStatement.setInt(6, jsonObject.getInt("refereeId"));
                        preparedStatement.setInt(7, jsonObject.getInt("firstPlayerId"));
                        preparedStatement.setInt(8, jsonObject.getInt("secondPlayerId"));
                        preparedStatement.setInt(9, jsonObject.getInt("result"));
                        preparedStatement.setInt(10, jsonObject.getInt("duration"));

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
                    PreparedStatement preparedStatement = connection.prepareStatement("delete from games where id = ?");

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
                        .writeMode(WriteMode.WRITE_THROUGH)
                        .loader(mapLoader);
        cache = client.getMapCache("cache", options);
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
                    "PostgresGameCachedDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    @Override
    public Optional<Game> get(final Game entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameCachedDAO.get(Game): no connection to data base"
                );
            }

            String key = String.format("game:%d", entity.getId());
            String value = cache.get(key);

            if (value == null)
                return Optional.empty();

            JSONObject jsonObject = new JSONObject(value);

            var game = new Game(
                jsonObject.getInt("id"),
                jsonObject.getInt("round"),
                jsonObject.getInt("duration"),
                jsonObject.getInt("number"),
                GameResult.values()[jsonObject.getInt("result")],
                new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("date")),
                jsonObject.getInt("refereeId"),
                jsonObject.getInt("firstPlayerId"),
                jsonObject.getInt("secondPlayerId")
            );

            return Optional.of(game);
        }
        catch (Exception e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameCachedDAO.get(Game): %s",
                    e.getMessage()
                )
            );
        }
    }

    @Override
    public Optional<List<Game>> get(final List<Game> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameCachedDAO.get(List<Game>): method is not implemented"
        );
    }

    @Override
    public Optional<List<Game>> get(String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameCachedDAO.get(String, String): method is not implemented"
        );
    }

    @Override
    public void create(final Game entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameCachedDAO.create(Game): no connection to data base"
                );
            }

            String key = String.format("game:%d", entity.getId());

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("round", entity.getRound());
            jsonObject.put("duration", entity.getDuration());
            jsonObject.put("number", entity.getNumber());
            jsonObject.put("result", entity.getResult().ordinal());
            jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(entity.getDate()));
            jsonObject.put("refereeId", entity.getRefereeId());
            jsonObject.put("firstPlayerId", entity.getFirstPlayerId());
            jsonObject.put("secondPlayerId", entity.getSecondPlayerId());

            cache.put(key, jsonObject.toString());
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameCachedDAO.create(Game): %s",
                    e.getMessage()
                )
            );
        }
    }

    @Override
    public void create(final List<Game> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameCachedDAO.create(List<Game>): method is not implemented"
        );
    }

    @Override
    public void update(final Game entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameCachedDAO.update(Game): method is not implemented"
        );
    }

    @Override
    public void update(final List<Game> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameCachedDAO.update(List<Game>): method is not implemented"
        );
    }

    @Override
    public void update(final Game entity, String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameCachedDAO.update(Game, String, String): method is not implemented"
        );
    }

    @Override
    public void update(final Game entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameCachedDAO.create(Game): no connection to data base"
                );
            }

            String key = String.format("game:%d", entity.getId());

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("round", entity.getRound());
            jsonObject.put("duration", entity.getDuration());
            jsonObject.put("number", entity.getNumber());
            jsonObject.put("result", entity.getResult().ordinal());
            jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(entity.getDate()));
            jsonObject.put("refereeId", entity.getRefereeId());
            jsonObject.put("firstPlayerId", entity.getFirstPlayerId());
            jsonObject.put("secondPlayerId", entity.getSecondPlayerId());

            for (var update: updates)
                jsonObject.put(update.getKey(), update.getValue());

            cache.put(key, jsonObject.toString());
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameCachedDAO.create(Game): %s",
                    e.getMessage()
                )
            );
        }
    }

    @Override
    public void update(final Game entity, String attributeName, final int delta) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameCachedDAO.update(Game, String, int): method is not implemented"
        );
    }

    @Override
    public void delete(final Game entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameCachedDAO.delete(Game): no connection to data base"
                );
            }

            String key = String.format("game:%d", entity.getId());

            cache.remove(key);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameCachedDAO.delete(Game): %s",
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
                    "PostgresGameCachedDAO.close(): %s",
                    e.getMessage()
                )
            );
        }
    }
}
