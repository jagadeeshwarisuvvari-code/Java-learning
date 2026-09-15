package Service;
import Exceptions.JobNotFoundException;
import model.Job;
import java.util.ArrayList;
import java.util.List;

public class JobService{

    private List<Job> jobs = new ArrayList<>();

    public Job postJob(String id,String title,String company,double minCgpa,double packageLpa,List<String> skills){
        Job job = new Job(id,title,company,minCgpa,packageLpa);
        job.getRequiredSkills().addAll(skills);
        jobs.add(job);
        return job;
    }

    public Job getJob(String jobId) throws JobNotFoundException{
        for(Job job:jobs){
            if(job.getId().equals(jobId)){
                return job;
            }
        }
        throw new JobNotFoundException("No job found with id " + jobId);
    }

    public List<Job> getAllJobs(){
        return jobs;
    }

    public List<Job> getJobsSortedByPackage(){
        List<Job> result = new ArrayList<>(jobs);
        // sorting job stored in result list 
        for(int i=0;i<result.size();i++){
            for(int j=i+1;j<result.size();j++){
                if(result.get(i).getPackageLpa()<result.get(j).getPackageLpa()){
                    Job temp = result.get(i);
                    result.set(i, result.get(j));
                    result.set(j, temp);
                }
            }
        }

        return result;
    }

    public List<Job> getEligibleJobs(double studentCgpa){
        List<Job> result = new ArrayList<>();
        for(Job job:jobs){
            if(job.isEligible(studentCgpa)){
                result.add(job);
            }
        }
        return result;
    }

    public boolean deleteJob(String jobId){
        for(Job job:jobs){
            if(job.getId().equals(job)){
                jobs.remove(job);
                return true;
            }
        }
        return false;
    }
}
