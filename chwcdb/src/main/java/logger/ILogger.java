package logger;

import logger.level.LogLevel;

public interface ILogger
{
    public void setLogLevel(final LogLevel level);
    public void setClass(final Class<?> clazz);

    public void fatal(String text);
    public void error(String text);
    public void warn(String text);
    public void info(String text);
    public void debug(String text);
    public void trace(String text);
}
