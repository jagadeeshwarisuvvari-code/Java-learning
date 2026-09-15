package Service;
import Exceptions.InvalidCredentialsException;
import Exceptions.UserNotFoundException;
import Exceptions.DuplicateUserException;
import model.User;
import model.Admin;
import model.Student;
import java.util.ArrayList;
import java.util.List;

public class AuthService{
    private List<User> users = new ArrayList<>();
    public AuthService(){
        Admin admin = new Admin("A200180","Suvvari","admin123","Placement Coordinator");
        users.add(admin);
    }

    public Student registerStudent(String studentId,String username,String password,String fullName,String branch,double cgpa) throws DuplicateUserException{
        for(User user:users){
            if(user.getId().equals(studentId)){
                throw new DuplicateUserException("Student ID " + studentId + " is already registered");
            }
        }
        Student student = new Student(studentId,username,password,fullName,branch,cgpa);
        users.add(student);
        return student;
    }

    public User login(String username,String password) throws UserNotFoundException,InvalidCredentialsException{
        for(User user:users){
            if(user.getUsername().equals(username)){
                if(!user.checkPassword(password)){
                    throw new InvalidCredentialsException("Invalid password for username: " + username);
                }
             return user;
            }  
        }
        throw new UserNotFoundException("User with username " + username + " not found");
    }

    public List<User> getAllUsers(){
        return users;
    }

}