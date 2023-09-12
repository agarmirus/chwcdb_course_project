package entity;

import java.util.Date;

import entity.enums.GameResult;

public class Game
{
    private Integer id;
    private Integer round;
    private Integer duration;
    private Integer number;
    private GameResult result;
    private Date date;
    private Integer refereeId;
    private Integer firstPlayerId;
    private Integer secondPlayerId;

    public Game() {}
    public Game(
        final Integer id,
        final Integer round,
        final Integer duration,
        final Integer number,
        final GameResult result,
        final Date date,
        final Integer refereeId,
        final Integer firstPlayerId,
        final Integer secondPlayerId
    )
    {
        this.id = id;
        this.round = round;
        this.duration = duration;
        this.number = number;
        this.result = result;
        this.date = date;
        this.refereeId = refereeId;
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
    }

    public void setId(final Integer id) { this.id = id; }
    public void setRound(final Integer round) { this.round = round; }
    public void setDuration(final Integer duration) { this.duration = duration; }
    public void setNumber(final Integer number) { this.number = number; }
    public void setResult(final GameResult result) { this.result = result; }
    public void setDate(final Date date) { this.date = date; }
    public void setRefereeId(final Integer refereeId) { this.refereeId = refereeId; }
    public void setFirstPlayerId(final Integer firstPlayerId) { this.firstPlayerId = firstPlayerId; }
    public void setSecondPlayerId(final Integer secondPlayerId) { this.secondPlayerId = secondPlayerId; }

    public Integer getId() { return id; }
    public Integer getRound() { return round; }
    public Integer getDuration() { return duration; }
    public Integer getNumber() { return number; }
    public GameResult getResult() { return result; }
    public Date getDate() { return date; }
    public Integer getRefereeId() { return refereeId; }
    public Integer getFirstPlayerId() { return firstPlayerId; }
    public Integer getSecondPlayerId() { return secondPlayerId; }
}
