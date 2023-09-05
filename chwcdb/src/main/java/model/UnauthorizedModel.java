package model;

import java.util.Optional;

import dao.IDAO;
import entity.User;
import appexception.*;

public class UnauthorizedModel extends IModel
{
    private IDAO<User> userDAO;

    public UnauthorizedModel() {}
    public UnauthorizedModel(final IDAO<User> userDAO) { this.userDAO = userDAO; }

    @Override
    public void setUserDAO(final IDAO<User> dao) { userDAO = dao; }

    @Override
    public IDAO<User> getUserDAO() { return userDAO; }

    @Override
    public Optional<User> authorize(final User user) throws CHWCDBException
    {
        if (userDAO == null)
            throw new CHWCDBNullDAOException(
                "UnauthorizedModel.authorize(User): user DAO is null"
            );
        
        if (user == null)
            throw new CHWCDBInvalidParameterException(
                "UnauthorizedModel.authorize(User): user is null"
            );
        
        if (user.getLogin() == null || user.getLogin().isEmpty())
            throw new CHWCDBInvalidParameterException(
                "UnauthorizedModel.authorize(User): user doesn't have login"
            );
        
        if (user.getHashedPassword() == null || user.getHashedPassword().isEmpty())
            throw new CHWCDBInvalidParameterException(
                "UnauthorizedModel.authorize(User): user doesn't have hashed password"
            );
        
        var result = userDAO.get(user);

        return result;
    }
}
