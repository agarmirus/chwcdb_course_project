package model;

import java.util.List;
import java.util.Optional;

import entity.*;
import appexception.CHWCDBException;

public abstract class IModel
{
    public Optional<User> authorize(final User user) throws CHWCDBException { return Optional.empty(); }
    
    public void addGame(final Game game) throws CHWCDBException {}
    public void removeGame(final Game game) throws CHWCDBException {}
    public void endGame(final Game game) throws CHWCDBException {}
    public Optional<List<GameMove>> getGameMoves() throws CHWCDBException { return Optional.empty(); }

    public void addMove(final Move move, final Game game) throws CHWCDBException {}
    public void removeMove(final Move move, final Game game) throws CHWCDBException {}

    public void addBet(final Bet bet, final Game game) throws CHWCDBException {}
    public void removeBet(final Bet bet) throws CHWCDBException {}
    public void updateBet(final Bet bet) throws CHWCDBException {}

    public void addPlayer(final Player player) throws CHWCDBException {}
    public void removePlayer(final Player player) throws CHWCDBException {}

    public void addUser(final User user) throws CHWCDBException {}
    public void removeUser(final User user) throws CHWCDBException {}
}
