package logger;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import logger.level.LogLevel;

public class InfluxLogger implements ILogger
{
    private InfluxDB influxDB;
    private LogLevel level = LogLevel.OFF;
    private Class<?> clazz;

    public InfluxLogger(
        String url,
        String user,
        String pswd,
        final Integer batchSize,
        final Integer batchDuration
    )
    {
        influxDB = InfluxDBFactory.connect(url, user, pswd);

        influxDB.setRetentionPolicy("defaultPolicy");
        influxDB.setDatabase("logs");
        influxDB.enableBatch(batchSize, batchDuration, TimeUnit.MILLISECONDS);
    }

    @Override
    public void setLogLevel(final LogLevel level)
    {
        this.level = level;
    }

    @Override
    public void setClass(final Class<?> clazz)
    {
        this.clazz = clazz;
    }

    void sendLog(String text)
    {
        var point = Point.measurement("memory")
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .addField("class", clazz.getSimpleName()) 
                        .addField("text", text)
                        .build();

        influxDB.write(point);
    }

    @Override
    public void fatal(String text)
    {
        if (level.ordinal() >= LogLevel.FATAL.ordinal())
            sendLog(text);
    }

    @Override
    public void error(String text)
    {
        if (level.ordinal() >= LogLevel.ERROR.ordinal())
            sendLog(text);
    }

    @Override
    public void warn(String text)
    {
        if (level.ordinal() >= LogLevel.WARN.ordinal())
            sendLog(text);
    }

    @Override
    public void info(String text)
    {
        if (level.ordinal() >= LogLevel.INFO.ordinal())
            sendLog(text);
    }

    @Override
    public void debug(String text)
    {
        if (level.ordinal() >= LogLevel.DEBUG.ordinal())
            sendLog(text);
    }

    @Override
    public void trace(String text)
    {
        if (level.ordinal() >= LogLevel.TRACE.ordinal())
            sendLog(text);
    }
}
