package view;

import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import entity.Bet;
import entity.GameMove;
import entity.enums.BetStatus;
import entity.enums.BetType;
import entity.enums.GameResult;
import presenter.IPresenter;

public abstract class IView extends JFrame
{
    protected void removeAllButtonListeners(JButton button)
    {
        for (var listener: button.getActionListeners())
            button.removeActionListener(listener);
    }

    public void setPresenterListener(final IPresenter listener)  {}

    public String getAuthLogin() { return null; }
    public String getAuthPassword() { return null; }

	public void addGameMovesTableRow(Object[] row) {}
    public void clearGameMovesTable() {}

	public Integer getGameId() { return null; }
	public Integer getGameRound() { return null; }
	public Integer getGameNumber() { return null; }
	public Date getGameDate() { return null; }
	public Integer getFirstPlayerId() { return null; }
	public Integer getSecondPlayerId() { return null; }
	public Integer getGameDuration() { return null; }
	public GameResult getGameResult() { return null; }

	public Integer getBetId() { return null; }
	public BetType getBetType() { return null; }
	public String getBetCondition() { return null; }
	public Double getBetCoefficient() { return null; }
	public BetStatus getBetStatus() { return null; }
	public List<Bet> getEnclosureBets() { return null; }

	public Integer getPlayerId() { return null; }
	public String getPlayerFirstName() { return null; }
	public String getPlayerSecondName() { return null; }
	public String getPlayerThirdName() { return null; }
	public Date getPlayerBirthDate() { return null; }
	public String getPlayerCountry() { return null; }
	public Integer getPlayerRaiting() { return null; }

    public Integer getRefereeId() { return null; }
	public String getRefereeFirstName() { return null; }
	public String getRefereeSecondName() { return null; }
	public String getRefereeThirdName() { return null; }
	public Date getRefereeBirthDate() { return null; }
	public String getRefereeCountry() { return null; }

	public void setGameId(final Integer id) {}
	public void setGameRound(final Integer round) {}
	public void setGameNumber(final Integer number) {}
	public void setGameDate(final Date date) {}
	public void setRefereeId(final Integer id) {}
	public void setFirstPlayerId(final Integer id) {}
	public void setSecondPlayerId(final Integer id) {}
	public void setGameDuration(final Integer duration) {}
	public void setGameResult(final GameResult result) {}

	public Integer getMoveId() { return null; }
	public List<GameMove> getMoves() { return null; }

    public void closeWindow()
    {
        dispose();
    }

    public void showInfoDialog(String text)
    {
        JOptionPane.showMessageDialog(this, text, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorDialog(String text)
    {
        JOptionPane.showMessageDialog(this, text, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
