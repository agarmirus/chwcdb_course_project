package presenter;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

import appexception.CHWCDBInvalidInputException;
import entity.Game;
import entity.Move;
import entity.enums.GameResult;
import model.IModel;
import regulator.IRegulator;
import view.IView;

public class SpectatorPresenter implements IPresenter
{
    private IModel model;
    private IView view;
    private IRegulator regulator;
    private RQueue<String> taskQueue;

    public SpectatorPresenter(
        final IModel model,
        final IView view,
        final IRegulator regulator,
        final RedissonClient client
    )
    {
        this.model = model;
        this.view = view;
        this.regulator = regulator;

        taskQueue = client.getQueue("taskqueue");
    }

    void addGame() throws Exception
    {
        Integer id = view.getGameId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter game ID"
            );
        
        Integer round = view.getGameRound();

        if (round == null)
            throw new CHWCDBInvalidInputException(
                "Enter game round"
            );

        Integer number = view.getGameNumber();

        if (number == null)
            throw new CHWCDBInvalidInputException(
                "Enter game number"
            );
        
        Date date = view.getGameDate();

        if (date == null)
            throw new CHWCDBInvalidInputException(
                "Invalid game date"
            );

        Integer refereeId = view.getRefereeId();

        if (refereeId == null)
            throw new CHWCDBInvalidInputException(
                "Invalid referee ID"
            );

        Integer firstPlayerId = view.getFirstPlayerId();

        if (firstPlayerId == null)
            throw new CHWCDBInvalidInputException(
                "Invalid first player ID"
            );

        Integer secondPlayerId = view.getSecondPlayerId();

        if (secondPlayerId == null)
            throw new CHWCDBInvalidInputException(
                "Invalid second player ID"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("op", "insert");
        jsonObject.put("type", "game");
        jsonObject.put("id", id);
        jsonObject.put("round", round);
        jsonObject.put("number", number);
        jsonObject.put("date", new SimpleDateFormat("dd.MM.yyyy").format(date));
        jsonObject.put("refereeId", refereeId);
        jsonObject.put("firstPlayerId", firstPlayerId);
        jsonObject.put("secondPlayerId", secondPlayerId);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("New game has been added");
    }

    void removeGame() throws Exception
    {
        Integer id = view.getGameId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter game ID"
            );

        var jsonObject = new JSONObject();
        
        jsonObject.put("op", "remove");
        jsonObject.put("type", "game");
        jsonObject.put("id", id);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Game has been removed");
    }

    void endGame() throws Exception
    {
        Integer id = view.getGameId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter game ID"
            );

        Integer duration = view.getGameDuration();

        if (duration == null)
            throw new CHWCDBInvalidInputException(
                "Invalid game duration"
            );

        GameResult result = view.getGameResult();

        if (result == null)
            throw new CHWCDBInvalidInputException(
                "Invalid game result"
            );

        var jsonObject = new JSONObject();
        
        jsonObject.put("op", "end");
        jsonObject.put("type", "game");
        jsonObject.put("id", id);
        jsonObject.put("duration", duration);
        jsonObject.put("result", result.ordinal());

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Game has been ended");
    }

    void gameInfo() throws Exception
    {
        Integer id = view.getGameId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter game ID"
            );
        
        var optGameMoves = model.getGameInfo(new Game(id));

        if (optGameMoves.isEmpty())
            view.showInfoDialog("Game is not found");
        else
        {
            Game game = null;
            int count = 1;

            view.clearGameMovesTable();

            for (var gameMove: optGameMoves.get())
            {
                if (count == 1)
                    game = gameMove.getGame();

                Move move = gameMove.getMove();

                view.addGameMovesTableRow(
                    new Object[]
                    {
                        count++,
                        move.getFigure().name(),
                        move.getStartCell(),
                        move.getEndCell(),
                        gameMove.getComment()
                    }
                );
            }

            view.setGameId(game.getId());
            view.setGameRound(game.getRound());
            view.setGameNumber(game.getNumber());
            view.setGameDate(game.getDate());
            view.setRefereeId(game.getRefereeId());
            view.setFirstPlayerId(game.getFirstPlayerId());
            view.setSecondPlayerId(game.getSecondPlayerId());
            view.setGameDuration(game.getDuration());
            view.setGameResult(game.getResult());
        }
    }

    void removeMove() throws Exception
    {
        Integer gameId = view.getGameId();

        if (gameId == null)
            throw new CHWCDBInvalidInputException(
                "Enter game ID"
            );

        Integer moveNumber = view.getMoveNumber();

        if (moveNumber == null)
            throw new CHWCDBInvalidInputException(
                "Enter move number"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("op", "remove");
        jsonObject.put("type", "gameMove");
        jsonObject.put("gameId", gameId);
        jsonObject.put("moveNumber", moveNumber);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Move has been removed");
    }

    void addMoves() throws Exception
    {
        Integer gameId = view.getGameId();

        if (gameId == null)
            throw new CHWCDBInvalidInputException(
                "Enter game ID"
            );
        
        var gameMoves = view.getMoves();

        if (gameMoves == null)
            throw new CHWCDBInvalidInputException(
                "Invalid moves"
            );
        
        if (gameMoves.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter moves"
            );
        
        var jsonObject = new JSONObject();

        jsonObject.put("op", "insertMany");
        jsonObject.put("type", "gameMove");
        jsonObject.put("gameId", gameId);

        var arr = new JSONArray();
        
        for (var gameMove: gameMoves)
        {
            var elemJSONObject = new JSONObject();
            Move move = gameMove.getMove();

            elemJSONObject.put("figure", move.getFigure().ordinal());
            elemJSONObject.put("startCell", move.getStartCell());
            elemJSONObject.put("endCell", move.getEndCell());
            elemJSONObject.put("comment", gameMove.getComment());
            elemJSONObject.put("number", gameMove.getNumber());

            arr.put(elemJSONObject);
        }

        jsonObject.put("params", arr);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Moves have been added");
    }

    void signOut() throws Exception
    {
        taskQueue.clear();
        view.closeWindow();
        regulator.changeUser();
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        try
        {
            String command = event.getActionCommand();

            if (command.equals("addgame"))
                addGame();
            else if (command.equals("removegame"))
                removeGame();
            else if (command.equals("endgame"))
                endGame();
            else if (command.equals("gameinfo"))
                gameInfo();
            else if (command.equals("removemove"))
                removeMove();
            else if (command.equals("addmoves"))
                addMoves();
            else if (command.equals("signout"))
                signOut();
        }
        catch (Exception e)
        {
            view.showErrorDialog(e.getMessage());
        }
    }
}
