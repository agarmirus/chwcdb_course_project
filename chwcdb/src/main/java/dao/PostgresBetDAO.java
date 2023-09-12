package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import appexception.*;
import javafx.util.Pair;

import entity.Bet;
import entity.enums.BetStatus;
import entity.enums.BetType;

public class PostgresBetDAO implements IDAO<Bet>
{
    private Connection connection;

    private Bet getOneBet(ResultSet resultSet) throws SQLException
    {
        Bet bet = null;

        if (resultSet.next())
        {
            BetType type = BetType.values()[resultSet.getInt("type")];

            if (type == BetType.ELEMENTARY)
            {
                bet = new Bet(
                    resultSet.getInt("id"),
                    type,
                    resultSet.getString("condition"),
                    resultSet.getDouble("coefficient"),
                    resultSet.getInt("gameId")
                );
            }
            else
            {
                bet = new Bet(
                    resultSet.getInt("id"),
                    type,
                    null
                );
            }
        }
        
        return bet;
    }

    private List<Bet> getEnclosures(final int gameId) throws SQLException
    {
        String query = String.format(
            "select id, type, condition, coefficient, status, game_id from bet_enclosures be join bets b on (b.id = be.enclosure_id and be.bet_id = %d) as encl_bets;",
            gameId
        );

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        List<Bet> result = new ArrayList<Bet>();

        while (resultSet.next())
        {
            result.add(
                new Bet(
                    resultSet.getInt("id"),
                    BetType.values()[resultSet.getInt("type")],
                    resultSet.getString("condition"),
                    resultSet.getDouble("coefficient"),
                    BetStatus.values()[resultSet.getInt("status")],
                    resultSet.getInt("game_id")
                )
            );
        }

        return result;
    }

    public PostgresBetDAO(
        String url,
        String bet,
        String pswd
    ) throws Exception
    {
        connection = DriverManager.getConnection(url, bet, pswd);
    }

    public void setConnection(String url, String bet, String pswd) throws CHWCDBException
    {
        try
        {
            connection = DriverManager.getConnection(url, bet, pswd);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetDAO.setConnection(String, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<Bet> get(final Bet entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBetDAO.get(Bet): no connection to data base"
                );
            }

            String query = String.format(
                "select * from bets where id = %d;",
                entity.getId()
            );
            
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(query);

            var result = getOneBet(queryResult);

            if (result == null || result.getType() == BetType.ELEMENTARY)
                return Optional.ofNullable(result);

            result.setBets(new ArrayList<Bet>());

            List<Bet> enclBets = getEnclosures(result.getId());

            for (var enclBet: enclBets)
            {
                if (enclBet.getType() == BetType.EXPRESS)
                    enclBet.setBets(getEnclosures(enclBet.getId()));
                
                result.getBets().add(enclBet);
            }

            return Optional.of(result);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetDAO.get(Bet): %s",
                    e.getMessage()
                )
            );
        }
    }

    public Optional<List<Bet>> get(final List<Bet> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.get(List<Bet>): method is not implemented"
        );
    }

    public Optional<List<Bet>> get(String attributeName, String value) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.get(String, String): method is not implemented"
        );
    }

    public void create(final Bet entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBetDAO.create(Bet): no connection to data base"
                );
            }
            
            String query = String.format(
                "insert into bets values (%d, %d, '%s', %f, %d, %d);",
                entity.getId(),
                entity.getType().ordinal(),
                entity.getCondition(),
                entity.getCoefficient(),
                entity.getStatus().ordinal(),
                entity.getGameId()
            );

            
            if (entity.getType() != BetType.ELEMENTARY)
            {
                String betEnclosuresQuery = "";

                for (var enclBet: entity.getBets())
                {
                    if (betEnclosuresQuery.isEmpty())
                        betEnclosuresQuery = String.format(
                            "insert into bet_enclosures values (%d, %d)",
                            entity.getId(),
                            enclBet.getId()
                        );
                    else
                        betEnclosuresQuery += String.format(
                            ", (%d, %d)",
                            entity.getId(),
                            enclBet.getId()
                        );
                }

                query = "begin;" + query + betEnclosuresQuery + ";commit;";
            }

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetDAO.create(Bet): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void create(final List<Bet> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.get(String, String): method is not implemented"
        );
    }

    public void update(final Bet entity) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.update(Bet): method is not implemented"
        );
    }

    public void update(final List<Bet> entities) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.update(List<Bet>): method is not implemented"
        );
    }

    public void update(final Bet entity, String attributeName, String value) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBetDAO.update(Bet, String, String): no connection to data base"
                );
            }

            String query = String.format(
                "update bets set %s = '%s' where id = %d;",
                attributeName,
                value,
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetDAO.update(Bet, String, String): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void update(final Bet entity, List<Pair<String, String>> updates) throws CHWCDBException
    {
        throw new CHWCDBDataAccessException(
            "PostgresBetDAO.update(Bet, List<Pair<String, String>>): method is not implemented"
        );
    }
    
    public void update(final Bet entity, String attributeName, final int delta) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBetDAO.update(Bet, String, int): no connection to data base"
                );
            }

            String query = String.format(
                "update bets set %s = %s + %s where id = %d;",
                attributeName,
                attributeName,
                delta,
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetDAO.update(Bet, String, int): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void delete(final Bet entity) throws CHWCDBException
    {
        try
        {
            if (connection.isClosed() || !connection.isValid(0))
            {
                throw new CHWCDBDataAccessException(
                    "PostgresBetDAO.delete(Bet): no connection to data base"
                );
            }

            String query = String.format(
                "delete from bets where id = %d;",
                entity.getId()
            );

            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBetDAO.delete(Bet): %s",
                    e.getMessage()
                )
            );
        }
    }

    public void close() throws CHWCDBException
    {
        try
        {
            if (!connection.isClosed())
                connection.close();
        }
        catch (SQLException e)
        {
            throw new CHWCDBDataAccessException(
                String.format(
                    "PostgresBookDAO.close(): %s",
                    e.getMessage()
                )
            );
        }
    }
}
