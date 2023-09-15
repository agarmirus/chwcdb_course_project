package presenter;

import java.awt.event.ActionEvent;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;

import appexception.CHWCDBInvalidInputException;
import entity.User;
import model.IModel;
import regulator.IRegulator;
import view.IView;

public class UnauthorizedPresenter implements IPresenter
{
    private IModel model;
    private IView view;
    private IRegulator regulator;

    public UnauthorizedPresenter(
        final IModel model,
        final IView view,
        final IRegulator regulator
    )
    {
        this.model = model;
        this.view = view;
        this.regulator = regulator;
    }

    public void signIn() throws Exception
    {
        String login = view.getAuthLogin();

        if (login.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter login"
            );
        
        String pswd = view.getAuthPassword();

        if (pswd.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Enter password"
            );
        
        pswd = DigestUtils.sha1Hex(pswd);

        Optional<User> optUser = model.authorize(new User(login, pswd, null));

        if (optUser.isEmpty())
            throw new CHWCDBInvalidInputException(
                "Authentication failed"
            );
        
        view.closeWindow();
        regulator.changeUser(optUser.get().getRole());
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        try
        {
            String command = event.getActionCommand();

            if (command.equals("signin"))
                signIn();
        }
        catch (Exception e)
        {
            view.showErrorDialog(e.getMessage());
        }
    }
}
