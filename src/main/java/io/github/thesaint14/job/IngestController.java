package io.github.thesaint14.job;

import java.nio.file.Path;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.thesaint14.cleaning.NullNormalizer;
import io.github.thesaint14.db.SchemaWriter;
import io.github.thesaint14.inference.TypeInferer;
import io.github.thesaint14.parser.DirectoryIngestor;
import io.github.thesaint14.parser.IngestResult;
import io.github.thesaint14.parser.ParsedData;

@RestController 

public class IngestController {
    private final DataSource dataSource;
    private final IngestionJobRepository jobRepository;


    public IngestController(DataSource dataSource, IngestionJobRepository jobRepository) {
        this.dataSource = dataSource;
        this.jobRepository = jobRepository;
    }

    @PostMapping("/ingest")
    public List<IngestionJob> ingest(@RequestParam String directory) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return new DirectoryIngestor().ingest(Path.of(directory)).stream()
                    .map(result -> runJob(conn, result))
                    .toList();
        }
    }

    private IngestionJob runJob(Connection conn, IngestResult result) {
        IngestionJob job = new IngestionJob();
        job.setFileName(result.getFile().toString());
        job.setIngestedAt(LocalDateTime.now());

        if (!result.isSuccess()) {
            job.setStatus("FAILED");
            job.setErrorMessage(result.getError().getMessage());
            return jobRepository.save(job);
        }

        try {
            ParsedData inferred = new TypeInferer().infer(result.getData());
            ParsedData cleaned = new NullNormalizer().normalize(inferred);
            new SchemaWriter().write(conn, cleaned.getSchema(), cleaned.getRecords());
            job.setStatus("SUCCESS");
            job.setRowCount(cleaned.getRecords().size());
        } catch (Exception e) {
            job.setStatus("FAILED");
            job.setErrorMessage(e.getMessage());
        }
        return jobRepository.save(job);
    }
}