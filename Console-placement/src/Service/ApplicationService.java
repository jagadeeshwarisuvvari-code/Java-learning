package Service;
import enums.ApplicationStatus;
import Exceptions.AlreadyAppliedException;
import Exceptions.JobNotFoundException;
import model.Application;
import model.Job;
import model.Student;

import java.util.ArrayList;
import java.util.List;

public class ApplicationService{
    private List<Application> applications = new ArrayList<>();
    private JobService jobService;
    private int appid = 1;
    public ApplicationService(JobService jobService){
        this.jobService = jobService;
    }

    public Application apply(Student student,String jobId) throws JobNotFoundException, AlreadyAppliedException{  
        Job job = jobService.getJob(jobId);
        if(student.hasAppliedTo(jobId)){
            throw new AlreadyAppliedException(student.getFullName()+ " has already applied to job "+ jobId);
        }

        Application application = new Application(appid,student.getId(),jobId);
        appid++;
        applications.add(application);
        student.recordApplication(jobId);
        return application;
    }

    public List<Application> getApplicationsForJob(String jobId) throws JobNotFoundException{
        List<Application> result = new ArrayList<>();
        for(Application application:applications){
            if(application.getJobId().equals(jobId)){
                result.add(application);
            }
        }   
        return result;
    }

    public List<Application> getApplicationsForStudent(String studentId){
        List<Application> result = new ArrayList<>();
        for(Application application:applications){
            if(application.getStudentId().equals(studentId)){
                result.add(application);
            }
        }
        return result;
    }

    public List<Application> getAllApplications() {
        return applications;
    }

    public void updateStatus(int applicationId,ApplicationStatus newStatus){
        for(Application application:applications){
            if(application.getId()==applicationId){
                application.setStatus(newStatus);
                return;
            }
        }
    }
}

