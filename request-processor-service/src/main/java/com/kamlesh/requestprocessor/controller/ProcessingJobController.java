package com.kamlesh.requestprocessor.controller;

import com.kamlesh.requestprocessor.dto.ProcessingJobRequestDto;
import com.kamlesh.requestprocessor.dto.ProcessingJobResponseDto;
import com.kamlesh.requestprocessor.mapper.ProcessingJobMapper;
import com.kamlesh.requestprocessor.service.ProcessingJobService;
import com.kamlesh.requestprocessor.service.QueueService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/", produces = "application/json")
public class ProcessingJobController {
    private final ProcessingJobService processingJobService;
    private static final Logger logger = LoggerFactory.getLogger(ProcessingJobController.class);
    public ProcessingJobController(ProcessingJobService processingJobService){
        this.processingJobService = processingJobService;
    }
    @PostMapping(value = "/submit", consumes = "application/json")
    public ResponseEntity<String> queue(@Valid @RequestBody ProcessingJobRequestDto processingJobRequestDto){

        logger.info("Received job submission request: {}",processingJobRequestDto);

        logger.debug("Job pushed to queue successfully");

        processingJobService.createProcessingJob(processingJobRequestDto);
        logger.debug("Job persisted in DB successfully");

        return ResponseEntity.ok().body("Queued For Https and inserted in db!!");
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<ProcessingJobResponseDto>> getAll(){

        logger.info("Fetching All Existing Job!");

        List<ProcessingJobResponseDto> jobs = processingJobService.findAll();
        logger.debug("Total Job Returned {}", jobs.size());
        return ResponseEntity.ok().body(jobs);
    }
}
