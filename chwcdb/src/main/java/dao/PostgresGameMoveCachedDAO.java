package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.MapOptions.WriteMode;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;

import appexception.*;
import entity.Game;
import entity.GameMove;
import entity.Move;
import entity.enums.Figure;
import entity.enums.GameResult;
import javafx.util.Pair;

public class PostgresGameMoveCachedDAO extends PostgresGameMoveDAO
{
    private Connection connection;
    private RMapCache<String, String> cache;
    private long ttl;
    private boolean deleted;

    public PostgresGameMoveCachedDAO(
        String url,
        String user,
        String pswd,
        final RedissonClient client,
        final long ttl
    ) throws Exception
    {
        super(url, user, pswd);

        deleted = false;

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
                    String[] keyParts = key.split(":");

                    Integer gameId = Integer.parseInt(keyParts[1]);
                    Integer moveNumber = Integer.parseInt(keyParts[2]);

                    var jsonObject = new JSONObject();

                    Statement statement = connection.createStatement();
                    String query = String.format(
                        "select game_id, round, duration, g.number as number, result, date, referee_id, first_player_id, second_player_id, move_id, jm.number as move_number, figure, start_cell, end_cell, comment " +
                        "from (select game_id, move_id, figure, start_cell, end_cell, number, comment from game_moves gm join moves m on move_id = m.id) as jm " +
                        "join games g on g.id = game_id " +
                        "where game_id = %d and jm.number = %d" +
                        "order by move_number;",
                        gameId,
                        moveNumber
                    );
                    ResultSet resultSet = statement.executeQuery(query);

                    if (!resultSet.next())
                        return null;
                    
                    jsonObject.put("round", resultSet.getInt("round"));
                    jsonObject.put("duration", resultSet.getInt("duration"));
                    jsonObject.put("number", resultSet.getInt("number"));
                    jsonObject.put("result", resultSet.getInt("result"));
                    jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date(((java.sql.Date)resultSet.getObject("date")).getTime())));
                    jsonObject.put("refereeId", resultSet.getInt("referee_id"));
                    jsonObject.put("firstPlayerId", resultSet.getInt("first_player_id"));
                    jsonObject.put("secondPlayerId", resultSet.getInt("second_player_id"));

                    jsonObject.put("figure", resultSet.getInt("figure"));
                    jsonObject.put("startCell", resultSet.getString("start_cell"));
                    jsonObject.put("endCell", resultSet.getString("end_cell"));

                    jsonObject.put("moveNumber", moveNumber);
                    jsonObject.put("comment", resultSet.getString("comment"));

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
                    Statement statement = connection.createStatement();

                    for (var entry: map.entrySet())
                    {
                        String[] keyParts = entry.getKey().split(":");

                        Integer gameId = Integer.parseInt(keyParts[1]);
                        int keyNum = 1;
                        String deletedKey = null;

                        do
                        {
                            deletedKey = cache.remove(String.format("gameMove:%d:%d", gameId, keyNum++));
                        }
                        while (deletedKey != null);

                        if (!deleted)
                        {
                            statement.executeUpdate(
                                String.format(
                                    "delete from game_moves where game_id = %d;",
                                    gameId
                                )
                            );

                            deleted = true;
                        }

                        var valueJSONObject = new JSONObject(entry.getValue());

                        ResultSet moveResultSet = statement.executeQuery(
                            String.format(
                                "select * from moves where figure = %d and start_cell = '%s' and end_cell = '%s';",
                                valueJSONObject.getInt("figure"),
                                valueJSONObject.getString("startCell"),
                                valueJSONObject.getString("endCell")
                            )
                        );

                        Integer moveId = 0;

                        if (!moveResultSet.next())
                        {
                            statement.executeUpdate(
                                String.format(
                                    "insert into moves values (default, %d, '%s', '%s');",
                                    valueJSONObject.getInt("figure"),
                                    valueJSONObject.getString("startCell"),
                                    valueJSONObject.getString("endCell")
                                )
                            );

                            ResultSet createdMove = statement.executeQuery(
                                String.format(
                                    "select * from moves where figure = %d and start_cell = '%s' and end_cell = '%s';",
                                    valueJSONObject.getInt("figure"),
                                    valueJSONObject.getString("startCell"),
                                    valueJSONObject.getString("endCell")
                                )
                            );
                            
                            createdMove.next();

                            moveId = createdMove.getInt("id");
                        }
                        else
                            moveId = moveResultSet.getInt("id");
                        
                        if (valueJSONObject.has("comment"))
                            statement.executeUpdate(
                                String.format(
                                    "insert into game_moves values (%d, %d, %d, '%s');",
                                    gameId,
                                    moveId,
                                    valueJSONObject.getInt("number"),
                                    valueJSONObject.getString("comment")
                                )
                            );
                        else
                            statement.executeUpdate(
                                String.format(
                                    "insert into game_moves values (%d, %d, %d, null);",
                                    gameId,
                                    moveId,
                                    valueJSONObject.getInt("number")
                                )
                            );
                    }

                    statement.close();
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
                    PreparedStatement statement = connection.prepareStatement("delete from game_moves where game_id = ? and move_id = ?");

                    for (var key: keys)
                    {
                        String[] keyParts = key.split(":");

                        Integer gameId = Integer.parseInt(keyParts[1]);
                        Integer moveId = Integer.parseInt(keyParts[2]);

                        statement.setInt(1, gameId);
                        statement.setInt(2, moveId);

                        statement.addBatch();
                    }

                    statement.executeBatch();
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
                        .loader(mapLoader)
                        .timeToLive(ttl, TimeUnit.MILLISECONDS);;
        cache = client.getMapCache("cache", options);
        
        this.ttl = ttl;
    }

    public void setConnection(String url, String referee, String pswd) throws CHWCDBException
    {
        try
        {
            super.setConnection(url, referee, pswd);
            connection = DriverManager.getConnection(url, referee, pswd);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameMoveCachedDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<GameMove> get(final GameMove entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveCachedDAO.get(GameMove): method is not implemented"
        );
    }

    public Optional<List<GameMove>> get(final List<GameMove> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveCachedDAO.get(List<GameMove>): method is not implemented"
        );
    }

    public Optional<List<GameMove>> get(String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameMoveCachedDAO.get(String, String): no connection to data base"
                );
            }
            
            Integer gameId = Integer.parseInt(value);

            List<GameMove> result = new ArrayList<GameMove>();

            String valueJSONString = null;
            int keyNumber = 1;

            do
            {
                String key = String.format("gameMove:%d:%d", gameId, keyNumber);

                valueJSONString = cache.get(key);

                if (valueJSONString != null)
                {
                    var resultJSONObject = new JSONObject(valueJSONString);

                    var game = new Game(
                        gameId,
                        resultJSONObject.getInt("round"),
                        resultJSONObject.getInt("duration"),
                        resultJSONObject.getInt("number"),
                        GameResult.values()[resultJSONObject.getInt("result")],
                        new SimpleDateFormat("yyyy-MM-dd").parse(resultJSONObject.getString("date")),
                        resultJSONObject.getInt("refereeId"),
                        resultJSONObject.getInt("firstPlayerId"),
                        resultJSONObject.getInt("secondPlayerId")
                    );

                    var move = new Move(
                        0,
                        Figure.values()[resultJSONObject.getInt("figure")],
                        resultJSONObject.getString("startCell"),
                        resultJSONObject.getString("endCell")
                    );

                    if (resultJSONObject.has("comment"))
                        result.add(
                            new GameMove(
                                game,
                                move,
                                keyNumber,
                                resultJSONObject.getString("comment")
                            )
                        );
                    else
                        result.add(
                            new GameMove(
                                game,
                                move,
                                keyNumber,
                                null
                            )
                        );
                    
                    ++keyNumber;
                }
            }
            while (valueJSONString != null);

            if (result.isEmpty())
                return Optional.empty();
            
            return Optional.of(result);
        }
        catch (Exception e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameMoveCachedDAO.get(String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void create(final GameMove entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveCachedDAO.create(GameMove): method is not implemented"
        );
    }

    public void create(final List<GameMove> entities) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                deleted = false;

                throw new CHWCDBDataAccessException(
                    "PostgresGameMoveCachedDAO.delete(GameMove): no connection to data base"
                );
            }

            deleted = false;

            for (var gameMove: entities)
            {
                Game game = gameMove.getGame();
                Move move = gameMove.getMove();

                var jsonObject = new JSONObject();
                jsonObject.put("figure", move.getFigure().ordinal());
                jsonObject.put("startCell", move.getStartCell());
                jsonObject.put("endCell", move.getEndCell());
                jsonObject.put("number", gameMove.getNumber());
                jsonObject.put("comment", gameMove.getComment());

                String key = String.format("gameMove:%d:%d", game.getId(), gameMove.getNumber());

                cache.put(key, jsonObject.toString(), ttl, TimeUnit.MILLISECONDS);
            }

            deleted = false;
        }
        catch (SQLException e)
        {
            deleted = false;

            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameMoveCachedDAO.delete(GameMove): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final GameMove entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveCachedDAO.update(GameMove): method is not implemented"
        );
    }

    public void update(final List<GameMove> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveCachedDAO.update(List<GameMove>): method is not implemented"
        );
    }

    public void update(final GameMove entity, String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveCachedDAO.update(GameMove, String, String): method is not implemented"
        );
    }

    public void update(final GameMove entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveCachedDAO.update(GameMove, List<Pair<String, String>>): method is not implemented"
        );
    }
    
    public void update(final GameMove entity, String attributeName, final int delta) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveCachedDAO.update(GameMove, String, int): method is not implemented"
        );
    }

    public void delete(final GameMove entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameMoveCachedDAO.delete(GameMove): no connection to data base"
                );
            }
            
            Integer gameId = entity.getGame().getId();
            Integer moveNumber = entity.getNumber();

            String mainKey = String.format("gameMove:%d:%d", gameId, moveNumber);

            cache.remove(mainKey);

            var allCacheKeys = cache.readAllKeySet();

            for (var key: allCacheKeys)
            {
                String[] keyParts = key.split(":");

                if (Integer.parseInt(keyParts[1]) == gameId)
                {
                    var jsonObject = new JSONObject(cache.get(key));
                    Integer n = jsonObject.getInt("moveNumber");

                    if (n >= moveNumber)
                        cache.remove(key);
                }
            }
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameMoveCachedDAO.delete(GameMove): %s",
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
                    "PostgresGameMoveCachedDAO.close(): %s",
                    e.getMessage()
                )
            );
        }
    }
}
