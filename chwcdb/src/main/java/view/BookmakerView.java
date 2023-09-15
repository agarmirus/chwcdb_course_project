package view;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.table.*;

import entity.enums.BetStatus;
import entity.enums.BetType;
import entity.enums.GameResult;
import presenter.IPresenter;

public class BookmakerView extends IView {
	public BookmakerView() {
		initComponents();
		setVisible(true);
	}

	private void initComponents() {
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		label5 = new JLabel();
		label6 = new JLabel();
		label7 = new JLabel();
		gameIdSpinner = new JSpinner();
		roundSpinner = new JSpinner();
		numberSpinner = new JSpinner();
		dateField = new JTextField();
		refereeIdSpinner = new JSpinner();
		firstPlayerIdSpinner = new JSpinner();
		secondPlayerIdSpinner = new JSpinner();
		label8 = new JLabel();
		durationSpinner = new JSpinner();
		label9 = new JLabel();
		resultBox = new JComboBox<>();
		gameInfoButton = new JButton();
		scrollPane1 = new JScrollPane();
		gameMovesTable = new JTable();
		label10 = new JLabel();
		betIdSpinner = new JSpinner();
		label11 = new JLabel();
		betTypeBox = new JComboBox<>();
		label12 = new JLabel();
		conditionField = new JTextField();
		coefSpinner = new JSpinner();
		label13 = new JLabel();
		label15 = new JLabel();
		enclBetsIdField = new JTextField();
		gameInfoButton2 = new JButton();
		label16 = new JLabel();
		betStatusBox = new JComboBox<>();
		setBetStatusButton = new JButton();
		gameInfoButton4 = new JButton();
		signOutButton = new JButton();

		//======== this ========
		Container contentPane = getContentPane();

		gameInfoButton.setActionCommand("gameinfo");
		signOutButton.setActionCommand("signout");
		setBetStatusButton.setActionCommand("betstatus");

		//---- label1 ----
		label1.setText("Game ID:");

		//---- label2 ----
		label2.setText("Round:");

		//---- label3 ----
		label3.setText("Number:");

		//---- label4 ----
		label4.setText("Date:");

		//---- label5 ----
		label5.setText("Referee ID:");

		//---- label6 ----
		label6.setText("1st Player ID:");

		//---- label7 ----
		label7.setText("2st Player ID:");

		//---- label8 ----
		label8.setText("Duration:");

		//---- label9 ----
		label9.setText("Result:");

		//---- resultBox ----
		resultBox.setModel(new DefaultComboBoxModel<>(new String[] {
			"Draw",
			"First Won",
			"Second Won"
		}));

		//---- gameInfoButton ----
		gameInfoButton.setText("Get Game Info");

		//======== scrollPane1 ========
		{

			//---- gameMovesTable ----
			gameMovesTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"\u2116", "Figure", "Start Cell", "End Cell", "Comment"
				}
			) {
				boolean[] columnEditable = new boolean[] {
					true, true, true, true, false
				};
				@Override
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return columnEditable[columnIndex];
				}
			});
			scrollPane1.setViewportView(gameMovesTable);
		}

		//---- label10 ----
		label10.setText("Bet ID:");

		//---- label11 ----
		label11.setText("Bet Type:");

		//---- betTypeBox ----
		betTypeBox.setModel(new DefaultComboBoxModel<>(new String[] {
			"Elementary",
			"Express",
			"System"
		}));

		//---- label12 ----
		label12.setText("Condition:");

		//---- coefSpinner ----
		coefSpinner.setModel(new SpinnerNumberModel(1.0, 1.0, null, 0.01));

		//---- label13 ----
		label13.setText("Coefficient:");

		//---- label15 ----
		label15.setText("Enclosure Bets IDs:");

		//---- gameInfoButton2 ----
		gameInfoButton2.setText("Add Bet");

		//---- label16 ----
		label16.setText("Bet Status:");

		//---- betStatusBox ----
		betStatusBox.setModel(new DefaultComboBoxModel<>(new String[] {
			"None",
			"Success",
			"Fail"
		}));

		//---- setBetStatusButton ----
		setBetStatusButton.setText("Set Bet Status");

		//---- gameInfoButton4 ----
		gameInfoButton4.setText("Remove Bet");

		//---- signOutButton ----
		signOutButton.setText("Sign Out");

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(44, 44, 44)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(label9)
								.addComponent(label8)
								.addComponent(label7)
								.addComponent(label6)
								.addComponent(label5)
								.addComponent(label4)
								.addComponent(label3)
								.addComponent(label2)
								.addComponent(label1))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(gameIdSpinner, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(roundSpinner, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(numberSpinner, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(dateField, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(refereeIdSpinner, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(firstPlayerIdSpinner, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(secondPlayerIdSpinner, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(durationSpinner, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(resultBox, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
							.addGap(18, 18, 18))
						.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(contentPaneLayout.createParallelGroup()
								.addComponent(signOutButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
								.addComponent(gameInfoButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
							.addGap(26, 26, 26)))
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 480, Short.MAX_VALUE)
							.addComponent(label16)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(betStatusBox, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(setBetStatusButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
							.addGap(26, 26, 26))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGroup(contentPaneLayout.createParallelGroup()
								.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 427, GroupLayout.PREFERRED_SIZE)
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGap(131, 131, 131)
									.addGroup(contentPaneLayout.createParallelGroup()
										.addComponent(gameInfoButton4, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
										.addComponent(gameInfoButton2, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(label10)
										.addComponent(label11)
										.addComponent(label12)
										.addComponent(label13))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(coefSpinner)
										.addComponent(betTypeBox)
										.addComponent(betIdSpinner)
										.addComponent(conditionField)))
								.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
									.addComponent(label15)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(enclBetsIdField, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)))
							.addGap(77, 77, 77))))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGap(192, 192, 192)
									.addComponent(gameInfoButton2)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(gameInfoButton4)
									.addGap(59, 59, 59))
								.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
									.addContainerGap()
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label10)
										.addComponent(betIdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label11)
										.addComponent(betTypeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label12)
										.addComponent(conditionField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(coefSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(label13))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label15)
										.addComponent(enclBetsIdField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(63, 63, 63)))
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label16)
								.addComponent(betStatusBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(setBetStatusButton)))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(40, 40, 40)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 446, GroupLayout.PREFERRED_SIZE)
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label1)
										.addComponent(gameIdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label2)
										.addComponent(roundSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label3)
										.addComponent(numberSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label4)
										.addComponent(dateField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label5)
										.addComponent(refereeIdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label6)
										.addComponent(firstPlayerIdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label7)
										.addComponent(secondPlayerIdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(18, 18, 18)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label8)
										.addComponent(durationSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label9)
										.addComponent(resultBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(18, 18, 18)
									.addComponent(gameInfoButton)
									.addGap(93, 93, 93)
									.addComponent(signOutButton)))))
					.addContainerGap(32, Short.MAX_VALUE))
		);
		pack();
		setLocationRelativeTo(getOwner());

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JLabel label5;
	private JLabel label6;
	private JLabel label7;
	private JSpinner gameIdSpinner;
	private JSpinner roundSpinner;
	private JSpinner numberSpinner;
	private JTextField dateField;
	private JSpinner refereeIdSpinner;
	private JSpinner firstPlayerIdSpinner;
	private JSpinner secondPlayerIdSpinner;
	private JLabel label8;
	private JSpinner durationSpinner;
	private JLabel label9;
	private JComboBox<String> resultBox;
	private JButton gameInfoButton;
	private JScrollPane scrollPane1;
	private JTable gameMovesTable;
	private JLabel label10;
	private JSpinner betIdSpinner;
	private JLabel label11;
	private JComboBox<String> betTypeBox;
	private JLabel label12;
	private JTextField conditionField;
	private JSpinner coefSpinner;
	private JLabel label13;
	private JLabel label15;
	private JTextField enclBetsIdField;
	private JButton gameInfoButton2;
	private JLabel label16;
	private JComboBox<String> betStatusBox;
	private JButton setBetStatusButton;
	private JButton gameInfoButton4;
	private JButton signOutButton;

	@Override
	public void setPresenterListener(final IPresenter listener)
	{
		removeAllButtonListeners(signOutButton);
		removeAllButtonListeners(gameInfoButton);
		removeAllButtonListeners(setBetStatusButton);

		gameInfoButton.addActionListener(listener);
		signOutButton.addActionListener(listener);
		setBetStatusButton.addActionListener(listener);
	}

	@Override
	public void addGameMovesTableRow(Object[] row)
	{
		DefaultTableModel model = (DefaultTableModel)gameMovesTable.getModel();
		model.addRow(row);
	}

	@Override
	public Integer getGameId()
	{
		return (Integer)gameIdSpinner.getValue();
	}

	@Override
	public Integer getGameRound()
	{
		return (Integer)roundSpinner.getValue();
	}

	@Override
	public Integer getGameNumber()
	{
		return (Integer)numberSpinner.getValue();
	}

	@Override
	public Date getGameDate()
	{
		try
		{
			return new SimpleDateFormat("dd.MM.yyyy").parse(dateField.getText());
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	@Override
	public Integer getRefereeId()
	{
		return (Integer)refereeIdSpinner.getValue();
	}

	@Override
	public Integer getFirstPlayerId()
	{
		return (Integer)firstPlayerIdSpinner.getValue();
	}

	@Override
	public Integer getSecondPlayerId()
	{
		return (Integer)secondPlayerIdSpinner.getValue();
	}

	@Override
	public Integer getGameDuration()
	{
		return (Integer)durationSpinner.getValue();
	}

	@Override
	public GameResult getGameResult()
	{
		return GameResult.values()[resultBox.getSelectedIndex()];
	}

	@Override
	public Integer getBetId()
	{
		return (Integer)betIdSpinner.getValue();
	}

	@Override
	public BetType getBetType()
	{
		return BetType.values()[betTypeBox.getSelectedIndex()];
	}

	@Override
	public String getBetCondition()
	{
		return conditionField.getText().strip();
	}

	@Override
	public Integer geBetCoefficient()
	{
		return (Integer)coefSpinner.getValue();
	}

	@Override
	public BetStatus getBetStatus()
	{
		return BetStatus.values()[betStatusBox.getSelectedIndex()];
	}

	@Override
	public void setGameId(final Integer id)
	{
		gameIdSpinner.setValue(id);
	}

	@Override
	public void setGameRound(final Integer round)
	{
		roundSpinner.setValue(round);
	}

	@Override
	public void setGameNumber(final Integer number)
	{
		numberSpinner.setValue(number);
	}

	@Override
	public void setGameDate(final Date date)
	{
		dateField.setText(new SimpleDateFormat("dd.MM.yyyy").format(date));
	}

	@Override
	public void setRefereeId(final Integer id)
	{
		refereeIdSpinner.setValue(id);
	}

	@Override
	public void setFirstPlayerId(final Integer id)
	{
		firstPlayerIdSpinner.setValue(id);
	}

	@Override
	public void setSecondPlayerId(final Integer id)
	{
		secondPlayerIdSpinner.setValue(id);
	}

	@Override
	public void setGameDuration(final Integer duration)
	{
		durationSpinner.setValue(duration);
	}

	@Override
	public void setGameResult(final GameResult result)
	{
		resultBox.setSelectedIndex(result.ordinal());
	}
}
