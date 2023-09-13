package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
