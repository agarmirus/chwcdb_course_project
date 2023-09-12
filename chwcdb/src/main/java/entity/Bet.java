package entity;

import java.util.List;

import entity.enums.BetType;
import entity.enums.BetStatus;

public class Bet
{
    private Integer id;
    private BetType type;
    private String condition;
    private Double coefficient;
    private List<Bet> bets;
    private BetStatus status;
    private Integer gameId;

    public Bet() {}
    public Bet(
        final Integer id,
        final BetType type,
        String condition,
        final Double coefficient,
        final BetStatus status,
        final Integer gameId
    )
    {
        this.id = id;
        this.type = type;
        this.condition = condition;
        this.coefficient = coefficient;
        this.gameId = gameId;
        this.status = status;
    }
    public Bet(
        final Integer id,
        final BetType type,
        String condition,
        final Double coefficient,
        final Integer gameId
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
        final Integer id,
        final BetType type,
        final List<Bet> bets
    )
    {
        this.id = id;
        this.type = type;
        this.bets = bets;

        status = BetStatus.NONE;
    }

    public void setId(final Integer id) { this.id = id; }
    public void setType(final BetType type) { this.type = type; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setCoefficient(final Double coefficient) { this.coefficient = coefficient; }
    public void setBets(final List<Bet> bets) { this.bets = bets; }
    public void setStatus(final BetStatus status) { this.status = status; }
    public void setGameId(final Integer gameId) { this.gameId = gameId; }

    public Integer getId() { return id; }
    public BetType getType() { return type; }
    public String getCondition() { return condition; }
    public Double getCoefficient() { return coefficient; }
    public List<Bet> getBets() { return bets; }
    public BetStatus getStatus() { return status; }
    public Integer getGameId() { return gameId; }
}
