package Exceptions;

public class InvalidCredentialsException extends Exception{
     public InvalidCredentialsException(String message){
        super(message);
        // when login username or passwprd does not match
    }
}
