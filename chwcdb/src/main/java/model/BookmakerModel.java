package model;

import java.util.List;
import java.util.Optional;

import dao.IDAO;
import entity.Bet;
import entity.Game;
import entity.GameMove;
import entity.enums.BetStatus;
import entity.enums.BetType;
import appexception.*;

public class BookmakerModel extends IModel
{
    private IDAO<GameMove> gameMoveDAO;
    private IDAO<Bet> betDAO;

    private void checkElementary(final Bet bet) throws CHWCDBException
    {
        String condition = bet.getCondition();
        
        if (condition == null || condition.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.addBet(Bet): elementary bet doesn't have a condition"
            );
        
        if (bet.getCoefficient() <= 1)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.addBet(Bet): elementary bet coefficient is not greater than 1"
            );
        
        if (bet.getGameId() < 1)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.addBet(Bet): game ID is less than 1"
            );
            
        if (bet.getBets() != null)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.addBet(Bet): elementary bet has enclosures"
            );
    }

    private void checkExpress(final Bet bet) throws CHWCDBException
    {
        List<Bet> bets = bet.getBets();

        if (bets == null || bets.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.addBet(Bet): express doesn't have enclosures"
            );
        
        for (var elemBet: bets)
            if (elemBet == null)
                throw new CHWCDBInvalidParameterException(
                    "BookmakerModel.addBet(Bet): elementary bet is null"
                );
    }

    private void checkSystem(final Bet bet) throws CHWCDBException
    {
        List<Bet> bets = bet.getBets();

        if (bets == null || bets.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.addBet(Bet): system doesn't have enclosures"
            );
        
        for (var express: bets)
            if (express == null)
                throw new CHWCDBInvalidParameterException(
                    "BookmakerModel.addBet(Bet): express bet is null"
                );
    }

    public BookmakerModel() {}
    public BookmakerModel(
        final IDAO<GameMove> gameMoveDAO,
        final IDAO<Bet> betDAO
    )
    {
        this.gameMoveDAO = gameMoveDAO;
        this.betDAO = betDAO;
    }

    @Override
    public void setBetDAO(final IDAO<Bet> dao) { betDAO = dao; }

    @Override
    public void setGameMoveDAO(final IDAO<GameMove> dao) { gameMoveDAO = dao; }

    @Override
    public IDAO<Bet> getBetDAO() { return betDAO; }

    @Override
    public IDAO<GameMove> getGameMoveDAO() { return gameMoveDAO; }

    @Override
    public Optional<List<GameMove>> getGameInfo(final Game game) throws CHWCDBException
    {
        if (gameMoveDAO == null)
            throw new CHWCDBNullDAOException(
                "BookmakerModel.getGameMoves(Game): game move DAO is null"
            );
        
        if (game == null)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.getGameMoves(Game): game is null"
            );

        int gameId = game.getId();
        
        if (gameId < 1)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.getGameMoves(Game): game ID is less than 1"
            );
        
        var result = gameMoveDAO.get("game_id", Integer.toString(gameId));

        return result;
    }

    @Override
    public void addBet(final Bet bet) throws CHWCDBException
    {
        if (betDAO == null)
            throw new CHWCDBNullDAOException(
                "BookmakerModel.addBet(Bet): bet DAO is null"
            );

        if (bet == null)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.addBet(Bet): bet is null"
            );

        BetType type = bet.getType();
    
        if (type == null)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.addBet(Bet): bet doesn't have a type"
            );

        if (type == BetType.ELEMENTARY)
            checkElementary(bet);
        else if (type == BetType.EXPRESS)
            checkExpress(bet);
        else if (type == BetType.SYSTEM)
            checkSystem(bet);
        
        bet.setStatus(BetStatus.NONE);
        betDAO.create(bet);
    }

    @Override
    public void removeBet(final Bet bet) throws CHWCDBException
    {
        if (betDAO == null)
            throw new CHWCDBNullDAOException(
                "BookmakerModel.removeBet(Bet): bet DAO is null"
            );

        if (bet == null)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.removeBet(Bet): bet is null"
            );
        
        if (bet.getId() < 1)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.removeBet(Bet): bet ID is less than 1"
            );
        
        betDAO.delete(bet);
    }

    @Override
    public void updateBetStatus(final Bet bet) throws CHWCDBException
    {
        if (betDAO == null)
            throw new CHWCDBNullDAOException(
                "BookmakerModel.updateBetStatus(Bet): bet DAO is null"
            );

        if (bet == null)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.updateBetStatus(Bet): bet is null"
            );
        
        if (bet.getId() < 1)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.updateBetStatus(Bet): bet ID is less than 1"
            );

        BetStatus status = bet.getStatus();
        
        if (status == null)
            throw new CHWCDBInvalidParameterException(
                "BookmakerModel.updateBetStatus(Bet): bet doesn't have status"
            );

        betDAO.update(bet, "status", status.ordinal());
    }
}
