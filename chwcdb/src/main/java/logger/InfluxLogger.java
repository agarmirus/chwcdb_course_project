package logger;

import java.time.Instant;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.*;

import logger.level.LogLevel;

public class InfluxLogger implements ILogger
{
    private LogLevel level = LogLevel.OFF;
    private Class<?> clazz;
    private WriteApiBlocking writeApi;

    public InfluxLogger(final InfluxDBClient influxDB)
    {
        writeApi = influxDB.getWriteApiBlocking();
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
                        .addField("class", clazz.getSimpleName()) 
                        .addField("text", text)
                        .time(Instant.now().toEpochMilli(), WritePrecision.MS);

        writeApi.writePoint(point);
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
