package model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Optional;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import dao.IDAO;
import entity.User;
import entity.enums.UserRole;
import appexception.*;

public class UnauthorizedModelTest
{
    @Tested UnauthorizedModel unauthorizedModel;

    @Injectable IDAO<User> userDAO;

    /*
    --------------------------
        authorize tests
    --------------------------
    */
    @Test
    public void authorizeValidTest() throws Exception
    {
        String login = "aaaaa";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        new Expectations()
        {{
            userDAO.get(user);
            result = Optional.of(new User("aaaaa", null, UserRole.SPECTATOR));
        }};

        var optFoundUser = unauthorizedModel.authorize(user);
        assertNotNull(optFoundUser);
        assertTrue(optFoundUser.isPresent());

        var foundUser = optFoundUser.get();
        assertNotNull(foundUser);
        assertEquals(user.getLogin(), foundUser.getLogin());
        assertNull(foundUser.getHashedPassword());
        assertEquals(user.getRole(), foundUser.getRole());
    }

    @Test(expected = CHWCDBNullDAOException.class)
    public void authorizeNullUserDAOTest() throws Exception
    {
        String login = "aaaaa";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        unauthorizedModel.setUserDAO(null);
        unauthorizedModel.authorize(user);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void authorizeNullUserTest() throws Exception
    {
        unauthorizedModel.authorize(null);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void authorizeNullLoginTest() throws Exception
    {
        String login = null;
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        unauthorizedModel.authorize(user);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void authorizeEmptyLoginTest() throws Exception
    {
        String login = "";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        unauthorizedModel.authorize(user);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void authorizeNullHashedPasswordTest() throws Exception
    {
        String login = "aaaaa";
        String hashedPswd = null;
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        unauthorizedModel.authorize(user);
    }

    @Test(expected = CHWCDBInvalidParameterException.class)
    public void authorizeEmptyHashedPasswordTest() throws Exception
    {
        String login = "aaaaa";
        String hashedPswd = "";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        unauthorizedModel.authorize(user);
    }

    @Test(expected = CHWCDBDataAccessException.class)
    public void authorizeFailedDataAccessTest() throws Exception
    {
        String login = "aaaaa";
        String hashedPswd = "bbb";
        UserRole role = UserRole.SPECTATOR;

        var user = new User(login, hashedPswd, role);

        new Expectations()
        {{
            userDAO.get(user);
            result = new CHWCDBDataAccessException();
        }};

        unauthorizedModel.authorize(user);
    }
}
