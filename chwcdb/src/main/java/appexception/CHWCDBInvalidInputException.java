package appexception;

public class CHWCDBInvalidInputException extends Exception
{
    public CHWCDBInvalidInputException() {  super(); }
    public CHWCDBInvalidInputException(String message) { super(message); }
    public CHWCDBInvalidInputException(String message, Throwable cause) { super(message, cause); }
}
