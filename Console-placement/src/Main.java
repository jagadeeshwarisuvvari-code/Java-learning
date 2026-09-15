import enums.*;
import model.*;
import Exceptions.*;
import Service.ApplicationService;
import Service.AuthService;
import Service.JobService;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main{
   private static final Scanner sc = new Scanner(System.in);
   private static final AuthService authserv = new AuthService();
   private static final JobService jobserv = new JobService();
   private static final ApplicationService applicationserv = new ApplicationService(jobserv);

   public static void main(String []args){
        while(true){
            System.out.println("Welcome to the placament cell ");
            System.out.println("\n 1.login \n 2.Register as  student \n 3.exit\n  ");
            System.out.println("Choose: ");
            String choice = sc.nextLine().trim();
            switch(choice){
                case "1":handleLogin();
                            break;
                case "2":handleRegister();
                            break;
                case "3":System.out.println("Exiting the applciation");
                            return;
                default: System.out.println("Invalid choice, try again");
            }

        }
   }

   private static void handleLogin(){
        System.out.println("Enter username: ");
        String username = sc.nextLine().trim();
        System.out.println("Enter password: ");
        String password = sc.nextLine().trim();

        try{
            User user = authserv.login(username,password);
            if(user.getRole() == Role.ADMIN) {
                AdminMenu((Admin) user);
            }       
            else if(user.getRole() == Role.STUDENT) {
                StudentMenu((Student) user);
            }
        }
         catch(UserNotFoundException | InvalidCredentialsException e){
            System.out.println(e.getMessage());
        }
    }

    private static void handleRegister(){
        System.out.println("Enter student ID: ");
        String studentId = sc.nextLine().trim();
        System.out.println("Enter username: ");
        String username = sc.nextLine().trim();
        System.out.println("Enter password: ");
        String password = sc.nextLine().trim();
        System.out.println("Enter full name: ");
        String fullName = sc.nextLine().trim();
        System.out.println("Enter branch: ");
        String branch = sc.nextLine().trim();
        System.out.println("Enter CGPA: ");
        double cgpa = Double.parseDouble(sc.nextLine().trim());
        try{
            Student student =authserv.registerStudent(studentId,username,password,fullName,branch,cgpa);    
            System.out.println("Registration successful! You can now log in");
        }
         catch(DuplicateUserException e){
            System.out.println(e.getMessage());
        }
    
    }

    private static void AdminMenu(Admin admin){
      //  System.out.println(admin.getProfileSummary());
        while(true){
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Create Job");
            System.out.println("2. View All Jobs");
            System.out.println("3. View all Students");
            System.out.println("4. View Applications for a Job");
            System.out.println("5.Update Applcation Status");
            System.out.println("6. Logout");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            switch(choice){
                case "1":CreateJob();
                            break;
                case "2": ViewAllJobs();
                            break;
                case "3": ViewAllStudents();
                            break;
                case "4": ViewApplicationsForJob();
                            break;
                case "5": UpdateApplicationStatus();
                            break;
                case "6": System.out.println("Logging out...");
                            return; 
                default: System.out.println("Invalid choice, try agai");
            }
        }
    }

    private static void CreateJob(){
        System.out.println("Enter the Jobid");
        String jobId =sc.nextLine().trim();
        System.out.println("Enter job title: ");
        String title = sc.nextLine().trim();
        System.out.println("Enter company name: ");
        String company = sc.nextLine().trim();  
        System.out.println("Enter minimum CGPA required: ");
        double minCgpa = Double.parseDouble(sc.nextLine().trim());
        System.out.println("Enter package in LPA: ");
        double packageLpa = Double.parseDouble(sc.nextLine().trim());
        System.out.println("Enter required skills (comma-separated): ");
        String[] skillsArr = sc.nextLine().trim().split(",");
        List<String> requiredSkills = new ArrayList<>();
        for (String skill : skillsArr) {
            requiredSkills.add(skill.trim());
        }
        Job newjob = jobserv.postJob(jobId,title,company,minCgpa,packageLpa,requiredSkills);
    }

    private static void ViewAllJobs(){
        List<Job> jobs = jobserv.getAllJobs();
        if(jobs.isEmpty()){
            System.out.println("No jobs available");
        }
         else{
            for(Job job:jobs){
                System.out.println(job);
            }
        }
    }

    private static void ViewAllStudents(){
        List<User> users = authserv.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No students registered");
        } else {
            for (User user:users){
                System.out.println(user);
            }
        }
    }

    private static void ViewApplicationsForJob(){
        System.out.println("Enter job ID: ");
        String jobId = sc.nextLine().trim();
        try{
            List<Application> applications = applicationserv.getApplicationsForJob(jobId);
            if (applications.isEmpty()) {
                System.out.println("No applications for this job");
            }
            else{
                for(Application app:applications){
                    System.out.println(app);
                }
            }
        }
        catch(JobNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    private static void UpdateApplicationStatus(){
        System.out.println("Enter application ID: ");
        int appId = Integer.parseInt(sc.nextLine().trim());
        System.out.println("Enter new status (APPLIED, SHORTLISTED, SELECTED,REJECTED): ");
        String status = sc.nextLine().trim();
        try{
            ApplicationStatus newStatus = ApplicationStatus.valueOf(status.toUpperCase());
            applicationserv.updateStatus(appId,newStatus);
            System.out.println("Application status updated successfully");
        } 
        catch(IllegalArgumentException e){
            System.out.println("Invalid status. Please enter APPLIED, SHORTLISTED, SELECTED, or REJECTED.");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    //Student menu
    private static void StudentMenu(Student student){
        while(true){
            System.out.println("\nStudent Menu:");
            System.out.println("1. View All Jobs");
            System.out.println("2. View Eligible Jobs");
            System.out.println("3. Apply for a Job");
            System.out.println("4. View My Applications");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            switch(choice){
                case "1": ViewAllJobs();
                            break;
                case "2": ViewEligibleJobs(student);
                            break;
                case "3": ApplyForJob(student);
                            break;
                case "4": ViewMyApplications(student);
                            break;
                case "5": System.out.println("Logging out...");
                            return; 
                default: System.out.println("Invalid choice, try again");
            }
        }

    }

    private static void ViewEligibleJobs(Student student){
        List<Job> jobs = jobserv.getEligibleJobs(student.getCgpa());
        if(jobs.isEmpty()){
            System.out.println("No eligible jobs available");
        }
         else{
            for(Job job:jobs){
                System.out.println(job);
            }
        }
    }


    private static void ApplyForJob(Student student){
        System.out.println("Enter job ID to apply: ");
        String jobId = sc.nextLine().trim();
        try{
            Application application = applicationserv.apply(student,jobId);
            System.out.println("Application submitted successfully: " + application);
        }
        catch(JobNotFoundException | AlreadyAppliedException e){
            System.out.println(e.getMessage());
        }
    }

    private static void ViewMyApplications(Student student){
        List<Application> applications = applicationserv.getApplicationsForStudent(student.getId());
        if(applications.isEmpty()){
            System.out.println("You have not applied to any jobs yet");
        }
         else{
            for(Application app:applications){
                System.out.println(app);
            }
        }
    }

}


