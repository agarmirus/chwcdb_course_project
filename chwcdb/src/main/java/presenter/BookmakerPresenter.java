package presenter;

import java.awt.event.ActionEvent;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

import appexception.CHWCDBInvalidInputException;
import entity.Bet;
import entity.Game;
import entity.Move;
import entity.enums.BetStatus;
import entity.enums.BetType;
import model.IModel;
import regulator.IRegulator;
import view.IView;

public class BookmakerPresenter implements IPresenter
{
    private IModel model;
    private IView view;
    private IRegulator regulator;
    private RQueue<String> taskQueue;

    public BookmakerPresenter(
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

    void addBet() throws Exception
    {
        Integer id = view.getBetId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter bet ID"
            );
        
        BetType type = view.getBetType();

        if (type == null)
            throw new CHWCDBInvalidInputException(
                "Enter bet type"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("type", "bet");
        jsonObject.put("op", "insert");
        jsonObject.put("id", id);
        jsonObject.put("betType", type.ordinal());
        
        if (type == BetType.ELEMENTARY)
        {
            String condition = view.getBetCondition();

            if (condition.isEmpty())
                condition = null;
            
            Double coefficient = view.getBetCoefficient();

            if (coefficient == null)
                throw new CHWCDBInvalidInputException(
                    "Enter bet coefficient"
                );
            
            Integer gameId = view.getGameId();

            if (gameId == null)
                throw new CHWCDBInvalidInputException(
                    "Enter game ID"
                );
            
            jsonObject.put("condition", condition);
            jsonObject.put("coefficient", coefficient);
            jsonObject.put("gameId", gameId);
        }
        else
        {
            List<Bet> bets = view.getEnclosureBets();

            if (bets == null)
                throw new CHWCDBInvalidInputException(
                    "Invalid enclosure bets IDs"
                );

            if (bets.isEmpty())
                throw new CHWCDBInvalidInputException(
                    "Enter enclosure bets IDs"
                );

            var arr = new JSONArray();

            for (var bet: bets)
            {
                var elemJSONObject = new JSONObject();

                elemJSONObject.put("id", bet.getId());

                arr.put(elemJSONObject);
            }

            jsonObject.put("bets", arr);
        }

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Bet has been addded");
    }

    void removeBet() throws Exception
    {
        Integer id = view.getBetId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter bet ID"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("type", "bet");
        jsonObject.put("op", "remove");
        jsonObject.put("id", id);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Bet has been removed");
    }

    void betStatus() throws Exception
    {
        Integer id = view.getBetId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter bet ID"
            );

        BetStatus status = view.getBetStatus();

        if (status == null)
            throw new CHWCDBInvalidInputException(
                "Enter bet status"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("type", "bet");
        jsonObject.put("op", "update");
        jsonObject.put("id", id);
        jsonObject.put("status", status.ordinal());

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Bet status has been updated");
    }

    void signOut() throws Exception
    {
        taskQueue.clear();
        regulator.changeUser();
        view.closeWindow();
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        try
        {
            String command = event.getActionCommand();

            if (command.equals("gameinfo"))
                gameInfo();
            else if (command.equals("betstatus"))
                betStatus();
            else if (command.equals("addbet"))
                addBet();
            else if (command.equals("removebet"))
                removeBet();
            else if (command.equals("signout"))
                signOut();
        }
        catch (Exception e)
        {
            view.showErrorDialog(e.getMessage());
        }
    }
}
