package entity;

import java.util.Date;

import entity.enums.GameResult;

public class Game
{
    private int id;
    private int round;
    private int duration;
    private int number;
    private GameResult result;
    private Date date;
    private int refereeId;
    private int firstPlayerId;
    private int secondPlayerId;

    public Game() {}
    public Game(
        final int id,
        final int round,
        final int duration,
        final int number,
        final GameResult result,
        final Date date,
        final int refereeId,
        final int firstPlayerId,
        final int secondPlayerId
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

    public void setId(final int id) { this.id = id; }
    public void setRound(final int round) { this.round = round; }
    public void setDuration(final int duration) { this.duration = duration; }
    public void setNumber(final int number) { this.number = number; }
    public void setResult(final GameResult result) { this.result = result; }
    public void setDate(final Date date) { this.date = date; }
    public void setRefereeId(final int refereeId) { this.refereeId = refereeId; }
    public void setFirstPlayerId(final int firstPlayerId) { this.firstPlayerId = firstPlayerId; }
    public void setSecondPlayerId(final int secondPlayerId) { this.secondPlayerId = secondPlayerId; }

    public int getId() { return id; }
    public int getRound() { return round; }
    public int getDuration() { return duration; }
    public int getNumber() { return number; }
    public GameResult getResult() { return result; }
    public Date getDate() { return date; }
    public int getRefereeId() { return refereeId; }
    public int getFirstPlayerId() { return firstPlayerId; }
    public int getSecondPlayerId() { return secondPlayerId; }
}
