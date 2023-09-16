package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import appexception.*;
import javafx.util.Pair;

import entity.GameMove;
import entity.Game;
import entity.Move;
import entity.enums.Figure;
import entity.enums.GameResult;

public class PostgresGameMoveDAO implements IDAO<GameMove>
{
    private Connection connection;

    public PostgresGameMoveDAO(
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
                    "PostgresGameMoveDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<GameMove> get(final GameMove entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveDAO.get(GameMove): method is not implemented"
        );
    }

    public Optional<List<GameMove>> get(final List<GameMove> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveDAO.get(List<GameMove>): method is not implemented"
        );
    }

    public Optional<List<GameMove>> get(String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameMoveDAO.get(String, String): no connection to data base"
                );
            }

            String query = String.format(
                "select game_id, round, duration, number, m.number as move_number, result, date, referee_id, first_player_id, second_player_id, move_id, figure, start_cell, end_cell, comment " +
                "from (select game_id, move_id, number, comment from game_moves gm join moves m on gm.id = m.game_id where %s = %d) " +
                "join games g on game_id = g.id " +
                "order by move_number;",
                attributeName,
                value
            );

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<GameMove> result = new ArrayList<GameMove>();

            while (resultSet.next())
            {
                var game = new Game(
                    resultSet.getInt("game_id"),
                    resultSet.getInt("round"),
                    resultSet.getInt("duration"),
                    resultSet.getInt("number"),
                    GameResult.values()[resultSet.getInt("result")],
                    new Date(((java.sql.Date)resultSet.getObject("date")).getTime()),
                    resultSet.getInt("referee_id"),
                    resultSet.getInt("first_player_id"),
                    resultSet.getInt("second_player_id")
                );

                var move = new Move(
                    resultSet.getInt("move_id"),
                    Figure.values()[resultSet.getInt("figure")],
                    resultSet.getString("start_cell"),
                    resultSet.getString("end_cell")
                );

                result.add(
                    new GameMove(
                        game, move, resultSet.getInt("move_number"), resultSet.getString("comment")
                    )
                );
            }

            if (result.isEmpty())
                return Optional.empty();
            
            return Optional.of(result);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameMoveDAO.delete(GameMove): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void create(final GameMove entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveDAO.create(GameMove): method is not implemented"
        );
    }

    public void create(final List<GameMove> entities) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameMoveDAO.delete(GameMove): no connection to data base"
                );
            }

            Statement statement = connection.createStatement();

            for (var entity: entities)
            {
                Move move = entity.getMove();
                Game game = entity.getGame();

                statement.executeQuery(
                    String.format(
                        "delete from game_moves where game_id = %d;",
                        game.getId()
                    )
                );

                ResultSet resultSet = statement.executeQuery(
                    String.format(
                        "select * from moves where figure = %d and start_cell = '%s' and end_cell = '%s';",
                        move.getFigure().ordinal(),
                        move.getStartCell(),
                        move.getEndCell()
                    )
                );

                Integer moveId = 0;

                if (!resultSet.next())
                {
                    ResultSet moveResultSet = statement.executeQuery(
                        String.format(
                            "insert into moves values (%d, '%s', '%s')",
                            move.getFigure().ordinal(),
                            move.getStartCell(),
                            move.getEndCell()
                        )
                    );

                    moveResultSet.next();

                    moveId = moveResultSet.getInt("id");
                }
                else
                    moveId = resultSet.getInt("id");

                statement.executeQuery(
                    String.format(
                        "insert into game_moves values (%d, %d, %d, '%s')",
                        game.getId(),
                        moveId,
                        entity.getNumber(),
                        entity.getComment()
                    )
                );
            }
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameMoveDAO.delete(GameMove): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final GameMove entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveDAO.update(GameMove): method is not implemented"
        );
    }

    public void update(final List<GameMove> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveDAO.update(List<GameMove>): method is not implemented"
        );
    }

    public void update(final GameMove entity, String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveDAO.update(GameMove, String, String): method is not implemented"
        );
    }

    public void update(final GameMove entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveDAO.update(GameMove, List<Pair<String, String>>): method is not implemented"
        );
    }
    
    public void update(final GameMove entity, String attributeName, final int delta) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresGameMoveDAO.update(GameMove, String, int): method is not implemented"
        );
    }

    public void delete(final GameMove entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresGameMoveDAO.delete(GameMove): no connection to data base"
                );
            }

            String query = String.format(
                "delete from game_move where game_id = %d and move_id = %d;",
                entity.getGame().getId(),
                entity.getMove().getId()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresGameMoveDAO.delete(GameMove): %s",
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
