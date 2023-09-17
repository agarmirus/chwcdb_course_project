package model.decorator;

import java.util.List;
import java.util.Optional;

import appexception.CHWCDBException;
import entity.*;
import logger.ILogger;
import model.*;

public class AdminModelLogDecorator extends IModel
{
    private IModel model;
    private ILogger logger;

    public AdminModelLogDecorator(
        final IModel model,
        final ILogger logger
    )
    {
        this.model = model;
        this.logger = logger;
        
        logger.setClass(AdminModel.class);
    }

    @Override
    public Optional<User> authorize(final User user) throws CHWCDBException
    {
        try
        {
            logger.info("authorizing");
            logger.trace("authorize(User) method called");
            var result = model.authorize(user);
            logger.trace("authorize(User) method successfully finished");

            return result;
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }
    
    @Override
    public void addGame(final Game game) throws CHWCDBException
    {
        try
        {
            logger.info("adding game");
            logger.trace("addGame(Game) method called");
            model.addGame(game);
            logger.trace("addGame(Game) method successfully finished");
            logger.info("game has been added");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeGame(final Game game) throws CHWCDBException
    {
        try
        {
            logger.info("removing game");
            logger.trace("removeGame(Game) method called finished");
            model.removeGame(game);
            logger.trace("removeGame(Game) method successfully finished");
            logger.info("game has been removed");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void endGame(final Game game) throws CHWCDBException
    {
        try
        {
            logger.info("ending game");
            logger.trace("endGame(Game) method called finished");
            model.endGame(game);
            logger.trace("endGame(Game) method successfully finished");
            logger.info("game has been ended");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<List<GameMove>> getGameInfo(final Game game) throws CHWCDBException
    {
        try
        {
            logger.info("getting game info");
            logger.trace("getGameInfo(Game) method called");
            var result = model.getGameInfo(game);
            logger.trace("getGameInfo(Game) method successfully finished");
            logger.info("game info has been got");

            return result;
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void addMoves(final List<GameMove> moves) throws CHWCDBException
    {
        try
        {
            logger.info("adding moves");
            logger.trace("addMoves(List<GameMove>) method called finished");
            model.addMoves(moves);
            logger.trace("addMoves(List<GameMove>) method successfully finished");
            logger.info("moves have been added");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeMove(final GameMove move) throws CHWCDBException
    {
        try
        {
            logger.info("removing move");
            logger.trace("removeMove(GameMove) method called finished");
            model.removeMove(move);
            logger.trace("removeMove(GameMove) method successfully finished");
            logger.info("move has been removed");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void addBet(final Bet bet) throws CHWCDBException
    {
        try
        {
            logger.info("adding bet");
            logger.trace("addBet(Bet) method called finished");
            model.addBet(bet);
            logger.trace("addBet(Bet) method successfully finished");
            logger.info("bet has been added");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeBet(final Bet bet) throws CHWCDBException
    {
        try
        {
            logger.info("removing bet");
            logger.trace("removeBet(Bet) method called finished");
            model.removeBet(bet);
            logger.trace("removeBet(Bet) method successfully finished");
            logger.info("bet has been removed");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateBetStatus(final Bet bet) throws CHWCDBException
    {
        try
        {
            logger.info("updating bet status");
            logger.trace("updateBetStatus(Bet) method called finished");
            model.updateBetStatus(bet);
            logger.trace("updateBetStatus(Bet) method successfully finished");
            logger.info("bet status has been updated");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void addPlayer(final Player player) throws CHWCDBException
    {
        try
        {
            logger.info("adding player");
            logger.trace("addPlayer(Player) method called finished");
            model.addPlayer(player);
            logger.trace("addPlayer(Player) method successfully finished");
            logger.info("player has been added");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removePlayer(final Player player) throws CHWCDBException
    {
        try
        {
            logger.info("removing player");
            logger.trace("removePlayer(Player) method called finished");
            model.removePlayer(player);
            logger.trace("removePlayer(Player) method successfully finished");
            logger.info("player has been removed");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void addUser(final User user) throws CHWCDBException
    {
        try
        {
            logger.info("adding user");
            logger.trace("addUser(User) method called finished");
            model.addUser(user);
            logger.trace("addUser(User) method successfully finished");
            logger.info("user has been added");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeUser(final User user) throws CHWCDBException
    {
        try
        {
            logger.info("removing user");
            logger.trace("removeUser(User) method called finished");
            model.removeUser(user);
            logger.trace("removeUser(User) method successfully finished");
            logger.info("user has been removed");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void addReferee(final Referee referee) throws CHWCDBException
    {
        try
        {
            logger.info("adding referee");
            logger.trace("addReferee(Referee) method called finished");
            model.addReferee(referee);
            logger.trace("addReferee(Referee) method successfully finished");
            logger.info("referee has been added");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeReferee(final Referee referee) throws CHWCDBException
    {
        try
        {
            logger.info("removing referee");
            logger.trace("removeReferee(Referee) method called finished");
            model.removeReferee(referee);
            logger.trace("removeReferee(Referee) method successfully finished");
            logger.info("referee has been removed");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void runTaskQueue() throws CHWCDBException
    {
        try
        {
            logger.info("running tasks from task queue");
            logger.trace("runTaskQueue() method called finished");
            model.runTaskQueue();
            logger.trace("runTaskQueue() method successfully finished");
            logger.info("queue tasks completed");
        }
        catch (CHWCDBException e)
        {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void runTest() {}
}
