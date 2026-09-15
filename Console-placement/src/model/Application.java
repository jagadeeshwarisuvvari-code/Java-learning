package model;
import enums.ApplicationStatus;
import java.time.LocalDateTime;

public class Application{
    private final int id;
    private final String studentId;
    private final String jobId;
    private ApplicationStatus status;
    private final LocalDateTime appliedOn;

    public Application(int id,String studentId,String jobId){
        this.id = id;
        this.studentId = studentId;
        this.jobId = jobId;
        this.status = ApplicationStatus.APPLIED;
        this.appliedOn = LocalDateTime.now();
    }

    public int getId(){
        return id;
    }

    public String getStudentId(){
        return studentId;
    }

    public String getJobId(){
        return jobId;
    }

    public ApplicationStatus getStatus(){
        return status;
    }

    public void setStatus(ApplicationStatus status){
        this.status = status;
    }

    public LocalDateTime getAppliedOn(){
        return appliedOn;
    }

    @Override
    public String toString(){
        return String.format("[App#%d] studentId=%s jobId=%s status=%s",id,studentId,jobId,status);
    }
}


