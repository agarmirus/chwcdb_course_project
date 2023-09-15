package entity;

public class GameMove
{
    private Game game;
    private Move move;
    private Integer number;
    private String comment;

    public GameMove() {}
    public GameMove(
        final Game game,
        final Move move,
        final Integer number,
        String comment
    )
    {
        this.game = game;
        this.move = move;
        this.number = number;
        this.comment = comment;
    }

    public void setGame(final Game game) { this.game = game; }
    public void setMove(final Move move) { this.move = move; }
    public void setNumber(final Integer number) { this.number = number; }
    public void setComment(String comment) { this.comment = comment; }

    public Game getGame() { return game; }
    public Move getMove() { return move; }
    public Integer getNumber() { return number; }
    public String getComment() { return comment; }
}
