package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.MapOptions.WriteMode;
import org.redisson.api.map.MapWriter;

import appexception.*;
import entity.Bet;
import entity.enums.BetType;
import javafx.util.Pair;

public class PostgresBetCachedDAO extends PostgresBetDAO
{
    private Connection connection;
    private RMapCache<String, String> cache;
    private long ttl;

    public PostgresBetCachedDAO(
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
                    PreparedStatement betsPreparedStatement = connection.prepareStatement(
                        "insert into bets values (?, ?, ?, ?, ?, ?) on conflict (login) do udpate set status = ?"
                    );
                    PreparedStatement enclBetsPlPreparedStatement = null;

                    for (var entry: map.entrySet())
                    {
                        Integer id = Integer.parseInt(entry.getKey().split(":")[1]);

                        var jsonObject = new JSONObject(entry.getValue());
                        
                        BetType type = BetType.values()[jsonObject.getInt("type")];

                        betsPreparedStatement.setInt(1, id);
                        betsPreparedStatement.setInt(2, jsonObject.getInt("type"));

                        if (jsonObject.has("condition"))
                            betsPreparedStatement.setString(3, jsonObject.getString("condition"));
                        else
                            betsPreparedStatement.setString(3, null);
                        
                        if (jsonObject.has("coefficient"))
                            betsPreparedStatement.setDouble(4, jsonObject.getDouble("coefficient"));
                        else
                            betsPreparedStatement.setNull(4, Types.REAL);
                            
                        betsPreparedStatement.setInt(5, jsonObject.getInt("status"));

                        if (jsonObject.has("game_id"))
                            betsPreparedStatement.setInt(6, jsonObject.getInt("game_id"));
                        else
                            betsPreparedStatement.setNull(6, Types.INTEGER);
                        
                        betsPreparedStatement.setInt(7, jsonObject.getInt("status"));

                        betsPreparedStatement.addBatch();

                        if (type != BetType.ELEMENTARY)
                        {
                            if (enclBetsPlPreparedStatement == null)
                                enclBetsPlPreparedStatement = connection.prepareStatement(
                                    "insert into bet_enclosures values (?, ?) on conflict (game_id, enclosure_id) do nothing"
                                );
                            
                            var arr = jsonObject.getJSONArray("bets");

                            for (int i = 0; i < arr.length(); ++i)
                            {
                                enclBetsPlPreparedStatement.setInt(1, jsonObject.getInt("id"));
                                enclBetsPlPreparedStatement.setInt(2, arr.getJSONObject(i).getInt("id"));

                                enclBetsPlPreparedStatement.addBatch();
                            }
                        }
                    }

                    betsPreparedStatement.executeBatch();
                    betsPreparedStatement.close();

                    if (enclBetsPlPreparedStatement != null)
                    {
                        enclBetsPlPreparedStatement.executeBatch();
                        enclBetsPlPreparedStatement.close();
                    }
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
                    PreparedStatement preparedStatement = connection.prepareStatement("delete from bets where id = ?");

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
                    "PostgresBetCachedDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<Bet> get(final Bet entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetCachedDAO.get(Bet): method is not implemented"
        );
    }

    public Optional<List<Bet>> get(final List<Bet> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetCachedDAO.get(List<Bet>): method is not implemented"
        );
    }

    public Optional<List<Bet>> get(String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetCachedDAO.get(String, String): method is not implemented"
        );
    }

    public void create(final Bet entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBetCachedDAO.create(Bet): no connection to data base"
                );
            }
            
            var jsonObject = new JSONObject();

            jsonObject.put("type", entity.getType().ordinal());
            jsonObject.put("status", entity.getStatus().ordinal());

            if (entity.getType() == BetType.ELEMENTARY)
            {
                jsonObject.put("condition", entity.getCondition());
                jsonObject.put("coefficient", entity.getCoefficient());
                jsonObject.put("game_id", entity.getGameId());
            }
            else
            {
                var arr = new JSONArray();

                for (var bet: entity.getBets())
                {
                    var enclJSONObject = new JSONObject();
                    enclJSONObject.put("id", bet.getId());

                    arr.put(enclJSONObject);
                }

                jsonObject.put("bets", arr);
            }

            String key = String.format("bet:%d", entity.getId());

            cache.put(key, jsonObject.toString(), ttl, TimeUnit.MILLISECONDS);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetCachedDAO.create(Bet): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void create(final List<Bet> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetCachedDAO.get(String, String): method is not implemented"
        );
    }

    public void update(final Bet entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetCachedDAO.update(Bet): method is not implemented"
        );
    }

    public void update(final List<Bet> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetCachedDAO.update(List<Bet>): method is not implemented"
        );
    }

    public void update(final Bet entity, String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBetCachedDAO.update(Bet, String, String): no connection to data base"
                );
            }

            var jsonObject = new JSONObject();

            jsonObject.put("status", entity.getStatus().ordinal());

            String key = String.format("bet:%d", entity.getId());

            cache.replace(key, jsonObject.toString());
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetCachedDAO.update(Bet, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final Bet entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetCachedDAO.update(Bet, List<Pair<String, String>>): method is not implemented"
        );
    }
    
    public void update(final Bet entity, String attributeName, final int delta) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetCachedDAO.update(Bet, String, int): method is not implemented"
        );
    }

    public void delete(final Bet entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBetCachedDAO.delete(Bet): no connection to data base"
                );
            }

            String key = String.format("bet:%d", entity.getId());

            cache.remove(key);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetCachedDAO.delete(Bet): %s",
                    e.getMessage()
                )
            );
        }
    }

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
                    "PostgresBetCachedDAO.close(): %s",
                    e.getMessage()
                )
            );
        }
    }
}
