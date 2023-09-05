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
import entity.Player;
import entity.Referee;
import entity.User;
import entity.Move;
import entity.enums.BetType;
import entity.enums.Figure;
import entity.enums.GameResult;
import entity.enums.UserRole;
import appexception.*;

public class AdminModelTest
{
    @Tested AdminModel adminModel;

    @Injectable IDAO<Game> gameDAO;
    @Injectable IDAO<GameMove> gameMoveDAO;
    @Injectable IDAO<Bet> betDAO;
    @Injectable IDAO<Player> playerDAO;
    @Injectable IDAO<User> userDAO;
    @Injectable IDAO<Referee> refereeDAO;

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

        var optResult = adminModel.getGameInfo(game);
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

        var optResult = adminModel.getGameInfo(game);
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

        adminModel.setGameMoveDAO(null);
        adminModel.getGameInfo(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void getGameInfoNullGameTest() throws Exception
    {
        adminModel.getGameInfo(null);
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

        adminModel.getGameInfo(game);
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

        adminModel.getGameInfo(game);
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

        adminModel.removeGame(game);

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

        adminModel.setGameDAO(null);
        adminModel.removeGame(game);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeGameNullGameTest() throws Exception
    {
        adminModel.removeGame(null);
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

        adminModel.removeGame(game);
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

        adminModel.removeGame(game);
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

        adminModel.removeBet(bet);

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

        adminModel.setBetDAO(null);
        adminModel.removeBet(bet);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeBetNullBetTest() throws Exception
    {
        adminModel.removeBet(null);
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

        adminModel.removeBet(bet);
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

        adminModel.removeBet(bet);
    }

    /*
    --------------------------
        addPlayer tests
    --------------------------
    */
    @Test
    public void addPlayerValidTest() throws Exception
    {
        int playerId = 1;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "Russia";
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.addPlayer(player);

        new Verifications()
        {{
            playerDAO.create(player);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void addPlayerNullPlayerDAOTest() throws Exception
    {
        int playerId = 1;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "Russia";
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.setPlayerDAO(null);
        adminModel.addPlayer(player);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addPlayerNullPlayerTest() throws Exception
    {
        adminModel.addPlayer(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addPlayerNullFirstNameTest() throws Exception
    {
        int playerId = 1;
        String firstName = null;
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "Russia";
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.addPlayer(player);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addPlayerEmptyFirstNameTest() throws Exception
    {
        int playerId = 1;
        String firstName = "";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "Russia";
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.addPlayer(player);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addPlayerNullSecondNameTest() throws Exception
    {
        int playerId = 1;
        String firstName = "aaa";
        String secondName = null;
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "Russia";
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.addPlayer(player);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addPlayerEmptySecondNameTest() throws Exception
    {
        int playerId = 1;
        String firstName = "aaa";
        String secondName = "";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "Russia";
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.addPlayer(player);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addPlayerNullThirdNameTest() throws Exception
    {
        int playerId = 1;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = null;
        Date birthDate = new Date(1);
        String country = "Russia";
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.addPlayer(player);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addPlayerEmptyThirdNameTest() throws Exception
    {
        int playerId = 1;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "";
        Date birthDate = new Date(1);
        String country = "Russia";
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.addPlayer(player);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addPlayerNullBirthDateTest() throws Exception
    {
        int playerId = 1;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = null;
        String country = "Russia";
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.addPlayer(player);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addPlayerNullCountryTest() throws Exception
    {
        int playerId = 1;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = null;
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.addPlayer(player);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addPlayerEmptyCountryTest() throws Exception
    {
        int playerId = 1;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "";
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.addPlayer(player);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addPlayerNegPlayerRaitingTest() throws Exception
    {
        int playerId = 1;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "Russia";
        int raiting = -1;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.addPlayer(player);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void addPlayerFailedDataAccessTest() throws Exception
    {
        int playerId = 1;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "Russia";
        int raiting = 1500;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        new Expectations()
        {{
            playerDAO.create(player);
            result = new CHWCDBDataAccessException();
        }};

        adminModel.addPlayer(player);
    }

    /*
    --------------------------
        removePlayer tests
    --------------------------
    */
    @Test
    public void removePlayerValidTest() throws Exception
    {
        int playerId = 10;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "USA";
        int raiting = 1000;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.removePlayer(player);

        new Verifications()
        {{
            playerDAO.delete(player);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void removePlayerNullPlayerDAOTest() throws Exception
    {
        int playerId = 10;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "USA";
        int raiting = 1000;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.setPlayerDAO(null);
        adminModel.removePlayer(player);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removePlayerNullPlayerTest() throws Exception
    {
        adminModel.removePlayer(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removePlayerNegPlayerIdTest() throws Exception
    {
        int playerId = -10;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "USA";
        int raiting = 1000;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        adminModel.removePlayer(player);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void removePlayerFailedDataAccessTest() throws Exception
    {
        int playerId = 10;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = new Date(1);
        String country = "USA";
        int raiting = 1000;

        var player = new Player(
            playerId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country,
            raiting
        );

        new Expectations()
        {{
            playerDAO.delete(player);
            result = new CHWCDBDataAccessException();
        }};

        adminModel.removePlayer(player);
    }

    /*
    --------------------------
        addUser tests
    --------------------------
    */
    @Test
    public void addUserVaildTest() throws Exception
    {
        String login = "aaa";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        adminModel.addUser(user);

        new Verifications()
        {{
            userDAO.create(user);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void addUserNullUserDAOTest() throws Exception
    {
        String login = "aaa";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        adminModel.setUserDAO(null);
        adminModel.addUser(user);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addUserNullUserTest() throws Exception
    {
        adminModel.addUser(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addUserNullLoginTest() throws Exception
    {
        String login = null;
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        adminModel.addUser(user);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addUserEmptyLoginTest() throws Exception
    {
        String login = "";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        adminModel.addUser(user);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addUserNullHashedPasswordTest() throws Exception
    {
        String login = "aaa";
        String hashedPswd = null;
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        adminModel.addUser(user);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addUserEmptyHashedPasswordTest() throws Exception
    {
        String login = "aaa";
        String hashedPswd = "";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        adminModel.addUser(user);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addUserNullRoleTest() throws Exception
    {
        String login = "aaa";
        String hashedPswd = "bbb";
        UserRole role = null;

        var user = new User(login, hashedPswd, role);

        adminModel.addUser(user);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void addUserFailedDataAccessTest() throws Exception
    {
        String login = "aaa";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        new Expectations()
        {{
            userDAO.create(user);
            result = new CHWCDBDataAccessException();
        }};

        adminModel.addUser(user);
    }

    /*
    --------------------------
        removeUser tests
    --------------------------
    */
    @Test
    public void removeUserValidTest() throws Exception
    {
        String login = "remove";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        adminModel.removeUser(user);

        new Verifications()
        {{
            userDAO.delete(user);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void removeUserNullUserDAOTest() throws Exception
    {
        String login = "remove";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        adminModel.setUserDAO(null);
        adminModel.removeUser(user);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeUserNullUserTest() throws Exception
    {
        adminModel.removeUser(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeUserNullLoginTest() throws Exception
    {
        String login = null;
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        adminModel.removeUser(user);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeUserEmptyLoginTest() throws Exception
    {
        String login = "";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        adminModel.removeUser(user);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void removeUserFailedDataAccessTest() throws Exception
    {
        String login = "remove";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        new Expectations()
        {{
            userDAO.delete(user);
            result = new CHWCDBDataAccessException();
        }};

        adminModel.removeUser(user);
    }

    /*
    --------------------------
        addReferee tests
    --------------------------
    */
    @Test
    public void addRefereeValidTest() throws Exception
    {
        int refereeId = 4;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "Russia";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.addReferee(referee);

        new Verifications()
        {{
            refereeDAO.create(referee);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void addRefereeNullRefereeDAOTest() throws Exception
    {
        int refereeId = 4;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "Russia";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.setRefereeDAO(null);
        adminModel.addReferee(referee);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addRefereeNullRefereeTest() throws Exception
    {
        adminModel.addReferee(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addRefereeNullFirstNameTest() throws Exception
    {
        int refereeId = 4;
        String firstName = null;
        String secondName = "bbb";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "Russia";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.addReferee(referee);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addRefereeEmptyFirstNameTest() throws Exception
    {
        int refereeId = 4;
        String firstName = "";
        String secondName = "bbb";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "Russia";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.addReferee(referee);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addRefereeNullSecondNameTest() throws Exception
    {
        int refereeId = 4;
        String firstName = "aaa";
        String secondName = null;
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "Russia";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.addReferee(referee);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addRefereeEmptySecondNameTest() throws Exception
    {
        int refereeId = 4;
        String firstName = "aaa";
        String secondName = "";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "Russia";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.addReferee(referee);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addRefereeNullThirdNameTest() throws Exception
    {
        int refereeId = 4;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = null;
        var birthDate = new Date(1);
        String country = "Russia";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.addReferee(referee);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addRefereeEmptyThirdNameTest() throws Exception
    {
        int refereeId = 4;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "";
        var birthDate = new Date(1);
        String country = "Russia";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.addReferee(referee);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addRefereeNullBirthDateTest() throws Exception
    {
        int refereeId = 4;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        Date birthDate = null;
        String country = "Russia";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.addReferee(referee);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addRefereeNullCountryTest() throws Exception
    {
        int refereeId = 4;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = null;

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.addReferee(referee);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void addRefereeEmptyCountryTest() throws Exception
    {
        int refereeId = 4;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.addReferee(referee);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void addRefereeFailedDataAccess() throws Exception
    {
        int refereeId = 4;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "Russia";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        new Expectations()
        {{
            refereeDAO.create(referee);
            result = new CHWCDBDataAccessException();
        }};

        adminModel.addReferee(referee);
    }

    /*
    --------------------------
        removeReferee tests
    --------------------------
    */
    public void removeRefereeValidTest() throws Exception
    {
        int refereeId = 10;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "USA";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.removeReferee(referee);

        new Verifications()
        {{
            refereeDAO.delete(referee);
            times = 1;
        }};
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void removeRefereeNullRefereeDAOTest() throws Exception
    {
        int refereeId = 10;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "USA";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );
        
        adminModel.setRefereeDAO(null);
        adminModel.removeReferee(referee);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void removeRefereeNegRefereeIdTest() throws Exception
    {
        int refereeId = -10;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "USA";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        adminModel.removeReferee(referee);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void removeRefereeFailedDataAccessTest() throws Exception
    {
        int refereeId = 10;
        String firstName = "aaa";
        String secondName = "bbb";
        String thirdName = "ccc";
        var birthDate = new Date(1);
        String country = "USA";

        var referee = new Referee(
            refereeId,
            firstName,
            secondName,
            thirdName,
            birthDate,
            country
        );

        new Expectations()
        {{
            refereeDAO.delete(referee);
            result = new CHWCDBDataAccessException();
        }};

        adminModel.removeReferee(referee);
    }
}
