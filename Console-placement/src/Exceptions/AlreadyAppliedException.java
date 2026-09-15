package Exceptions;

public class AlreadyAppliedException extends Exception{
     public AlreadyAppliedException(String message){
        super(message);
        // When student tries to apply the same job twice
        }
}
