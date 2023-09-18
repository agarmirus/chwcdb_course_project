package regulator;

import org.influxdb.InfluxDB;
import org.redisson.api.RedissonClient;

import dao.*;
import entity.enums.*;
import logger.ILogger;
import presenter.*;
import view.*;
import view.decorator.*;
import model.*;
import model.decorator.*;

public class Regulator implements IRegulator
{
    private IPresenter presenter;
    private RedissonClient client;
    private String connStr;
    private boolean cached;
    private long ttl;
    private ILogger logger;

    public Regulator(
        String connStr,
        final RedissonClient client,
        final InfluxDB influx,
        final boolean cached,
        final long ttl,
        final ILogger logger
    )
    {
        this.client = client;
        this.connStr = connStr;
        this.cached = cached;
        this.ttl = ttl;
        this.logger = logger;
    }

    public void changeUser() throws Exception
    {
        IModel model = null;

        if (cached)
            model = new UnauthorizedModelLogDecorator(
                new UnauthorizedModel(new PostgresUserCachedDAO(connStr, "unauthorized", "unauthorized", client, ttl)),
                logger
            );
        else
            model = new UnauthorizedModelLogDecorator(
                new UnauthorizedModel(new PostgresUserDAO(connStr, "unauthorized", "unauthorized")),
                logger
            );

        IView view = new UnauthorizedViewLogDecorator(
            new UnauthorizedView(),
            logger
        );

        presenter = new UnauthorizedPresenter(model, view, this);
    }

    public void changeUser(final UserRole role) throws Exception
    {
        if (role == UserRole.SPECTATOR)
        {
            IModel model = null;

            if (cached)
                model = new SpectatorModelLogDecorator(
                    new SpectatorModel(
                        new PostgresGameCachedDAO(connStr, "spectator", "spectator", client, ttl),
                        new PostgresGameMoveCachedDAO(connStr, "spectator", "spectator", client, ttl),
                        client
                    ),
                    logger
                );
            else
                model = new SpectatorModelLogDecorator(
                new SpectatorModel(
                        new PostgresGameDAO(connStr, "spectator", "spectator"),
                        new PostgresGameMoveDAO(connStr, "spectator", "spectator"),
                        client
                    ),
                    logger
                );

            IView view = new SpectatorViewLogDecorator(
                new SpectatorView(),
                logger
            );

            presenter = new SpectatorPresenter(model, view, this, client);
        }
        else if (role == UserRole.BOOKMAKER)
        {
            IModel model = null;

            if (cached)
                model = new BookmakerModelLogDecorator(
                    new BookmakerModel(
                        new PostgresGameMoveCachedDAO(connStr, "bookmaker", "bookmaker", client, ttl),
                        new PostgresBetCachedDAO(connStr, "bookmaker", "bookmaker", client, ttl),
                        client
                    ),
                    logger
                );
            else
            {
                model = new BookmakerModelLogDecorator(
                    new BookmakerModel(
                        new PostgresGameMoveDAO(connStr, "bookmaker", "bookmaker"),
                        new PostgresBetDAO(connStr, "bookmaker", "bookmaker"),
                        client
                    ),
                    logger
                );
            }

            IView view = new BookmakerViewLogDecorator(
                new BookmakerView(),
                logger
            );

            presenter = new BookmakerPresenter(model, view, this, client);
        }
        else if (role == UserRole.ADMIN)
        {
            IModel model = null;

            if (cached)
                model = new AdminModelLogDecorator(
                    new AdminModel(
                        new PostgresGameCachedDAO(connStr, "administrator", "administrator", client, ttl),
                        new PostgresGameMoveCachedDAO(connStr, "administrator", "administrator", client, ttl),
                        new PostgresBetCachedDAO(connStr, "administrator", "administrator", client, ttl),
                        new PostgresPlayerCachedDAO(connStr, "administrator", "administrator", client, ttl),
                        new PostgresUserCachedDAO(connStr, "administrator", "administrator", client, ttl),
                        new PostgresRefereeCachedDAO(connStr, "administrator", "administrator", client, ttl),
                        client
                    ),
                    logger
                );
            else
            {
                model = new AdminModelLogDecorator(
                    new AdminModel(
                        new PostgresGameDAO(connStr, "administrator", "administrator"),
                        new PostgresGameMoveDAO(connStr, "administrator", "administrator"),
                        new PostgresBetDAO(connStr, "administrator", "administrator"),
                        new PostgresPlayerDAO(connStr, "administrator", "administrator"),
                        new PostgresUserDAO(connStr, "administrator", "administrator"),
                        new PostgresRefereeDAO(connStr, "administrator", "administrator"),
                        client
                    ),
                    logger
                );
            }

            IView view = new AdminViewLogDecorator(
                new AdminView(),
                logger
            );

            presenter = new AdminPresenter(model, view, this, client);
        }
    }
}
