package model;
import enums.Role;

public abstract class User{
    private final String id;
    private String username;
    private String password;
    private final Role role;

    User(String id,String username,String password,Role role){
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public boolean checkPassword(String password){
        return this.password!=null && this.password.equals(password);
    }

    public void setPassword(String password){
        this.password = password;
    }

    public Role getRole(){
        return role;
    }

    public abstract String getProfileSummary();

    @Override
    public String toString(){
        return "User{id=" + id + ", username='" + username + "', role=" + role + '}';
    }
}


