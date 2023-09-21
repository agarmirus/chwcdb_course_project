import java.io.BufferedReader;
import java.io.FileReader;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.json.JSONObject;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import logger.ILogger;
import logger.InfluxLogger;
import logger.level.LogLevel;
import regulator.Regulator;

public class App
{
    static protected String readConfigFile(String fileName) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String result = "";

        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null)
        {
            sb.append(line);
            sb.append(System.lineSeparator());

            line = br.readLine();
        }

        result = sb.toString();

        br.close();

        return result;
    }

    static private void setLogLevel(ILogger logger, String levelString)
    {
        if (levelString.equals("off"))
            logger.setLogLevel(LogLevel.OFF);
        else if (levelString.equals("fatal"))
            logger.setLogLevel(LogLevel.FATAL);
        else if (levelString.equals("error"))
            logger.setLogLevel(LogLevel.ERROR);
        else if (levelString.equals("warn"))
            logger.setLogLevel(LogLevel.WARN);
        else if (levelString.equals("info"))
            logger.setLogLevel(LogLevel.INFO);
        else if (levelString.equals("debug"))
            logger.setLogLevel(LogLevel.DEBUG);
        else if (levelString.equals("trace"))
            logger.setLogLevel(LogLevel.TRACE);
        else if (levelString.equals("all"))
            logger.setLogLevel(LogLevel.ALL);
    }

    static public void main(String[] args)
    {
        try
        {
            String configString = readConfigFile("config.json");
            var configJSONObject = new JSONObject(configString);

            String connString = configJSONObject.getString("conn");
            String logLevelString = configJSONObject.getString("logLevel");
            String redisConnString = configJSONObject.getString("redisConn");
            String influxConnString = configJSONObject.getString("influxConn");
            Boolean cached = configJSONObject.getBoolean("cached");

            long ttl = configJSONObject.getLong("ttl"); ////////////

            if (cached)
                ttl = configJSONObject.getLong("ttl");
            
            // InfluxDB influx = InfluxDBFactory.connect(influxConnString, "logger", "logger");

            // ILogger logger = new InfluxLogger(influx, 100, 200);
            // setLogLevel(logger, logLevelString);

            var config = new Config();
            config.useSingleServer().setAddress(redisConnString);

            RedissonClient redisClient = Redisson.create(config);

            // new Regulator(connString, redisClient, influx, cached, ttl, logger).changeUser();
            new Regulator(connString, redisClient, null, cached, ttl, null).changeUser();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
