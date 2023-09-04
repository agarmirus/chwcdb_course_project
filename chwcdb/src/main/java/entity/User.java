package entity;

import entity.enums.UserRole;

public class User
{
    private String login;
    private String hashedPswd;
    private UserRole role;

    public User() {}
    public User(
        String login,
        String hashedPswd,
        final UserRole role
    )
    {
        this.login = login;
        this.hashedPswd = hashedPswd;
        this.role = role;
    }

    public void setLogin(String login) { this.login = login; }
    public void setHashedPassword(String hashedPswd) { this.hashedPswd = hashedPswd; }
    public void setRole(final UserRole role) { this.role = role; }

    public String getLogin() { return login; }
    public String getHashedPassword() { return hashedPswd; }
    public UserRole getRole() { return role; }
}
