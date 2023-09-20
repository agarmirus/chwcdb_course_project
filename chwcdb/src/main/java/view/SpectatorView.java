package view;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.table.*;

import entity.GameMove;
import entity.Move;
import entity.enums.Figure;
import entity.enums.GameResult;
import presenter.IPresenter;

public class SpectatorView extends IView {
	public SpectatorView() {
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
		removeGameButton = new JButton();
		gameInfoButton = new JButton();
		scrollPane1 = new JScrollPane();
		gameMovesTable = new JTable();
		signOutButton = new JButton();
		label10 = new JLabel();
		moveIdSpinner = new JSpinner();
		removeMoveButton = new JButton();
		scrollPane2 = new JScrollPane();
		movesTextArea = new JTextArea();
		addMovesButton = new JButton();
		addGameButton = new JButton();
		endGameButton = new JButton();

		//======== this ========
		Container contentPane = getContentPane();

		addGameButton.setActionCommand("addgame");
		removeGameButton.setActionCommand("removegame");
		endGameButton.setActionCommand("endgame");
		gameInfoButton.setActionCommand("gameinfo");
		removeMoveButton.setActionCommand("removemove");
		addMovesButton.setActionCommand("addmoves");
		signOutButton.setActionCommand("signout");

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

		//---- removeGameButton ----
		removeGameButton.setText("Remove Game");

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

		//---- signOutButton ----
		signOutButton.setText("Sign Out");

		//---- label10 ----
		label10.setText("Move ID:");

		//---- removeMoveButton ----
		removeMoveButton.setText("Remove Move");

		//======== scrollPane2 ========
		{
			scrollPane2.setViewportView(movesTextArea);
		}

		//---- addMovesButton ----
		addMovesButton.setText("Add Moves");

		//---- addGameButton ----
		addGameButton.setText("Add Game");

		//---- endGameButton ----
		endGameButton.setText("End Game");

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
								.addComponent(resultBox, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(90, 90, 90)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(removeGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(signOutButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(addGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(endGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(gameInfoButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(18, 18, 18)
							.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 427, GroupLayout.PREFERRED_SIZE))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(12, 12, 12)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(label10)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(moveIdSpinner, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGap(14, 14, 14)
									.addComponent(removeMoveButton)))
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(scrollPane2)))
					.addContainerGap(57, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
					.addGap(0, 471, Short.MAX_VALUE)
					.addComponent(addMovesButton)
					.addGap(150, 150, 150))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(37, 37, 37)
					.addGroup(contentPaneLayout.createParallelGroup()
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
								.addComponent(resultBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(scrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE))
					.addGap(32, 32, 32)
					.addGroup(contentPaneLayout.createParallelGroup()
						.addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addComponent(addGameButton)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(removeGameButton)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(endGameButton)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(gameInfoButton))
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGap(28, 28, 28)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label10)
										.addComponent(moveIdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(removeMoveButton)))
							.addGap(0, 21, Short.MAX_VALUE)))
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(addMovesButton)
						.addComponent(signOutButton))
					.addGap(53, 53, 53))
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
	private JButton removeGameButton;
	private JButton gameInfoButton;
	private JScrollPane scrollPane1;
	private JTable gameMovesTable;
	private JButton signOutButton;
	private JLabel label10;
	private JSpinner moveIdSpinner;
	private JButton removeMoveButton;
	private JScrollPane scrollPane2;
	private JTextArea movesTextArea;
	private JButton addMovesButton;
	private JButton addGameButton;
	private JButton endGameButton;

	@Override
	public void setPresenterListener(final IPresenter listener)
	{
		removeAllButtonListeners(addGameButton);
		removeAllButtonListeners(removeGameButton);
		removeAllButtonListeners(endGameButton);
		removeAllButtonListeners(gameInfoButton);
		removeAllButtonListeners(removeMoveButton);
		removeAllButtonListeners(addMovesButton);
		removeAllButtonListeners(signOutButton);

		addGameButton.addActionListener(listener);
		removeGameButton.addActionListener(listener);
		endGameButton.addActionListener(listener);
		gameInfoButton.addActionListener(listener);
		removeMoveButton.addActionListener(listener);
		addMovesButton.addActionListener(listener);
		signOutButton.addActionListener(listener);
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
	public Integer getMoveId()
	{
		return (Integer)moveIdSpinner.getValue();
	}

	boolean checkChessPosition(String position)
	{
		char col = position.charAt(0);
		char row = position.charAt(1);
		
		return col >= 'a' && col <= 'h' && row >= '1' && row <= '8';
	}

	@Override
	public List<GameMove> getMoves()
	{
		List<GameMove> moves = new ArrayList<GameMove>();
		String str = movesTextArea.getText().strip();

		if (str.isEmpty())
			return moves;

		String[] movesStr = str.split("\n");
		int i = 0;

		for (String moveStr: movesStr)
		{
			moveStr = moveStr.strip();

			char figureChar = moveStr.charAt(0);
			Figure figure;

			if (figureChar == 'K')
				figure = Figure.KING;
			else if (figureChar == 'Q')
				figure = Figure.QUEEN;
			else if (figureChar == 'N')
				figure = Figure.KNIGHT;
			else if (figureChar == 'R')
				figure = Figure.ROOK;
			else if (figureChar == 'B')
				figure = Figure.BISHOP;
			else if (figureChar == 'e')
				figure = Figure.PAWN;
			else
				return null;

			if (moveStr.charAt(3) != '-')
				return null;

			String startCell = moveStr.substring(1, 3);
			String endCell = moveStr.substring(4, 6);
			
			if (!checkChessPosition(startCell) || !checkChessPosition(endCell))
				return null;
			
			String comment = null;
			String[] moveStrParts = moveStr.split(" ", 1);

			if (moveStrParts.length >= 2)
				comment = moveStrParts[1].strip();
			
			Move move = new Move(0, figure, startCell, endCell);

			moves.add(new GameMove(null, move, ++i, comment));
		}

		return moves;
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
