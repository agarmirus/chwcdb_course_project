package appexception;

public class CHWCDBDataAccessException extends CHWCDBException
{
    public CHWCDBDataAccessException() {  super(); }
    public CHWCDBDataAccessException(String message) { super(message); }
    public CHWCDBDataAccessException(String message, Throwable cause) { super(message, cause); }
}
