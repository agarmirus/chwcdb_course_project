package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;

import presenter.IPresenter;

public class UnauthorizedView extends IView {
	public UnauthorizedView() {
		initComponents();
		setVisible(true);
	}

	private void initComponents() {
		label1 = new JLabel();
		loginField = new JTextField();
		label2 = new JLabel();
		signInButton = new JButton();
		passwordField = new JPasswordField();

		signInButton.setActionCommand("signin");

		//======== this ========
		setResizable(false);
		Container contentPane = getContentPane();

		//---- label1 ----
		label1.setText("Login");

		//---- label2 ----
		label2.setText("Password");

		//---- signInButton ----
		signInButton.setText("Sign In (Worker)");

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(42, 42, 42)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(label2)
						.addComponent(label1))
					.addGap(18, 18, 18)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addComponent(loginField, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
						.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
					.addContainerGap(36, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
					.addContainerGap(130, Short.MAX_VALUE)
					.addComponent(signInButton)
					.addGap(127, 127, 127))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(60, 60, 60)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(loginField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label1))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(label2)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(28, 28, 28)
					.addComponent(signInButton)
					.addContainerGap(54, Short.MAX_VALUE))
		);
		pack();
		setLocationRelativeTo(getOwner());

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private JLabel label1;
	private JTextField loginField;
	private JLabel label2;
	private JButton signInButton;
	private JPasswordField passwordField;

	@Override
	public void setPresenterListener(final IPresenter listener)
	{
		removeAllButtonListeners(signInButton);

		signInButton.addActionListener(listener);
	}

	@Override
	public String getAuthLogin()
	{
		return loginField.getText().strip();
	}

	@Override
	public String getAuthPassword()
	{
		return new String(passwordField.getPassword());
	}
}
