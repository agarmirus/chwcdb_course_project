package presenter;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

import appexception.CHWCDBInvalidInputException;
import entity.Game;
import entity.Move;
import entity.enums.UserRole;
import model.IModel;
import regulator.IRegulator;
import view.IView;

public class AdminPresenter implements IPresenter
{
    private IModel model;
    private IView view;
    private IRegulator regulator;
    private RQueue<String> taskQueue;

    public AdminPresenter(
        final IModel model,
        final IView view,
        final IRegulator regulator,
        final RedissonClient client
    )
    {
        this.model = model;
        this.view = view;
        this.regulator = regulator;

        taskQueue = client.getQueue("taskqueue");
    }

    void gameInfo() throws Exception
    {
        Integer id = view.getGameId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter game ID"
            );
        
        var optGameMoves = model.getGameInfo(new Game(id));

        if (optGameMoves.isEmpty())
            view.showInfoDialog("Game is not found");
        else
        {
            Game game = null;
            int count = 1;

            view.clearGameMovesTable();

            for (var gameMove: optGameMoves.get())
            {
                if (count == 1)
                    game = gameMove.getGame();

                Move move = gameMove.getMove();

                view.addGameMovesTableRow(
                    new Object[]
                    {
                        count++,
                        move.getFigure().name(),
                        move.getStartCell(),
                        move.getEndCell(),
                        gameMove.getComment()
                    }
                );
            }

            view.setGameId(game.getId());
            view.setGameRound(game.getRound());
            view.setGameNumber(game.getNumber());
            view.setGameDate(game.getDate());
            view.setRefereeId(game.getRefereeId());
            view.setFirstPlayerId(game.getFirstPlayerId());
            view.setSecondPlayerId(game.getSecondPlayerId());
            view.setGameDuration(game.getDuration());
            view.setGameResult(game.getResult());
        }
    }

    void removeBet() throws Exception
    {
        Integer id = view.getBetId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter bet ID"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("type", "bet");
        jsonObject.put("op", "insert");
        jsonObject.put("id", id);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Bet has been removed");
    }

    void removeGame() throws Exception
    {
        Integer id = view.getGameId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter game ID"
            );

        var jsonObject = new JSONObject();
        
        jsonObject.put("op", "remove");
        jsonObject.put("type", "game");
        jsonObject.put("id", id);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Game has been removed");
    }

    void addPlayer() throws Exception
    {
        Integer id = view.getPlayerId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter player ID"
            );

        String firstName = view.getPlayerFirstName();

        if (firstName.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter player first name"
            );
        
        String secondName = view.getPlayerSecondName();

        if (secondName.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter player second name"
            );

        String thirdName = view.getPlayerThirdName();

        if (thirdName.isEmpty())
            thirdName = null;

        Date birthDate = view.getPlayerBirthDate();

        if (birthDate == null)
            throw new CHWCDBInvalidInputException(
                "Invalid player birth date"
            );
        
        String country = view.getPlayerCountry();

        if (country.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter player country"
            );
        
        Integer raiting = view.getPlayerRaiting();

        if (raiting == null)
            throw new CHWCDBInvalidInputException(
                "Enter player raiting"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("op", "insert");
        jsonObject.put("type", "player");
        jsonObject.put("id", id);
        jsonObject.put("firstName", firstName);
        jsonObject.put("secondName", secondName);
        jsonObject.put("thirdName", thirdName);
        jsonObject.put("birthDate", new SimpleDateFormat("dd.MM.yyyy").format(birthDate));
        jsonObject.put("country", country);
        jsonObject.put("raiting", raiting);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Player has been added");
    }

    void removePlayer() throws Exception
    {
        Integer id = view.getPlayerId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter player ID"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("op", "remove");
        jsonObject.put("type", "player");
        jsonObject.put("id", id);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Player has been removed");
    }

    void addReferee() throws Exception
    {
        Integer id = view.getRefereeId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter referee ID"
            );

        String firstName = view.getRefereeFirstName();

        if (firstName.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter referee first name"
            );
        
        String secondName = view.getRefereeSecondName();

        if (secondName.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter referee second name"
            );

        String thirdName = view.getRefereeThirdName();

        if (thirdName.isEmpty())
            thirdName = null;

        Date birthDate = view.getRefereeBirthDate();

        if (birthDate == null)
            throw new CHWCDBInvalidInputException(
                "Invalid referee birth date"
            );
        
        String country = view.getRefereeCountry();

        if (country.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter referee country"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("op", "insert");
        jsonObject.put("type", "referee");
        jsonObject.put("id", id);
        jsonObject.put("firstName", firstName);
        jsonObject.put("secondName", secondName);
        jsonObject.put("thirdName", thirdName);
        jsonObject.put("birthDate", new SimpleDateFormat("dd.MM.yyyy").format(birthDate));
        jsonObject.put("country", country);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Referee has been added");
    }

    void removeReferee() throws Exception
    {
        Integer id = view.getRefereeId();

        if (id == null)
            throw new CHWCDBInvalidInputException(
                "Enter referee ID"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("op", "remove");
        jsonObject.put("type", "referee");
        jsonObject.put("id", id);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("Referee has been removed");
    }

    void addUser() throws Exception
    {
        String login = view.getUserLogin();

        if (login.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter user login"
            );

        String pswd = view.getUserPassword();

        if (pswd.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter user password"
            );

        pswd = DigestUtils.sha1Hex(pswd);

        UserRole role = view.getUserRole();

        if (role == null)
            throw new CHWCDBInvalidInputException(
                "Invalid user role"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("op", "insert");
        jsonObject.put("type", "user");
        jsonObject.put("login", login);
        jsonObject.put("hashedPswd", pswd);
        jsonObject.put("role", role.ordinal());

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("User has been added");
    }

    void removeUser() throws Exception
    {
        String login = view.getUserLogin();

        if (login.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter user login"
            );

        var jsonObject = new JSONObject();

        jsonObject.put("op", "remove");
        jsonObject.put("type", "user");
        jsonObject.put("login", login);

        taskQueue.add(jsonObject.toString());

        model.runTaskQueue();

        view.showInfoDialog("User has been removed");
    }
    
    void signOut() throws Exception
    {
        taskQueue.clear();
        view.closeWindow();
        regulator.changeUser();
    }

    void test()
    {}

    @Override
    public void actionPerformed(ActionEvent event)
    {
        try
        {
            String command = event.getActionCommand();

            if (command.equals("gameinfo"))
                gameInfo();
            else if (command.equals("reomvegame"))
                removeGame();
            else if (command.equals("removebet"))
                removeBet();
            else if (command.equals("addplayer"))   
                addPlayer();
            else if (command.equals("removeplayer"))
                removePlayer();
            else if (command.equals("addreferee"))
                addReferee();
            else if (command.equals("removereferee"))
                removeReferee();
            else if (command.equals("adduser"))
                addUser();
            else if (command.equals("removeuser"))
                removeUser();
            else if (command.equals("test"))
                test();
            else if (command.equals("signout"))
                signOut();
        }
        catch (Exception e)
        {
            view.showErrorDialog(e.getMessage());
        }
    }
}
