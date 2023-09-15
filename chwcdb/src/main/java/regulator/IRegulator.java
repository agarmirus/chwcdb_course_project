package regulator;

import entity.enums.UserRole;

public interface IRegulator
{
    public void changeUser() throws Exception;
    public void changeUser(final UserRole role) throws Exception;
}
