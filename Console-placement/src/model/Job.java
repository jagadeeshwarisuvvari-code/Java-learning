package model;
import java.util.ArrayList;
import java.util.List;
public class Job{
    private final String id;
    private String title;
    private String company;
    private double minCgpa;
    private double packageLpa;

    private final List<String> requiredSkills = new ArrayList<>();

    public Job(String id,String title,String company,double minCgpa,double packageLpa){
        this.id = id;
        this.title = title;
        this.company = company;
        this.minCgpa = minCgpa;
        this.packageLpa = packageLpa;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getCompany(){
        return company;
    }

    public void setCompany(String company){
        this.company = company;
    }

    public double getMinCgpa(){
        return minCgpa;
    }

    public void setMinCgpa(double minCgpa){
        this.minCgpa = minCgpa;
    }

    public double getPackageLpa(){
        return packageLpa;
    }

    public void setPackageLpa(double packageLpa){
        this.packageLpa = packageLpa;
    }

    public List<String> getRequiredSkills(){
        return requiredSkills;
    }

    public boolean isEligible(double studentCgpa){
        return studentCgpa>=minCgpa;
    }

    @Override
    public String toString(){
        return String.format("[Job#%s] %s @ %s | Min CGPA: %.1f | Package: %.1f LPA | Skills: %s",id,title,company,minCgpa,packageLpa,requiredSkills);
    }
}


