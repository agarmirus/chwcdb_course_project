package model;

import java.util.List;
import java.util.Optional;

import entity.*;
import dao.IDAO;
import appexception.CHWCDBException;

public abstract class IModel
{
    public void setUserDAO(final IDAO<User> dao) {}
    public void setBetDAO(final IDAO<Bet> dao) {}
    public void setGameDAO(final IDAO<Game> dao) {}
    public void setGameMoveDAO(final IDAO<GameMove> dao) {}
    public void setMoveDAO(final IDAO<Move> dao) {}
    public void setPlayerDAO(final IDAO<Player> dao) {}
    public void setRefereeDAO(final IDAO<Referee> dao) {}

    public IDAO<User> getUserDAO() { return null; }
    public IDAO<Bet> getBetDAO() { return null; }
    public IDAO<Game> getGameDAO() { return null; }
    public IDAO<GameMove> getGameMoveDAO() { return null; }
    public IDAO<Move> getMoveDAO() { return null; }
    public IDAO<Player> getPlayerDAO() { return null; }
    public IDAO<Referee> getRefereeDAO() { return null; }

    public Optional<User> authorize(final User user) throws CHWCDBException { return Optional.empty(); }
    
    public void addGame(final Game game) throws CHWCDBException {}
    public void removeGame(final Game game) throws CHWCDBException {}
    public void endGame(final Game game) throws CHWCDBException {}
    public Optional<List<GameMove>> getGameMoves(final Game game) throws CHWCDBException { return Optional.empty(); }

    public void addMoves(final List<GameMove> moves) throws CHWCDBException {}
    public void removeMove(final GameMove move) throws CHWCDBException {}

    public void addBet(final Bet bet, final Game game) throws CHWCDBException {}
    public void removeBet(final Bet bet) throws CHWCDBException {}
    public void updateBet(final Bet bet) throws CHWCDBException {}

    public void addPlayer(final Player player) throws CHWCDBException {}
    public void removePlayer(final Player player) throws CHWCDBException {}

    public void addUser(final User user) throws CHWCDBException {}
    public void removeUser(final User user) throws CHWCDBException {}
}
