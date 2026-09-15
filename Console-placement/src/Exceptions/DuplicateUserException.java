package Exceptions;

public class DuplicateUserException extends Exception{
        public DuplicateUserException(String message){
        super(message);

        // When same user registerd twice   
    }
}
