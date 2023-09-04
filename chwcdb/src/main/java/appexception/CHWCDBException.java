package appexception;

public class CHWCDBException extends Exception
{
    public CHWCDBException() {  super(); }
    public CHWCDBException(String message) { super(message); }
    public CHWCDBException(String message, Throwable cause) { super(message, cause); }
}
