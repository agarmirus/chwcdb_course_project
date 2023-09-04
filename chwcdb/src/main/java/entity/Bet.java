package entity;

import java.util.List;

import entity.enums.BetType;
import entity.enums.BetStatus;

public class Bet
{
    private int id;
    private BetType type;
    private String condition;
    private double coefficient;
    private List<Bet> bets;
    private BetStatus status;
    private int gameId;

    public Bet() {}
    public Bet(
        final int id,
        final BetType type,
        String condition,
        final double coefficient,
        final int gameId
    )
    {
        this.id = id;
        this.type = type;
        this.condition = condition;
        this.coefficient = coefficient;
        this.gameId = gameId;

        status = BetStatus.NONE;
    }

    public Bet(
        final int id,
        final BetType type,
        String condition,
        final List<Bet> bets,
        final double coefficient,
        final int gameId
    )
    {
        this.id = id;
        this.type = type;
        this.condition = condition;
        this.bets = bets;
        this.coefficient = coefficient;
        this.gameId = gameId;

        status = BetStatus.NONE;
    }

    public void setId(final int id) { this.id = id; }
    public void setType(final BetType type) { this.type = type; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setCoefficient(final double coefficient) { this.coefficient = coefficient; }
    public void setBets(final List<Bet> bets) { this.bets = bets; }
    public void setStatus(final BetStatus status) { this.status = status; }
    public void setGameId(final int gameId) { this.gameId = gameId; }

    public int getId() { return id; }
    public BetType getType() { return type; }
    public String getCondition() { return condition; }
    public double getCoefficient() { return coefficient; }
    public List<Bet> getBets() { return bets; }
    public BetStatus getStatus() { return status; }
    public int getGameId() { return gameId; }
}
