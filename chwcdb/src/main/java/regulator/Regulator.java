package regulator;

import org.redisson.api.RedissonClient;

import dao.*;
import entity.enums.*;
import presenter.*;
import view.*;
import model.*;

public class Regulator implements IRegulator
{
    private IPresenter presenter;
    private RedissonClient client;
    private String connStr;
    private boolean cached;

    public Regulator(
        String connStr,
        final RedissonClient client
    )
    {
        this.client = client;
        this.connStr = connStr;
    }

    public void changeUser() throws Exception
    {
        presenter = new UnauthorizedPresenter(
            new UnauthorizedModel(new PostgresUserDAO(connStr, "unauthorized", "unauthorized")),
            new UnauthorizedView(),
            this
        );
    }

    public void changeUser(final UserRole role) throws Exception
    {
        if (role == UserRole.ADMIN)
            if (!cached)
                presenter = new AdminPresenter(
                    new AdminModel(
                        new PostgresGameDAO(connStr, "admin", "admin"),
                        new PostgresGameMoveDAO(connStr, "admin", "admin"),
                        new PostgresBetDAO(connStr, "admin", "admin"),
                        new PostgresPlayerDAO(connStr, "admin", "admin"),
                        new PostgresUserDAO(connStr, "admin", "admin"),
                        new PostgresRefereeDAO(connStr, "admin", "admin"),
                        client.<String>getQueue("taskqueue")
                    ),
                    new AdminView(),
                    this,
                    client
                );
        else if (role == UserRole.SPECTATOR)
            if (!cached)
                presenter = new SpectatorPresenter(
                    new SpectatorModel(
                        new PostgresGameDAO(connStr, "spectator", "spectator"),
                        new PostgresGameMoveDAO(connStr, "spectator", "spectator"),
                        client.<String>getQueue("taskqueue")
                    ),
                    new SpectatorView(),
                    this,
                    client
                );
        else if (role == UserRole.BOOKMAKER)
            if (!cached)
                presenter = new BookmakerPresenter(
                    new BookmakerModel(
                        new PostgresGameMoveDAO(connStr, "bookmaker", "bookmaker"),
                        new PostgresBetDAO(connStr, "bookmaker", "bookmaker"),
                        client.<String>getQueue("taskqueue")
                    ),
                    new BookmakerView(),
                    this,
                    client
                );
    }
}
