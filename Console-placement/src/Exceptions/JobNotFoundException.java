package Exceptions;

public class JobNotFoundException extends Exception{
         public JobNotFoundException(String message){
        super(message);
        // when jobid does not exiost in the job list
    }
}
