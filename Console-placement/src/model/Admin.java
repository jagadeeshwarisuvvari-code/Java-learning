package model;
import enums.Role;

public class Admin extends User{
    private String designation;

    public Admin(String id,String username,String password,String designation){
        super(id,username,password,Role.ADMIN);
        this.designation = designation;
    }

    public String getDesignation(){
        return designation;
    }

    public void setDesignation(String designation){
        this.designation = designation;
    }

    @Override
    public String getProfileSummary(){
        return "Admin: " + getUsername()+designation;
    }
}

