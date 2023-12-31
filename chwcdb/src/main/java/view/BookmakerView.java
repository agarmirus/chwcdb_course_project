package view;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.table.*;

import entity.Bet;
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
		label16 = new JLabel();
		betStatusBox = new JComboBox<>();
		setBetStatusButton = new JButton();
		signOutButton = new JButton();
		addBetButton = new JButton();
		removeBetButton = new JButton();

		//======== this ========
		Container contentPane = getContentPane();

		gameInfoButton.setActionCommand("gameinfo");
		setBetStatusButton.setActionCommand("betstatus");
		signOutButton.setActionCommand("signout");
		addBetButton.setActionCommand("addbet");
		removeBetButton.setActionCommand("removebet");

		//---- label1 ----
		label1.setText("ID игры:");

		//---- label2 ----
		label2.setText("Раунд:");

		//---- label3 ----
		label3.setText("Номер:");

		//---- label4 ----
		label4.setText("Дата:");

		//---- label5 ----
		label5.setText("ID судьи:");

		//---- label6 ----
		label6.setText("ID 1го игрока:");

		//---- label7 ----
		label7.setText("ID 2го игрока:");

		//---- label8 ----
		label8.setText("Длительность:");

		//---- label9 ----
		label9.setText("Результат:");

		//---- resultBox ----
		resultBox.setModel(new DefaultComboBoxModel<>(new String[] {
			"Ничья",
			"Победа первого",
			"Победа второго"
		}));

		//---- gameInfoButton ----
		gameInfoButton.setText("Инф. об игре");

		//======== scrollPane1 ========
		{

			//---- gameMovesTable ----
			gameMovesTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"\u2116", "Фигура", "Начальная клетка", "Конечная клетка", "Комментарий"
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
		label10.setText("ID ставки:");

		//---- label11 ----
		label11.setText("Тип ставки:");

		//---- betTypeBox ----
		betTypeBox.setModel(new DefaultComboBoxModel<>(new String[] {
			"Одинарная",
			"Экспресс",
			"Система"
		}));

		//---- label12 ----
		label12.setText("Условие:");

		//---- coefSpinner ----
		coefSpinner.setModel(new SpinnerNumberModel(1.0, 1.0, null, 0.01));

		//---- label13 ----
		label13.setText("Коэффициент:");

		//---- label15 ----
		label15.setText("ID вложенных ставок:");

		//---- label16 ----
		label16.setText("Статус ставки:");

		//---- betStatusBox ----
		betStatusBox.setModel(new DefaultComboBoxModel<>(new String[] {
			"Не известно",
			"Успех",
			"Провал"
		}));

		//---- setBetStatusButton ----
		setBetStatusButton.setText("Установить статус");

		//---- signOutButton ----
		signOutButton.setText("Выйти");

		//---- addBetButton ----
		addBetButton.setText("Добавить ставку");

		//---- removeBetButton ----
		removeBetButton.setText("Удалить ставку");

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
								.addComponent(gameInfoButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
								.addComponent(signOutButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
							.addGap(26, 26, 26)))
					.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 427, GroupLayout.PREFERRED_SIZE)
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
							.addGap(0, 51, Short.MAX_VALUE)
							.addComponent(label16)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(betStatusBox, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(setBetStatusButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
							.addGap(28, 28, 28))
						.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
									.addComponent(addBetButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
									.addGap(18, 18, 18)
									.addComponent(removeBetButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
									.addGap(38, 38, 38))
								.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addGroup(contentPaneLayout.createSequentialGroup()
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
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addComponent(label15)
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addComponent(enclBetsIdField, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)))
									.addGap(77, 77, 77))))))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(40, 40, 40)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
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
							.addComponent(signOutButton))
						.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 446, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(32, Short.MAX_VALUE))
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap(95, Short.MAX_VALUE)
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
						.addComponent(enclBetsIdField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label15))
					.addGap(42, 42, 42)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(removeBetButton)
						.addComponent(addBetButton))
					.addGap(86, 86, 86)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(label16)
						.addComponent(betStatusBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(setBetStatusButton))
					.addGap(96, 96, 96))
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
	private JLabel label16;
	private JComboBox<String> betStatusBox;
	private JButton setBetStatusButton;
	private JButton signOutButton;
	private JButton addBetButton;
	private JButton removeBetButton;

	@Override
	public void setPresenterListener(final IPresenter listener)
	{
		removeAllButtonListeners(signOutButton);
		removeAllButtonListeners(gameInfoButton);
		removeAllButtonListeners(setBetStatusButton);
		removeAllButtonListeners(addBetButton);
		removeAllButtonListeners(removeBetButton);

		gameInfoButton.addActionListener(listener);
		signOutButton.addActionListener(listener);
		setBetStatusButton.addActionListener(listener);
		removeBetButton.addActionListener(listener);
		addBetButton.addActionListener(listener);
	}

	@Override
	public void addGameMovesTableRow(Object[] row)
	{
		DefaultTableModel model = (DefaultTableModel)gameMovesTable.getModel();
		model.addRow(row);
	}

	@Override
	public void clearGameMovesTable()
	{
		DefaultTableModel model = (DefaultTableModel)gameMovesTable.getModel();
		model.setRowCount(0);
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
	public Double getBetCoefficient()
	{
		return (Double)coefSpinner.getValue();
	}

	@Override
	public BetStatus getBetStatus()
	{
		return BetStatus.values()[betStatusBox.getSelectedIndex()];
	}

	@Override
	public List<Bet> getEnclosureBets()
	{
		try
		{
			String[] idStrs = enclBetsIdField.getText().split(";");
			List<Bet> result = new ArrayList<Bet>();

			for (var idStr: idStrs)
			{
				Integer id = Integer.parseInt(idStr);

				result.add(new Bet(id));
			}

			return result;
		}
		catch (Exception e)
		{
			return null;
		}
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
