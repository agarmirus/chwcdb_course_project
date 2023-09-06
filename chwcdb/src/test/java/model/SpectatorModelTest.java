package model;

import dao.IDAO;
import entity.Game;
import entity.GameMove;
import entity.Move;
import entity.enums.Figure;
import entity.enums.GameResult;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import static org.junit.Assert.*;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;

import appexception.*;

public class SpectatorModelTest
{
    @Tested SpectatorModel spectatorModel;

    @Injectable IDAO<Game> gameDAO;
    @Injectable IDAO<GameMove> gameMoveDAO;

    /*
    --------------------------
        addGame tests
    --------------------------
    */
    @Test
    public void addGameValidTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.addGame(game);

        new Verifications()
        {{
            gameDAO.create(game);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void addGameNullGameDAOTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.setGameDAO(null);
        spectatorModel.addGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addGameNullGameTest() throws Exception
    {
        spectatorModel.addGame(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addGameNegRoundTest() throws Exception
    {
        int gameId = 1;
        int round = -1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.addGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addGameGreaterRoundTest() throws Exception
    {
        int gameId = 1;
        int round = 10;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.addGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addGameNegNumberTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = -2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.addGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addGameNullDateTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = null;
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.addGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addGameNegRefereeTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = -2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.addGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addGameNegFirstPlayerIdTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = -1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.addGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addGameNegSecondPlayerIdTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = -2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.addGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addGameSamePlayerIdTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 1;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.addGame(game);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void addGameFailedDataAccessTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        new Expectations()
        {{
            gameDAO.create(game);
            result = new CHWCDBDataAccessException();
        }};

        spectatorModel.addGame(game);
    }

    /*
    --------------------------
        removeGame tests
    --------------------------
    */
    @Test
    public void removeGameValidTest() throws Exception
    {
        int gameId = 2;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 3;
        int secondPlayerId = 4;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.removeGame(game);

        new Verifications()
        {{
            gameDAO.delete(game);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void removeGameNullGameDAOTest() throws Exception
    {
        int gameId = 2;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 3;
        int secondPlayerId = 4;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.setGameDAO(null);
        spectatorModel.removeGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeGameNullGameTest() throws Exception
    {
        spectatorModel.removeGame(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeGameNegGameIdTest() throws Exception
    {
        int gameId = -2;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 3;
        int secondPlayerId = 4;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.removeGame(game);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void removeGameFailedDataAccessTest() throws Exception
    {
        int gameId = 2;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 3;
        int secondPlayerId = 4;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        new Expectations()
        {{
            gameDAO.delete(game);
            result = new CHWCDBDataAccessException();
        }};

        spectatorModel.removeGame(game);
    }

    /*
    --------------------------
        endGame tests
    --------------------------
    */
    @Test
    public void endGameValidTest() throws Exception
    {
        int gameId = 2;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 3;
        int secondPlayerId = 4;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<Pair<String, String>> updates = new ArrayList<>();
        var durationUpdate = new Pair<String, String>(
            "duration",
            Integer.toString(duration)
        );
        var resultUpdate = new Pair<String, String>(
            "result",
            Integer.toString(result.ordinal())
        );
        updates.add(durationUpdate);
        updates.add(resultUpdate);

        spectatorModel.endGame(game);

        new Verifications()
        {{
            gameDAO.update(game, updates);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void endGameNullGameDAOTest() throws Exception
    {
        int gameId = 2;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 3;
        int secondPlayerId = 4;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.setGameDAO(null);
        spectatorModel.endGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void endGameNullGameTest() throws Exception
    {
        spectatorModel.endGame(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void endGameNegIdTest() throws Exception
    {
        int gameId = -2;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 3;
        int secondPlayerId = 4;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.endGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void endGameNegDurationTest() throws Exception
    {
        int gameId = 2;
        int round = 1;
        int duration = -300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 3;
        int secondPlayerId = 4;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );
        
        spectatorModel.endGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void endGameNullResultTest() throws Exception
    {
        int gameId = 2;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = null;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 3;
        int secondPlayerId = 4;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );
        
        spectatorModel.endGame(game);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void endGameFailedDataAccessTest() throws Exception
    {
        int gameId = 2;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 3;
        int secondPlayerId = 4;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<Pair<String, String>> updates = new ArrayList<>();
        var durationUpdate = new Pair<String, String>(
            "duration",
            Integer.toString(duration)
        );
        var resultUpdate = new Pair<String, String>(
            "result",
            Integer.toString(result.ordinal())
        );
        updates.add(durationUpdate);
        updates.add(resultUpdate);

        new Expectations()
        {{
            gameDAO.update(game, updates);
            result = new CHWCDBDataAccessException();
        }};

        spectatorModel.endGame(game);
    }

    /*
    --------------------------
        getGameInfo tests
    --------------------------
    */
    @Test
    public void getGameInfoValidTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B2", "B4"),
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        new Expectations()
        {{
            gameMoveDAO.get("game_id", Integer.toString(gameId));
            result = Optional.of(gameMoves);
        }};

        var optResult = spectatorModel.getGameInfo(game);
        assertNotNull(optResult);
        assertTrue(optResult.isPresent());

        var resultList = optResult.get();
        assertFalse(resultList.isEmpty());
        assertTrue(resultList.equals(gameMoves));
    }

    @Test
    public void getGameInfoValidEmptyTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        new Expectations()
        {{
            gameMoveDAO.get("game_id", Integer.toString(gameId));
            result = Optional.empty();
        }};

        var optResult = spectatorModel.getGameInfo(game);
        assertNotNull(optResult);
        assertTrue(optResult.isEmpty());
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void getGameInfoNullGameMoveDAOTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.setGameMoveDAO(null);
        spectatorModel.getGameInfo(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void getGameInfoNullGameTest() throws Exception
    {
        spectatorModel.getGameInfo(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void getGameInfoNegGameIdTest() throws Exception
    {
        int gameId = -1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        spectatorModel.getGameInfo(game);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void getGameInfoFailedDataAccessTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        new Expectations()
        {{
            gameMoveDAO.get("game_id", Integer.toString(gameId));
            result = new CHWCDBDataAccessException();
        }};

        spectatorModel.getGameInfo(game);
    }

    /*
    --------------------------
        addMoves tests
    --------------------------
    */
    @Test
    public void addMovesValidTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B2", "B4"),
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        spectatorModel.addMoves(gameMoves);

        new Verifications()
        {{
            gameMoveDAO.create(gameMoves);
            times = 1;
        }};
    }

    @Test
    public void addMovesEmptyValidTest() throws Exception
    {
        List<GameMove> gameMoves = new ArrayList<GameMove>();

        spectatorModel.addMoves(gameMoves);

        new Verifications()
        {{
            gameMoveDAO.create(gameMoves);
            times = 0;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void addMovesNullGameMovesDAOTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B2", "B4"),
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );
        
        spectatorModel.setGameMoveDAO(null);
        spectatorModel.addMoves(gameMoves);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addMovesNullGameMovesTest() throws Exception
    {
        spectatorModel.addMoves(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addMovesNullGameTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                null,
                new Move(1, Figure.PAWN, "B2", "B4"),
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        spectatorModel.addMoves(gameMoves);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addMovesNegMoveGameIdTest() throws Exception
    {
        int gameId = -1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B2", "B4"),
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        spectatorModel.addMoves(gameMoves);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addMovesNullMoveTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                game,
                null,
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        spectatorModel.addMoves(gameMoves);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addMovesNullGameMoveTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(null);
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        spectatorModel.addMoves(gameMoves);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addMovesNullFigureTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, null, "B2", "B4"),
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        spectatorModel.addMoves(gameMoves);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addMovesNullStartCellTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, null, "B4"),
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        spectatorModel.addMoves(gameMoves);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addMovesEmptyStartCellTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "", "B4"),
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        spectatorModel.addMoves(gameMoves);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addMovesNullEndCellTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B2", null),
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        spectatorModel.addMoves(gameMoves);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addMovesEmptyEndCellTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B2", ""),
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        spectatorModel.addMoves(gameMoves);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void addMovesFailedDataAccessTest() throws Exception
    {
        int gameId = 1;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        List<GameMove> gameMoves = new ArrayList<GameMove>();
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B2", "B4"),
                ""
            )
        );
        gameMoves.add(
            new GameMove(
                game,
                new Move(1, Figure.PAWN, "B7", "B6"),
                ""
            )
        );

        new Expectations()
        {{
            gameMoveDAO.create(gameMoves);
            result = new CHWCDBDataAccessException();
        }};

        spectatorModel.addMoves(gameMoves);
    }

    /*
    --------------------------
        addMoves tests
    --------------------------
    */
    @Test
    public void removeMoveValidTest() throws Exception
    {
        int gameId = 5;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        int moveId = 5;
        Figure figure = Figure.PAWN;
        String startCell = "B2";
        String endCell = "B4";

        var move = new Move(moveId, figure, startCell, endCell);

        String comment = "comment";

        var gameMove = new GameMove(game, move, comment);

        spectatorModel.removeMove(gameMove);

        new Verifications()
        {{
            gameMoveDAO.delete(gameMove);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void removeMoveNullGameMoveDAOTest() throws Exception
    {
        int gameId = 5;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        int moveId = 5;
        Figure figure = Figure.PAWN;
        String startCell = "B2";
        String endCell = "B4";

        var move = new Move(moveId, figure, startCell, endCell);

        String comment = "comment";

        var gameMove = new GameMove(game, move, comment);

        spectatorModel.setGameMoveDAO(null);
        spectatorModel.removeMove(gameMove);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeMoveNulGameMoveTest() throws Exception
    {
        spectatorModel.removeMove(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeMoveNullGameTest() throws Exception
    {
        int moveId = 5;
        Figure figure = Figure.PAWN;
        String startCell = "B2";
        String endCell = "B4";

        var move = new Move(moveId, figure, startCell, endCell);

        String comment = "comment";

        var gameMove = new GameMove(null, move, comment);

        spectatorModel.removeMove(gameMove);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeMoveNegGameIdTest() throws Exception
    {
        int gameId = -5;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        int moveId = 5;
        Figure figure = Figure.PAWN;
        String startCell = "B2";
        String endCell = "B4";

        var move = new Move(moveId, figure, startCell, endCell);

        String comment = "comment";

        var gameMove = new GameMove(game, move, comment);

        spectatorModel.removeMove(gameMove);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeMoveNullMoveTest() throws Exception
    {
        int gameId = 5;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        String comment = "comment";

        var gameMove = new GameMove(game, null, comment);

        spectatorModel.removeMove(gameMove);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeMoveNullFigureTest() throws Exception
    {
        int gameId = 5;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        int moveId = 5;
        Figure figure = null;
        String startCell = "B2";
        String endCell = "B4";

        var move = new Move(moveId, figure, startCell, endCell);

        String comment = "comment";

        var gameMove = new GameMove(game, move, comment);

        spectatorModel.removeMove(gameMove);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeMoveNullStartCellTest() throws Exception
    {
        int gameId = 5;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        int moveId = 5;
        Figure figure = Figure.PAWN;
        String startCell = null;
        String endCell = "B4";

        var move = new Move(moveId, figure, startCell, endCell);

        String comment = "comment";

        var gameMove = new GameMove(game, move, comment);

        spectatorModel.removeMove(gameMove);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeMoveEmptyStartCellTest() throws Exception
    {
        int gameId = 5;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        int moveId = 5;
        Figure figure = Figure.PAWN;
        String startCell = "";
        String endCell = "B4";

        var move = new Move(moveId, figure, startCell, endCell);

        String comment = "comment";

        var gameMove = new GameMove(game, move, comment);

        spectatorModel.removeMove(gameMove);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeMoveNullEndCellTest() throws Exception
    {
        int gameId = 5;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        int moveId = 5;
        Figure figure = Figure.PAWN;
        String startCell = "B2";
        String endCell = null;

        var move = new Move(moveId, figure, startCell, endCell);

        String comment = "comment";

        var gameMove = new GameMove(game, move, comment);

        spectatorModel.removeMove(gameMove);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeMoveEmptyEndCellTest() throws Exception
    {
        int gameId = 5;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        int moveId = 5;
        Figure figure = Figure.PAWN;
        String startCell = "B2";
        String endCell = "";

        var move = new Move(moveId, figure, startCell, endCell);

        String comment = "comment";

        var gameMove = new GameMove(game, move, comment);

        spectatorModel.removeMove(gameMove);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void removeMoveFailedDataAccessTest() throws Exception
    {
        int gameId = 5;
        int round = 1;
        int duration = 300;
        int number = 2;
        GameResult result = GameResult.FIRST;
        Date date = new Date(1);
        int refereeId = 2;
        int firstPlayerId = 1;
        int secondPlayerId = 2;

        var game = new Game(
            gameId,
            round,
            duration,
            number,
            result,
            date,
            refereeId,
            firstPlayerId,
            secondPlayerId
        );

        int moveId = 5;
        Figure figure = Figure.PAWN;
        String startCell = "B2";
        String endCell = "B4";

        var move = new Move(moveId, figure, startCell, endCell);

        String comment = "comment";

        var gameMove = new GameMove(game, move, comment);

        new Expectations()
        {{
            gameMoveDAO.delete(gameMove);
            result = new CHWCDBDataAccessException();
        }};

        spectatorModel.removeMove(gameMove);
    }
}
