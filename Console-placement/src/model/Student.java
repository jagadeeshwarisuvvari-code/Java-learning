package model;
import java.util.LinkedHashSet;
import java.util.Set;
import enums.Role;

public class Student extends User{
    private String fullName;
    private String branch;
    private double cgpa;

    private final Set<String> appliedJobIds = new LinkedHashSet<>();

    public Student(String id,String username,String password,String fullName,String branch,double cgpa){
        super(id,username,password,Role.STUDENT);
        this.fullName = fullName;
        this.branch = branch;
        this.cgpa = cgpa;
    }

    public String getFullName(){
        return fullName;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public String getBranch(){
        return branch;
    }

    public void setBranch(String branch){
        this.branch = branch;
    }

    public double getCgpa(){
        return cgpa;
    }

    public void setCgpa(double cgpa){
        this.cgpa = cgpa;
    }

    public Set<String> getAppliedJobIds(){
        return appliedJobIds;
    }

    public boolean hasAppliedTo(String jobId){
        return appliedJobIds.contains(jobId);
    }

    public void recordApplication(String jobId){
        appliedJobIds.add(jobId);
    }

    @Override
    public String getProfileSummary(){
        return String.format("Student: %s | Branch: %s | CGPA: %.2f | Applications: %d",fullName,branch,cgpa,appliedJobIds.size());
    }
}


