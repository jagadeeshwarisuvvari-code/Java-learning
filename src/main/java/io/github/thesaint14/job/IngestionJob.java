package io.github.thesaint14.job;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity 
@Getter 
@Setter 
@NoArgsConstructor 


public class IngestionJob {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String status;
    private Integer rowCount;
    private String errorMessage;
    private LocalDateTime ingestedAt;
    
}
