package entity;

public class GameMove
{
    private Game game;
    private Move move;
    private String comment;

    public GameMove() {}
    public GameMove(
        final Game game,
        final Move move,
        String comment
    )
    {
        this.game = game;
        this.move = move;
        this.comment = comment;
    }

    public void setGame(final Game game) { this.game = game; }
    public void setMove(final Move move) { this.move = move; }
    public void setComment(String comment) { this.comment = comment; }

    public Game getGame() { return game; }
    public Move getMove() { return move; }
    public String getComment() { return comment; }
}
