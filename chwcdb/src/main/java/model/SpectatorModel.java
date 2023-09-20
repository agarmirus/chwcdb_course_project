package model;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import org.json.JSONObject;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

import javafx.util.Pair;

import dao.IDAO;
import entity.Game;
import entity.Move;
import entity.enums.Figure;
import entity.enums.GameResult;
import entity.GameMove;
import appexception.*;

public class SpectatorModel extends IModel
{
    private IDAO<Game> gameDAO;
    private IDAO<GameMove> gameMoveDAO;

    private RQueue<String> taskQueue;

    public SpectatorModel(
        final IDAO<Game> gameDAO,
        final IDAO<GameMove> gameMoveDAO,
        final RedissonClient client
    )
    {
        this.gameDAO = gameDAO;
        this.gameMoveDAO = gameMoveDAO;
        this.taskQueue = client.getQueue("taskqueue");
    }

    @Override
    public void addGame(final Game game) throws CHWCDBException
    {
        if (gameDAO == null)
            throw new CHWCDBNullDAOException(
                "SpectatorModel.addGame(Game): game DAO is null"
            );
        
        if (game == null)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.addGame(Game): game is null"
            );
        
        int round = game.getRound();

        if (round < 1 || round > 8)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.addGame(Game): round is not in range from 1 to 8"
            );

        if (game.getNumber() < 1)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.addGame(Game): game number is less than 1"
            );
        
        if (game.getDate() == null)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.addGame(Game): date is null"
            );
        
        if (game.getRefereeId() < 1)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.addGame(Game): referee ID is less than 1"
            );
        
        if (game.getFirstPlayerId() < 1)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.addGame(Game): first player ID is less than 1"
            );
        
        if (game.getSecondPlayerId() < 1)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.addGame(Game): second player ID is less than 1"
            );

        if (game.getFirstPlayerId() == game.getSecondPlayerId())
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.addGame(Game): players are not different"
            );
        
        game.setDuration(0);
        game.setResult(null);

        gameDAO.create(game);
    }

    @Override
    public void removeGame(final Game game) throws CHWCDBException
    {
        if (gameDAO == null)
            throw new CHWCDBNullDAOException(
                "SpectatorModel.removeGame(Game): game DAO is null"
            );
        
        if (game == null)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.removeGame(Game): game is null"
            );
        
        if (game.getId() < 1)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.removeGame(Game): game ID is less than 1"
            );
        
        gameDAO.delete(game);
    }

    @Override
    public void endGame(final Game game) throws CHWCDBException
    {
        if (gameDAO == null)
            throw new CHWCDBNullDAOException(
                "SpectatorModel.endGame(Game): game DAO is null"
            );
        
        if (game == null)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.endGame(Game): game is null"
            );
        
        if (game.getId() < 1)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.endGame(Game): game ID is less than 1"
            );
        
        int duration = game.getDuration();

        if (duration < 0)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.endGame(Game): game duration is less than 1"
            );

        GameResult result = game.getResult();
        
        if (result == null)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.endGame(Game): game result is null"
            );
        
        List<Pair<String, String>> updates = new ArrayList<>();
        var durationUpdate = new Pair<String, String>(
            "duration",
            Integer.toString(duration)
        );
        var resultUpdate = new Pair<String, String>(
            "result",
            Integer.toString(result.ordinal())
        );
        updates.add(durationUpdate);
        updates.add(resultUpdate);

        gameDAO.update(game, updates);
    }

    @Override
    public Optional<List<GameMove>> getGameInfo(final Game game) throws CHWCDBException
    {
        if (gameMoveDAO == null)
            throw new CHWCDBNullDAOException(
                "SpectatorModel.getGameMoves(Game): game move DAO is null"
            );
        
        if (game == null)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.getGameMoves(Game): game is null"
            );

        int gameId = game.getId();
        
        if (gameId < 1)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.getGameMoves(Game): game ID is less than 1"
            );
        
        var result = gameMoveDAO.get("game_id", Integer.toString(gameId));

        return result;
    }

    @Override
    public void addMoves(final List<GameMove> gameMoves) throws CHWCDBException
    {
        if (gameMoveDAO == null)
            throw new CHWCDBNullDAOException(
                "SpectatorModel.addMoves(List<GameMove>): game move DAO is null"
            );
        
        if (gameMoves == null)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.addMoves(List<GameMove>): game moves list is null"
            );
        
        if (!gameMoves.isEmpty())
        {
            for (var gameMove: gameMoves)
            {
                if (gameMove == null)
                    throw new CHWCDBInvalidParameterException(
                        "SpectatorModel.addMoves(List<GameMove>): game move is null"
                    );
                    
                Game game = gameMove.getGame();

                if (game == null)
                    throw new CHWCDBInvalidParameterException(
                        "SpectatorModel.addMoves(List<GameMove>): game is null"
                    );

                if (game.getId() < 1)
                    throw new CHWCDBInvalidParameterException(
                        "SpectatorModel.addMoves(List<GameMove>): game ID is less than 1"
                    );
                
                Move move = gameMove.getMove();

                if (move == null)
                    throw new CHWCDBInvalidParameterException(
                        "SpectatorModel.addMoves(List<GameMove>): move is null"
                    );

                if (move.getFigure() == null)
                    throw new CHWCDBInvalidParameterException(
                        "SpectatorModel.addMoves(List<GameMove>): move doesn't have a figure"
                    );
                
                String startCell = move.getStartCell();

                if (startCell == null || startCell.isEmpty())
                    throw new CHWCDBInvalidParameterException(
                        "SpectatorModel.addMoves(List<GameMove>): move doesn't have a start cell"
                    );

                String endCell = move.getEndCell();

                if (endCell == null || endCell.isEmpty())
                    throw new CHWCDBInvalidParameterException(
                        "SpectatorModel.addMoves(List<GameMove>): move doesn't have an end cell"
                    );
            }
            
            gameMoveDAO.create(gameMoves);
        }
    }

    @Override
    public void removeMove(final GameMove gameMove) throws CHWCDBException
    {
        if (gameMoveDAO == null)
            throw new CHWCDBNullDAOException(
                "SpectatorModel.removeMove(GameMove): game move DAO is null"
            );
        
        if (gameMove == null)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.removeMove(GameMove): game move is null"
            );
        
        Game game = gameMove.getGame();

        if (game == null)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.removeMove(GameMove): game is null"
            );

        if (game.getId() < 1)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.removeMove(GameMove): game ID is less than 1"
            );
        
        Move move = gameMove.getMove();

        if (move == null)
            throw new CHWCDBInvalidParameterException(
                "SpectatorModel.removeMove(GameMove): move is null"
            );
        
        gameMoveDAO.delete(gameMove);
    }

    Game getGameFromJSON(JSONObject jsonObject) throws Exception
    {
        return new Game(
            jsonObject.getInt("id"),
            jsonObject.getInt("round"),
            null,
            jsonObject.getInt("number"),
            null,
            new SimpleDateFormat("dd.MM.yyyy").parse(jsonObject.getString("date")),
            jsonObject.getInt("refereeId"),
            jsonObject.getInt("firstPlayerId"),
            jsonObject.getInt("secondPlayerId")
        );
    }

    GameMove getRemovingGameMoveFromJSON(JSONObject jsonObject) throws Exception
    {
        if (jsonObject.has("comment"))
            return new GameMove(
                new Game(jsonObject.getInt("gameId")),
                new Move(jsonObject.getInt("moveId")),
                null,
                null
            );
        
        return new GameMove(
            new Game(jsonObject.getInt("gameId")),
            new Move(jsonObject.getInt("moveId")),
            null,
            null
        );
    }

    List<GameMove> getGameMovesFromJSON(JSONObject jsonObject) throws Exception
    {
        List<GameMove> result = new ArrayList<GameMove>();

        var game = new Game(jsonObject.getInt("gameId"));

        var arr = jsonObject.getJSONArray("params");

        for (int i = 0; i < arr.length(); ++i)
        {
            String comment = null;

            if (arr.getJSONObject(i).has("comment"))
                comment = arr.getJSONObject(i).getString("comment");

            result.add(
                new GameMove(
                    game,
                    new Move(
                        0,
                        Figure.values()[arr.getJSONObject(i).getInt("figure")],
                        arr.getJSONObject(i).getString("startCell"),
                        arr.getJSONObject(i).getString("endCell")
                    ),
                    arr.getJSONObject(i).getInt("number"),
                    comment
                )
            );
        }
        
        return result;
    }

    void performTask(String task) throws CHWCDBException
    {
        try
        {
            var jsonObject = new JSONObject(task);

            String type = jsonObject.getString("type");
            String op = jsonObject.getString("op");

            if (type.equals("gameMove"))
            {
                if (op.equals("remove"))
                    removeMove(getRemovingGameMoveFromJSON(jsonObject));
                else if (op.equals("insertMany"))
                    addMoves(getGameMovesFromJSON(jsonObject));
            }
            else if (type.equals("game"))
            {
                if (op.equals("insert"))
                    addGame(getGameFromJSON(jsonObject));
                else if (op.equals("remove"))
                    removeGame(new Game(jsonObject.getInt("id")));
                else if (op.equals("end"))
                {
                    Integer id = jsonObject.getInt("id");
                    Integer duration = jsonObject.getInt("duration");
                    GameResult result = GameResult.values()[jsonObject.getInt("result")];

                    var game = new Game(id);
                    game.setDuration(duration);
                    game.setResult(result);

                    endGame(game);
                }
            }
        }
        catch (Exception e)
        {
            throw new CHWCDBException("Task queue running error: " + e.getMessage());
        }
    }

    @Override
    public void runTaskQueue() throws CHWCDBException
    {
        String task = taskQueue.poll();

        while (task != null)
        {
            if (!task.isEmpty())
                performTask(task);
            
            task = taskQueue.poll();
        }
    }
}
