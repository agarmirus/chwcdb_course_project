package appexception;

public class CHWCDBInvalidParameterException extends CHWCDBException
{
    public CHWCDBInvalidParameterException() {  super(); }
    public CHWCDBInvalidParameterException(String message) { super(message); }
    public CHWCDBInvalidParameterException(String message, Throwable cause) { super(message, cause); }
}
