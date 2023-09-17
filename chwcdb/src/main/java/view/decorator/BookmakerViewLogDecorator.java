package view.decorator;

import java.util.Date;
import java.util.List;

import entity.*;
import entity.enums.*;
import logger.ILogger;
import presenter.IPresenter;
import view.*;

public class BookmakerViewLogDecorator extends IView
{
    private ILogger logger;
    private IView view;

    public BookmakerViewLogDecorator(
        final IView view,
        final ILogger logger
    )
    {
        this.view = view;
        this.logger = logger;

        logger.setClass(BookmakerView.class);
    }

    @Override
    public void setPresenterListener(final IPresenter listener)
    {
        logger.info("setting presenter for view");
        view.setPresenterListener(listener);
    }

    @Override
    public String getAuthLogin()
    {
        logger.info("getting text from auth login");

        return view.getAuthLogin();
    }

    @Override
    public String getAuthPassword()
    {
        logger.info("getting text from auth password");

        return view.getAuthPassword();
    }

    @Override
	public void addGameMovesTableRow(Object[] row)
    {
        logger.info("adding row into game moves table");

        view.addGameMovesTableRow(row);
    }

    @Override
    public void clearGameMovesTable()
    {
        logger.info("clearing game moves table");

        view.clearGameMovesTable();
    }

    @Override
	public Integer getGameId()
    {
        logger.info("getting game ID");

        return view.getGameId();
    }

    @Override
	public Integer getGameRound()
    {
        logger.info("getting game round");

        return view.getGameRound();
    }

    @Override
	public Integer getGameNumber()
    {
        logger.info("getting game number");

        return view.getGameNumber();
    }

    @Override
	public Date getGameDate()
    {
        logger.info("getting game date");

        return view.getGameDate();
    }

    @Override
	public Integer getFirstPlayerId()
    {
        logger.info("getting player first id");

        return view.getFirstPlayerId();
    }

    @Override
	public Integer getSecondPlayerId()
    {
        logger.info("getting player second id");

        return view.getSecondPlayerId();
    }

    @Override
	public Integer getGameDuration()
    {
        logger.info("getting game duration");

        return view.getGameDuration();
    }

    @Override
	public GameResult getGameResult()
    {
        logger.info("getting game result");

        return view.getGameResult();
    }

    @Override
	public Integer getBetId()
    {
        logger.info("getting bet ID");

        return view.getBetId();
    }

    @Override
	public BetType getBetType()
    {
        logger.info("getting bet type");

        return view.getBetType();
    }

    @Override
	public String getBetCondition()
    {
        logger.info("getting bet condition");

        return view.getBetCondition();
    }

    @Override
	public Double getBetCoefficient()
    {
        logger.info("getting bet coefficient");

        return view.getBetCoefficient();
    }

    @Override
	public BetStatus getBetStatus()
    {
        logger.info("getting bet status");

        return view.getBetStatus();
    }

    @Override
	public List<Bet> getEnclosureBets()
    {
        logger.info("getting enclosure bets");

        return view.getEnclosureBets();
    }

    @Override
	public Integer getPlayerId()
    {
        logger.info("getting player ID");

        return view.getPlayerId();
    }

    @Override
	public String getPlayerFirstName()
    {
        logger.info("getting player first name");

        return view.getPlayerFirstName();
    }

    @Override
	public String getPlayerSecondName()
    {
        logger.info("getting player second name");

        return view.getPlayerSecondName();
    }

    @Override
	public String getPlayerThirdName()
    {
        logger.info("getting player third name");

        return view.getPlayerThirdName();
    }

    @Override
	public Date getPlayerBirthDate()
    {
        logger.info("getting player birth date");

        return view.getPlayerBirthDate();
    }

    @Override
	public String getPlayerCountry()
    {
        logger.info("getting player country");

        return view.getPlayerCountry();
    }

    @Override
	public Integer getPlayerRaiting()
    {
        logger.info("getting player raiting");

        return view.getPlayerRaiting();
    }

    @Override
    public Integer getRefereeId()
    {
        logger.info("getting referee ID");

        return view.getRefereeId();
    }

    @Override
	public String getRefereeFirstName()
    {
        logger.info("getting referee first name");

        return view.getRefereeFirstName();
    }

    @Override
	public String getRefereeSecondName()
    {
        logger.info("getting referee second name");

        return view.getRefereeSecondName();
    }

    @Override
	public String getRefereeThirdName()
    {
        logger.info("getting referee third name");

        return view.getRefereeThirdName();
    }

    @Override
	public Date getRefereeBirthDate()
    {
        logger.info("getting referee birth date");

        return view.getRefereeBirthDate();
    }

    @Override
	public String getRefereeCountry()
    {
        logger.info("getting referee country");

        return view.getRefereeCountry();
    }

    @Override
	public String getUserLogin()
    {
        logger.info("getting user login");

        return view.getUserLogin();
    }

    @Override
	public String getUserPassword()
    {
        logger.info("getting user password");

        return view.getUserPassword();
    }

    @Override
	public UserRole getUserRole()
    {
        logger.info("getting user role");

        return view.getUserRole();
    }

    @Override
	public void setGameId(final Integer id)
    {
        logger.info("setting game ID");

        view.setGameId(id);
    }

    @Override
	public void setGameRound(final Integer round)
    {
        logger.info("setting game round");

        view.setGameRound(round);
    }

    @Override
	public void setGameNumber(final Integer number)
    {
        logger.info("setting game number");

        view.setGameNumber(number);
    }
    
    @Override
	public void setGameDate(final Date date)
    {
        logger.info("setting game date");

        view.setGameDate(date);
    }

    @Override
	public void setRefereeId(final Integer id)
    {
        logger.info("setting referee ID");

        view.setRefereeId(id);
    }

    @Override
	public void setFirstPlayerId(final Integer id)
    {
        logger.info("setting first player ID");

        view.setFirstPlayerId(id);
    }

    @Override
	public void setSecondPlayerId(final Integer id)
    {
        logger.info("setting second player ID");

        view.setSecondPlayerId(id);
    }

    @Override
	public void setGameDuration(final Integer duration)
    {
        logger.info("setting game duration");

        view.setGameDuration(duration);
    }

    @Override
	public void setGameResult(final GameResult result)
    {
        logger.info("setting game result");

        view.setGameResult(result);
    }

    @Override
	public Integer getMoveId()
    {
        logger.info("getting move ID");

        return view.getMoveId();
    }

    @Override
	public List<GameMove> getMoves()
    {
        logger.info("getting moves");

        return view.getMoves();
    }

    @Override
    public void closeWindow()
    {
        logger.info("closing window");

        view.closeWindow();
    }

    @Override
    public void showInfoDialog(String text)
    {
        logger.info("showing info dialog window");

        view.closeWindow();
    }

    @Override
    public void showErrorDialog(String text)
    {
        logger.info("showing error dialog window");

        view.closeWindow();
    }
}
