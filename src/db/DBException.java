package db;

public class DBException extends RuntimeException
{
    // we creates a class for the exception 
    // in this case we inject a string, a message in the class and send this message to constructor to the main class
    public DBException(String msg)
    {
        super(msg);
    }
}