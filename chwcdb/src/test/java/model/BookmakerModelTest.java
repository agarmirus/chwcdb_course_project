package model;

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
import dao.IDAO;
import entity.Bet;
import entity.Game;
import entity.GameMove;
import entity.Move;
import entity.enums.BetStatus;
import entity.enums.BetType;
import entity.enums.Figure;
import entity.enums.GameResult;
import appexception.*;

public class BookmakerModelTest
{
    @Tested BookmakerModel bookmakerModel;

    @Injectable IDAO<GameMove> gameMoveDAO;
    @Injectable IDAO<Bet> betDAO;

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

        var optResult = bookmakerModel.getGameInfo(game);
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

        var optResult = bookmakerModel.getGameInfo(game);
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

        bookmakerModel.setGameMoveDAO(null);
        bookmakerModel.getGameInfo(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void getGameInfoNullGameTest() throws Exception
    {
        bookmakerModel.getGameInfo(null);
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

        bookmakerModel.getGameInfo(game);
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

        bookmakerModel.getGameInfo(game);
    }

    /*
    --------------------------
        addBet tests
    --------------------------
    */
    @Test
    public void addBetElementaryValidTest() throws Exception
    {
        int betId = 1;
        BetType type = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 1;

        var bet = new Bet(betId, type, condition, coefficient, gameId);

        bookmakerModel.addBet(bet);

        new Verifications()
        {{
            betDAO.create(bet);
            times = 1;
        }};
    }

    @Test
    public void addBetExpressValidTest() throws Exception
    {
        int betId = 1;
        BetType type = BetType.EXPRESS;

        var elemBet1 = new Bet(1, BetType.ELEMENTARY, "1", 1.4, 1);
        var elemBet2 = new Bet(2, BetType.ELEMENTARY, "1", 2.5, 13);

        List<Bet> elemBets = new ArrayList<Bet>();
        elemBets.add(elemBet1);
        elemBets.add(elemBet2);

        var bet = new Bet(betId, type, elemBets);

        bookmakerModel.addBet(bet);

        new Verifications()
        {{
            betDAO.create(bet);
            times = 1;
        }};
    }

    @Test
    public void addBetSystemValidTest() throws Exception
    {
        int betId = 1;
        BetType type = BetType.SYSTEM;

        var elemBet1 = new Bet(1, BetType.ELEMENTARY, "1", 1.4, 1);
        var elemBet2 = new Bet(2, BetType.ELEMENTARY, "1", 2.5, 13);
        var elemBet3 = new Bet(3, BetType.ELEMENTARY, "X", 2.5, 24);

        List<Bet> elemBets1 = new ArrayList<Bet>();
        elemBets1.add(elemBet1);
        elemBets1.add(elemBet2);
        
        List<Bet> elemBets2 = new ArrayList<Bet>();
        elemBets2.add(elemBet1);
        elemBets2.add(elemBet3);

        var express1 = new Bet(1, BetType.EXPRESS, elemBets1);
        var express2 = new Bet(2, BetType.EXPRESS, elemBets2);

        List<Bet> expresses = new ArrayList<Bet>();
        expresses.add(express1);
        expresses.add(express2);

        var bet = new Bet(betId, type, expresses);

        bookmakerModel.addBet(bet);

        new Verifications()
        {{
            betDAO.create(bet);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void addBetNullBetDAOTest() throws Exception
    {
        int betId = 1;
        BetType type = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 1;

        var bet = new Bet(betId, type, condition, coefficient, gameId);

        bookmakerModel.setBetDAO(null);
        bookmakerModel.addBet(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addBetNullBetTest() throws Exception
    {
        bookmakerModel.addBet(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addBetNullBetTypeTest() throws Exception
    {
        int betId = 1;
        BetType type = null;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 1;

        var bet = new Bet(betId, type, condition, coefficient, gameId);

        bookmakerModel.addBet(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addBetElementaryNullCondition() throws Exception
    {
        int betId = 1;
        BetType type = BetType.ELEMENTARY;
        String condition = null;
        double coefficient = 1.4;
        int gameId = 1;

        var bet = new Bet(betId, type, condition, coefficient, gameId);

        bookmakerModel.addBet(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addBetElementaryEmptyCondition() throws Exception
    {
        int betId = 1;
        BetType type = BetType.ELEMENTARY;
        String condition = "";
        double coefficient = 1.4;
        int gameId = 1;

        var bet = new Bet(betId, type, condition, coefficient, gameId);

        bookmakerModel.addBet(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addBetElementaryInvalidCoefTest() throws Exception
    {
        int betId = 1;
        BetType type = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 0.4;
        int gameId = 1;

        var bet = new Bet(betId, type, condition, coefficient, gameId);

        bookmakerModel.addBet(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addBetElementaryNegGameIdTest() throws Exception
    {
        int betId = 1;
        BetType type = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = -1;

        var bet = new Bet(betId, type, condition, coefficient, gameId);

        bookmakerModel.addBet(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addBetExpressNullElemTest() throws Exception
    {
        int betId = 1;
        BetType type = BetType.EXPRESS;

        var elemBet1 = new Bet(1, BetType.ELEMENTARY, "1", 1.4, 1);

        List<Bet> elemBets = new ArrayList<Bet>();
        elemBets.add(elemBet1);
        elemBets.add(null);

        var bet = new Bet(betId, type, elemBets);

        bookmakerModel.addBet(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addBetExpressEmptyTest() throws Exception
    {
        int betId = 1;
        BetType type = BetType.EXPRESS;

        List<Bet> elemBets = new ArrayList<Bet>();

        var bet = new Bet(betId, type, elemBets);

        bookmakerModel.addBet(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addBetSystemNullBetsTest() throws Exception
    {
        int betId = 1;
        BetType type = BetType.SYSTEM;

        var bet = new Bet(betId, type, null);

        bookmakerModel.addBet(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addBetSystemEmptyBetsTest() throws Exception
    {
        int betId = 1;
        BetType type = BetType.SYSTEM;

        List<Bet> expresses = new ArrayList<Bet>();

        var bet = new Bet(betId, type, expresses);

        bookmakerModel.addBet(bet);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void addBetFailedDataAccessTest() throws Exception
    {
        int betId = 1;
        BetType type = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 1;

        var bet = new Bet(betId, type, condition, coefficient, gameId);

        new Expectations()
        {{
            betDAO.create(bet);
            result = new CHWCDBDataAccessException();
        }};

        bookmakerModel.addBet(bet);
    }

    /*
    --------------------------
        removeBet tests
    --------------------------
    */
    @Test
    public void removeBetValidTest() throws Exception
    {
        int betId = 5;
        BetType betType = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 5;

        var bet = new Bet(
            betId,
            betType,
            condition,
            coefficient,
            gameId
        );

        bookmakerModel.removeBet(bet);

        new Verifications()
        {{
            betDAO.delete(bet);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void removeBetNullBetDAOTest() throws Exception
    {
        int betId = 5;
        BetType betType = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 5;

        var bet = new Bet(
            betId,
            betType,
            condition,
            coefficient,
            gameId
        );

        bookmakerModel.setBetDAO(null);
        bookmakerModel.removeBet(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeBetNullBetTest() throws Exception
    {
        bookmakerModel.removeBet(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeBetNegBetId() throws Exception
    {
        int betId = -5;
        BetType betType = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 5;

        var bet = new Bet(
            betId,
            betType,
            condition,
            coefficient,
            gameId
        );

        bookmakerModel.removeBet(bet);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void removeBetFailedDataAccessTest() throws Exception
    {
        int betId = 5;
        BetType betType = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 5;

        var bet = new Bet(
            betId,
            betType,
            condition,
            coefficient,
            gameId
        );

        new Expectations()
        {{
            betDAO.delete(bet);
            result = new CHWCDBDataAccessException();
        }};

        bookmakerModel.removeBet(bet);
    }

    /*
    --------------------------
       updateBetStatus tests
    --------------------------
    */
    @Test
    public void updateBetStatusValidTest() throws Exception
    {
        int betId = 5;
        BetType betType = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 5;

        var bet = new Bet(
            betId,
            betType,
            condition,
            coefficient,
            gameId
        );

        bet.setStatus(BetStatus.TRUE);

        bookmakerModel.updateBetStatus(bet);

        new Verifications()
        {{
            betDAO.update(bet, "status", bet.getStatus().ordinal());
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void updateBetStatusNullBetDAOTest() throws Exception
    {
        int betId = 5;
        BetType betType = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 5;

        var bet = new Bet(
            betId,
            betType,
            condition,
            coefficient,
            gameId
        );

        bet.setStatus(BetStatus.TRUE);

        bookmakerModel.setBetDAO(null);
        bookmakerModel.updateBetStatus(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void updateBetStatusNullBetTest() throws Exception
    {
        bookmakerModel.updateBetStatus(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void updateBetStatusNegIdTest() throws Exception
    {
        int betId = -5;
        BetType betType = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 5;

        var bet = new Bet(
            betId,
            betType,
            condition,
            coefficient,
            gameId
        );

        bet.setStatus(BetStatus.TRUE);

        bookmakerModel.updateBetStatus(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void updateBetStatusNullStatusTest() throws Exception
    {
        int betId = 5;
        BetType betType = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 5;

        var bet = new Bet(
            betId,
            betType,
            condition,
            coefficient,
            gameId
        );

        bet.setStatus(null);

        bookmakerModel.updateBetStatus(bet);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void updateBetStatusInvalidDataAccessTest() throws Exception
    {
        int betId = 5;
        BetType betType = BetType.ELEMENTARY;
        String condition = "1";
        double coefficient = 1.4;
        int gameId = 5;

        var bet = new Bet(
            betId,
            betType,
            condition,
            coefficient,
            gameId
        );

        bet.setStatus(BetStatus.TRUE);

        new Expectations()
        {{
            betDAO.update(bet, "status", bet.getStatus().ordinal());
            result = new CHWCDBDataAccessException();
        }};

        bookmakerModel.updateBetStatus(bet);
    }
}
