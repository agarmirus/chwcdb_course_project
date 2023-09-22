package view;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.table.*;

import entity.enums.GameResult;
import entity.enums.UserRole;
import presenter.IPresenter;

public class AdminView extends IView {
	public AdminView() {
		initComponents();
		setVisible(true);
	}

	private void initComponents() {
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		label6 = new JLabel();
		label7 = new JLabel();
		gameIdSpinner = new JSpinner();
		roundSpinner = new JSpinner();
		numberSpinner = new JSpinner();
		dateField = new JTextField();
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
		betIdSpinner = new JSpinner();
		removeBetButton = new JButton();
		label11 = new JLabel();
		label12 = new JLabel();
		label13 = new JLabel();
		label14 = new JLabel();
		label15 = new JLabel();
		label16 = new JLabel();
		label17 = new JLabel();
		playerFirstNameField = new JTextField();
		playerSecondNameField = new JTextField();
		playerThirdNameField = new JTextField();
		playerBirthDateField = new JTextField();
		playerCountryField = new JTextField();
		playerRaitingSpinner = new JSpinner();
		playerIdSpinner = new JSpinner();
		addPlayerButton = new JButton();
		removePlayerButton = new JButton();
		label18 = new JLabel();
		label19 = new JLabel();
		refereeFirstNameField = new JTextField();
		refereeSecondNameField = new JTextField();
		label20 = new JLabel();
		label21 = new JLabel();
		refereeThirdNameField = new JTextField();
		refereeBirthDateField = new JTextField();
		label22 = new JLabel();
		label23 = new JLabel();
		refereeCountryField = new JTextField();
		refereeIdSpinner = new JSpinner();
		addRefereeButton = new JButton();
		removeRefereeButton = new JButton();
		label24 = new JLabel();
		label25 = new JLabel();
		label26 = new JLabel();
		userLoginField = new JTextField();
		userPasswordField = new JTextField();
		userRoleBox = new JComboBox<>();
		addUserButton = new JButton();
		removeUserButton = new JButton();
		testButton = new JButton();
		label27 = new JLabel();
		testResultField = new JTextField();

		//======== this ========
		Container contentPane = getContentPane();

		removeGameButton.setActionCommand("removegame");
		gameInfoButton.setActionCommand("gameinfo");
		signOutButton.setActionCommand("signout");
		removeBetButton.setActionCommand("removebet");
		addPlayerButton.setActionCommand("addplayer");
		removePlayerButton.setActionCommand("removeplayer");
		addRefereeButton.setActionCommand("addreferee");
		removeRefereeButton.setActionCommand("removereferee");
		addUserButton.setActionCommand("adduser");
		removeUserButton.setActionCommand("removeuser");
		testButton.setActionCommand("test");

		//---- label1 ----
		label1.setText("ID Игры:");

		//---- label2 ----
		label2.setText("Раунд:");

		//---- label3 ----
		label3.setText("Номер:");

		//---- label4 ----
		label4.setText("Дата:");

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

		//---- removeGameButton ----
		removeGameButton.setText("Удалить игру");

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

		//---- signOutButton ----
		signOutButton.setText("Выйти");

		//---- label10 ----
		label10.setText("ID ставки:");

		//---- removeBetButton ----
		removeBetButton.setText("Удалить ставку");

		//---- label11 ----
		label11.setText("ID игрока:");

		//---- label12 ----
		label12.setText("Фамилия:");

		//---- label13 ----
		label13.setText("Имя:");

		//---- label14 ----
		label14.setText("Отчество:");

		//---- label15 ----
		label15.setText("Дата рождения:");

		//---- label16 ----
		label16.setText("Страна:");

		//---- label17 ----
		label17.setText("Рейтинг:");

		//---- playerRaitingSpinner ----
		playerRaitingSpinner.setModel(new SpinnerNumberModel(0, 0, null, 1));

		//---- addPlayerButton ----
		addPlayerButton.setText("Добавить игрока");

		//---- removePlayerButton ----
		removePlayerButton.setText("Удалить игрока");

		//---- label18 ----
		label18.setText("ID судьи:");

		//---- label19 ----
		label19.setText("Фамилия:");

		//---- label20 ----
		label20.setText("Имя:");

		//---- label21 ----
		label21.setText("Отчество:");

		//---- label22 ----
		label22.setText("Дата рождения:");

		//---- label23 ----
		label23.setText("Страна:");

		//---- addRefereeButton ----
		addRefereeButton.setText("Добавить судью");

		//---- removeRefereeButton ----
		removeRefereeButton.setText("Удалить судью");

		//---- label24 ----
		label24.setText("Логин:");

		//---- label25 ----
		label25.setText("Пароль:");

		//---- label26 ----
		label26.setText("Роль:");

		//---- userRoleBox ----
		userRoleBox.setModel(new DefaultComboBoxModel<>(new String[] {
			"Администратор",
			"Наблюдатель",
			"Букмекер"
		}));

		//---- addUserButton ----
		addUserButton.setText("Добавить пользователя");

		//---- removeUserButton ----
		removeUserButton.setText("Удалить пользователя");

		//---- testButton ----
		testButton.setText("Тест");

		//---- label27 ----
		label27.setText("Результат:");

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(65, 65, 65)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(label9)
								.addComponent(label8)
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
								.addComponent(durationSpinner, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(resultBox, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(90, 90, 90)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(gameInfoButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
								.addComponent(removeGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(signOutButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)))
						.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(label7)
								.addComponent(label6))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(firstPlayerIdSpinner, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(secondPlayerIdSpinner, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))))
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(18, 18, 18)
							.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 427, GroupLayout.PREFERRED_SIZE)
							.addGap(47, 47, 47)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(label11)
										.addComponent(label12)
										.addComponent(label13)
										.addComponent(label14)
										.addComponent(label15)
										.addComponent(label16)
										.addComponent(label17))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup()
										.addComponent(playerRaitingSpinner, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addGroup(contentPaneLayout.createParallelGroup()
												.addComponent(playerSecondNameField, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
												.addComponent(playerThirdNameField, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
												.addComponent(playerBirthDateField, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
												.addComponent(playerCountryField, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
												.addComponent(playerFirstNameField, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
												.addComponent(playerIdSpinner, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
											.addGap(34, 34, 34)
											.addGroup(contentPaneLayout.createParallelGroup()
												.addGroup(contentPaneLayout.createSequentialGroup()
													.addGap(34, 34, 34)
													.addComponent(label23)
													.addGap(6, 6, 6)
													.addComponent(refereeCountryField, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
												.addGroup(contentPaneLayout.createSequentialGroup()
													.addGroup(contentPaneLayout.createParallelGroup()
														.addGroup(contentPaneLayout.createSequentialGroup()
															.addGroup(contentPaneLayout.createParallelGroup()
																.addGroup(contentPaneLayout.createSequentialGroup()
																	.addGap(23, 23, 23)
																	.addComponent(label22)
																	.addGap(6, 6, 6)
																	.addComponent(refereeBirthDateField, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
																.addGroup(contentPaneLayout.createSequentialGroup()
																	.addGap(18, 18, 18)
																	.addComponent(label19)
																	.addGap(6, 6, 6)
																	.addComponent(refereeFirstNameField, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
																.addGroup(contentPaneLayout.createSequentialGroup()
																	.addGap(22, 22, 22)
																	.addComponent(label18)
																	.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																	.addComponent(refereeIdSpinner, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
																.addGroup(contentPaneLayout.createSequentialGroup()
																	.addComponent(label20)
																	.addGap(6, 6, 6)
																	.addComponent(refereeSecondNameField, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
																.addGroup(contentPaneLayout.createSequentialGroup()
																	.addGap(13, 13, 13)
																	.addComponent(label21)
																	.addGap(6, 6, 6)
																	.addComponent(refereeThirdNameField, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)))
															.addGap(67, 67, 67)
															.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
																.addComponent(label24)
																.addComponent(label25)
																.addComponent(label26))
															.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
														.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
															.addComponent(label27)
															.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
															.addComponent(testResultField, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
															.addGap(4, 4, 4)))
													.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
														.addComponent(userLoginField)
														.addComponent(userPasswordField)
														.addComponent(userRoleBox, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
														.addComponent(testButton, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGap(54, 54, 54)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(removePlayerButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(addPlayerButton, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
									.addGap(88, 88, 88)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(removeRefereeButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(addRefereeButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addGap(97, 97, 97)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(addUserButton, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
										.addComponent(removeUserButton, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)))))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(92, 92, 92)
							.addComponent(label10)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(betIdSpinner, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(removeBetButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(34, Short.MAX_VALUE))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(37, 37, 37)
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(0, 0, Short.MAX_VALUE)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label11)
										.addComponent(playerIdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label12)
										.addComponent(playerFirstNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label13)
										.addComponent(playerSecondNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label14)
										.addComponent(playerThirdNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label15)
										.addComponent(playerBirthDateField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label16)
										.addComponent(playerCountryField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(refereeIdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(label18))
									.addGap(6, 6, 6)
									.addGroup(contentPaneLayout.createParallelGroup()
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addGap(3, 3, 3)
											.addComponent(label19))
										.addComponent(refereeFirstNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(6, 6, 6)
									.addGroup(contentPaneLayout.createParallelGroup()
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addGroup(contentPaneLayout.createParallelGroup()
												.addGroup(contentPaneLayout.createSequentialGroup()
													.addGap(3, 3, 3)
													.addComponent(label20))
												.addComponent(refereeSecondNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
											.addGap(6, 6, 6)
											.addGroup(contentPaneLayout.createParallelGroup()
												.addGroup(contentPaneLayout.createSequentialGroup()
													.addGap(3, 3, 3)
													.addComponent(label21))
												.addComponent(refereeThirdNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
											.addGap(6, 6, 6)
											.addGroup(contentPaneLayout.createParallelGroup()
												.addGroup(contentPaneLayout.createSequentialGroup()
													.addGap(3, 3, 3)
													.addComponent(label22))
												.addComponent(refereeBirthDateField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(label24)
												.addComponent(userLoginField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(label25)
												.addComponent(userPasswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(label26)
												.addComponent(userRoleBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup()
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addGap(3, 3, 3)
											.addComponent(label23))
										.addComponent(refereeCountryField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup()
										.addComponent(label17)
										.addComponent(playerRaitingSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
									.addComponent(addPlayerButton)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(removePlayerButton))
								.addGroup(contentPaneLayout.createParallelGroup()
									.addGroup(contentPaneLayout.createSequentialGroup()
										.addComponent(addUserButton)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(removeUserButton))
									.addGroup(contentPaneLayout.createSequentialGroup()
										.addComponent(addRefereeButton)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(removeRefereeButton))))
							.addGap(110, 110, 110))
						.addGroup(contentPaneLayout.createSequentialGroup()
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
										.addComponent(label6)
										.addComponent(firstPlayerIdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label7)
										.addComponent(secondPlayerIdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(43, 43, 43)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label8)
										.addComponent(durationSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(label9)
										.addComponent(resultBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(32, 32, 32)
									.addComponent(removeGameButton)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(gameInfoButton))
								.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 405, GroupLayout.PREFERRED_SIZE))
							.addGap(18, 18, 18)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(signOutButton)
								.addComponent(label10)
								.addComponent(betIdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(removeBetButton)
								.addComponent(testButton)
								.addComponent(label27)
								.addComponent(testResultField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addContainerGap(23, Short.MAX_VALUE))))
		);
		pack();
		setLocationRelativeTo(getOwner());

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JLabel label6;
	private JLabel label7;
	private JSpinner gameIdSpinner;
	private JSpinner roundSpinner;
	private JSpinner numberSpinner;
	private JTextField dateField;
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
	private JSpinner betIdSpinner;
	private JButton removeBetButton;
	private JLabel label11;
	private JLabel label12;
	private JLabel label13;
	private JLabel label14;
	private JLabel label15;
	private JLabel label16;
	private JLabel label17;
	private JTextField playerFirstNameField;
	private JTextField playerSecondNameField;
	private JTextField playerThirdNameField;
	private JTextField playerBirthDateField;
	private JTextField playerCountryField;
	private JSpinner playerRaitingSpinner;
	private JSpinner playerIdSpinner;
	private JButton addPlayerButton;
	private JButton removePlayerButton;
	private JLabel label18;
	private JLabel label19;
	private JTextField refereeFirstNameField;
	private JTextField refereeSecondNameField;
	private JLabel label20;
	private JLabel label21;
	private JTextField refereeThirdNameField;
	private JTextField refereeBirthDateField;
	private JLabel label22;
	private JLabel label23;
	private JTextField refereeCountryField;
	private JSpinner refereeIdSpinner;
	private JButton addRefereeButton;
	private JButton removeRefereeButton;
	private JLabel label24;
	private JLabel label25;
	private JLabel label26;
	private JTextField userLoginField;
	private JTextField userPasswordField;
	private JComboBox<String> userRoleBox;
	private JButton addUserButton;
	private JButton removeUserButton;
	private JButton testButton;
	private JLabel label27;
	private JTextField testResultField;

	@Override
	public void setPresenterListener(final IPresenter listener)
	{
		removeAllButtonListeners(removeGameButton);
		removeAllButtonListeners(gameInfoButton);
		removeAllButtonListeners(signOutButton);
		removeAllButtonListeners(removeBetButton);
		removeAllButtonListeners(addPlayerButton);
		removeAllButtonListeners(removePlayerButton);
		removeAllButtonListeners(addRefereeButton);
		removeAllButtonListeners(removeRefereeButton);
		removeAllButtonListeners(addUserButton);
		removeAllButtonListeners(removeUserButton);
		removeAllButtonListeners(testButton);

		removeGameButton.addActionListener(listener);
		gameInfoButton.addActionListener(listener);
		signOutButton.addActionListener(listener);
		removeBetButton.addActionListener(listener);
		addPlayerButton.addActionListener(listener);
		removePlayerButton.addActionListener(listener);
		addRefereeButton.addActionListener(listener);
		removeRefereeButton.addActionListener(listener);
		addUserButton.addActionListener(listener);
		removeUserButton.addActionListener(listener);
		testButton.addActionListener(listener);
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
	public Integer getPlayerId()
	{
		return (Integer)playerIdSpinner.getValue();
	}

	@Override
	public String getPlayerFirstName()
	{
		return playerFirstNameField.getText().strip();
	}

	@Override
	public String getPlayerSecondName()
	{
		return playerSecondNameField.getText().strip();
	}

	@Override
	public String getPlayerThirdName()
	{
		return playerThirdNameField.getText().strip();
	}

	@Override
	public Date getPlayerBirthDate()
	{
		try
		{
			return new SimpleDateFormat("dd.MM.yyyy").parse(playerBirthDateField.getText());
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	@Override
	public String getPlayerCountry()
	{
		return playerCountryField.getText().strip();
	}

	@Override
	public Integer getPlayerRaiting()
	{
		return (Integer)playerRaitingSpinner.getValue();
	}

	@Override
	public Integer getRefereeId()
	{
		return (Integer)refereeIdSpinner.getValue();
	}

	@Override
	public String getRefereeFirstName()
	{
		return refereeFirstNameField.getText().strip();
	}

	@Override
	public String getRefereeSecondName()
	{
		return refereeSecondNameField.getText().strip();
	}

	@Override
	public String getRefereeThirdName()
	{
		return refereeThirdNameField.getText().strip();
	}

	@Override
	public Date getRefereeBirthDate()
	{
		try
		{
			return new SimpleDateFormat("dd.MM.yyyy").parse(refereeBirthDateField.getText());
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	@Override
	public String getRefereeCountry()
	{
		return refereeCountryField.getText().strip();
	}

	@Override
	public String getUserLogin()
	{
		return userLoginField.getText().strip();
	}

	@Override
	public String getUserPassword()
	{
		return userPasswordField.getText().strip();
	}

	@Override
	public UserRole getUserRole()
	{
		return UserRole.values()[userRoleBox.getSelectedIndex()];
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

	@Override
	public void setTestResult(final long result)
	{
		testResultField.setText(Long.toString(result));
	}
}
