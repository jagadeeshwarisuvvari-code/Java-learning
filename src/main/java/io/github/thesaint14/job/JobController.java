package io.github.thesaint14.job;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping ("/jobs")

public class JobController {
    private final IngestionJobRepository jobRepository;

    public JobController(IngestionJobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @GetMapping
    public List<IngestionJob> getAllJobs() {
        return jobRepository.findAll();
    }

    @GetMapping("/{id}")
    public IngestionJob getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No job found with id: " + id));
    }

    @DeleteMapping ("/{id}")
    public void delete(@PathVariable Long id){
        jobRepository.deleteById(id);
    }
}
