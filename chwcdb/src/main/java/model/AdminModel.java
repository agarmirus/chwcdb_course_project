package model;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

import dao.IDAO;
import entity.Bet;
import entity.Game;
import entity.GameMove;
import entity.Player;
import entity.Referee;
import entity.User;
import entity.enums.UserRole;
import appexception.*;

public class AdminModel extends IModel
{
    private IDAO<Game> gameDAO;
    private IDAO<GameMove> gameMoveDAO;
    private IDAO<Bet> betDAO;
    private IDAO<Player> playerDAO;
    private IDAO<User> userDAO;
    private IDAO<Referee> refereeDAO;

    private RQueue<String> taskQueue;

    public AdminModel() {}
    public AdminModel(
        final IDAO<Game> gameDAO,
        final IDAO<GameMove> gameMoveDAO,
        final IDAO<Bet> betDAO,
        final IDAO<Player> playerDAO,
        final IDAO<User> userDAO,
        final IDAO<Referee> refereeDAO,
        final RedissonClient client
    )
    {
        this.gameDAO = gameDAO;
        this.gameMoveDAO = gameMoveDAO;
        this.betDAO = betDAO;
        this.playerDAO = playerDAO;
        this.userDAO = userDAO;
        this.refereeDAO = refereeDAO;
        this.taskQueue = client.getQueue("taskqueue");
    }

    @Override
    public Optional<List<GameMove>> getGameInfo(final Game game) throws CHWCDBException
    {
        if (gameMoveDAO == null)
            throw new CHWCDBNullDAOException(
                "AdminModel.getGameMoves(Game): game move DAO is null"
            );
        
        if (game == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.getGameMoves(Game): game is null"
            );

        int gameId = game.getId();
        
        if (gameId < 1)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.getGameMoves(Game): game ID is less than 1"
            );
        
        var result = gameMoveDAO.get("game_id", Integer.toString(gameId));

        return result;
    }

    @Override
    public void removeGame(final Game game) throws CHWCDBException
    {
        if (gameDAO == null)
            throw new CHWCDBNullDAOException(
                "AdminModel.removeGame(Game): game DAO is null"
            );
        
        if (game == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.removeGame(Game): game is null"
            );
        
        if (game.getId() < 1)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.removeGame(Game): game ID is less than 1"
            );
        
        gameDAO.delete(game);
    }

    @Override
    public void removeBet(final Bet bet) throws CHWCDBException
    {
        if (betDAO == null)
            throw new CHWCDBNullDAOException(
                "AdminModel.removeBet(Bet): bet DAO is null"
            );

        if (bet == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.removeBet(Bet): bet is null"
            );
        
        if (bet.getId() < 1)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.removeBet(Bet): bet ID is less than 1"
            );
        
        betDAO.delete(bet);
    }

    @Override
    public void addPlayer(final Player player) throws CHWCDBException
    {
        if (playerDAO == null)
            throw new CHWCDBNullDAOException(
                "AdminModel.addPlayer(Player): player DAO is null"
            );
        
        if (player == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addPlayer(Player): player is null"
            );
        
        String firstName = player.getFirstName();

        if (firstName == null || firstName.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addPlayer(Player): player doesn't have a first name"
            );
        
        String secondName = player.getSecondName();

        if (secondName == null || secondName.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addPlayer(Player): player doesn't have a second name"
            );
        
        String thirdName = player.getThirdName();

        if (thirdName == null || thirdName.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addPlayer(Player): player doesn't have a third name"
            );
        
        if (player.getBirthDate() == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addPlayer(Player): player doesn't have a birth date"
            );
        
        String country = player.getCountry();
        
        if (country == null || country.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addPlayer(Player): player doesn't have a country"
            );
        
        if (player.getRaiting() < 0)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addPlayer(Player): player raiting is less than 0"
            );
        
        playerDAO.create(player);
    }

    @Override
    public void removePlayer(final Player player) throws CHWCDBException
    {
        if (playerDAO == null)
            throw new CHWCDBNullDAOException(
                "AdminModel.removePlayer(Player): player DAO is null"
            );
        
        if (player == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.removePlayer(Player): player is null"
            );
        
        if (player.getId() < 1)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.removePlayer(Player): player ID is less than 1"
            );
        
        playerDAO.delete(player);
    }

    @Override
    public void addUser(final User user) throws CHWCDBException
    {
        if (userDAO == null)
            throw new CHWCDBNullDAOException(
                "AdminModel.addUser(User): user DAO is null"
            );
        
        if (user == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addUser(User): user is null"
            );
        
        String login = user.getLogin();
        
        if (login == null || login.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addUser(User): user doesn't have a login"
            );
        
        String hashedPswd = user.getHashedPassword();
    
        if (hashedPswd == null || hashedPswd.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addUser(User): user doesn't have a hashed password"
            );
        
        if (user.getRole() == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addUser(User): user doesn't have a role"
            );
        
        userDAO.create(user);
    }

    @Override
    public void removeUser(final User user) throws CHWCDBException
    {
        if (userDAO == null)
            throw new CHWCDBNullDAOException(
                "AdminModel.removeUser(User): user DAO is null"
            );
        
        if (user == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.removeUser(User): user is null"
            );
        
        String login = user.getLogin();
        
        if (login == null || login.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "AdminModel.removeUser(User): user doesn't have a login"
            );
        
        userDAO.delete(user);
    }

    @Override
    public void addReferee(final Referee referee) throws CHWCDBException
    {
        if (refereeDAO == null)
            throw new CHWCDBNullDAOException(
                "AdminModel.addReferee(Referee): referee DAO is null"
            );

        if (referee == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addReferee(Referee): referee is null"
            );

        String firstName = referee.getFirstName();
        
        if (firstName == null || firstName.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addReferee(Referee): referee doesn't have a first name"
            );
        
        String secondName = referee.getSecondName();
    
        if (secondName == null || secondName.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addReferee(Referee): referee doesn't have a second name"
            );
        
        String thirdName = referee.getThirdName();

        if (thirdName == null || thirdName.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addReferee(Referee): referee doesn't have a third name"
            );
        
        if (referee.getBirthDate() == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addReferee(Referee): referee doesn't have a birth date"
            );

        String country = referee.getCountry();

        if (country == null || country.isEmpty())
            throw new CHWCDBInvalidParameterException(
                "AdminModel.addReferee(Referee): referee doesn't have a country"
            );
        
        refereeDAO.create(referee);
    }

    @Override
    public void removeReferee(final Referee referee) throws CHWCDBException
    {
        if (refereeDAO == null)
            throw new CHWCDBNullDAOException(
                "AdminModel.removeReferee(Referee): referee DAO is null"
            );

        if (referee == null)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.removeReferee(Referee): referee is null"
            );
        
        if (referee.getId() < 1)
            throw new CHWCDBInvalidParameterException(
                "AdminModel.removeReferee(Referee): referee ID is less than 1"
            );

        refereeDAO.delete(referee);
    }

    Referee getRefereeFromJSON(JSONObject jsonObject) throws Exception
    {
        return new Referee(
            jsonObject.getInt("id"),
            jsonObject.getString("firstName"),
            jsonObject.getString("secondName"),
            jsonObject.getString("thirdName"),
            new SimpleDateFormat("dd.MM.yyyy").parse(jsonObject.getString("birthDate")),
            jsonObject.getString("country")
        );
    }

    User getUserFromJSON(JSONObject jsonObject) throws Exception
    {
        return new User(
            jsonObject.getString("login"),
            jsonObject.getString("hashedPswd"),
            UserRole.values()[jsonObject.getInt("role")]
        );
    }

    Player getPlayerFromJSON(JSONObject jsonObject) throws Exception
    {
        return new Player(
            jsonObject.getInt("id"),
            jsonObject.getString("firstName"),
            jsonObject.getString("secondName"),
            jsonObject.getString("thirdName"),
            new SimpleDateFormat("dd.MM.yyyy").parse(jsonObject.getString("birthDate")),
            jsonObject.getString("country"),
            jsonObject.getInt("raiting")
        );
    }

    void performTask(String task) throws CHWCDBException
    {
        try
        {
            var jsonObject = new JSONObject(task);

            String type = jsonObject.getString("type");
            String op = jsonObject.getString("op");

            if (op.equals("insert"))
            {
                if (type.equals("referee"))
                    addReferee(getRefereeFromJSON(jsonObject));
                else if (type.equals("user"))
                    addUser(getUserFromJSON(jsonObject));
                else if (type.equals("player"))
                    addPlayer(getPlayerFromJSON(jsonObject));
            }
            else if (op.equals("remove"))
            {
                if (type.equals("game"))
                    removeGame(new Game(jsonObject.getInt("id")));
                else if (type.equals("bet"))
                    removeBet(new Bet(jsonObject.getInt("id")));
                else if (type.equals("player"))
                    removePlayer(new Player(jsonObject.getInt("id")));
                else if (type.equals("referee"))
                    removeReferee(new Referee(jsonObject.getInt("id")));
                else if (type.equals("user"))
                    removeUser(new User(jsonObject.getString("login")));
            }
        }
        catch (Exception e)
        {
            throw new CHWCDBException("Task queue running error: " + e.getMessage());
        }
    }

    @Override
    public void runTaskQueue() throws CHWCDBException
    {
        String task = taskQueue.poll();

        while (task != null)
        {
            if (!task.isEmpty())
                performTask(task);
            
            task = taskQueue.poll();
        }
    }
}
