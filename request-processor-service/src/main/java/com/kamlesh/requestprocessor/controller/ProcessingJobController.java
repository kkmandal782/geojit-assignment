package com.kamlesh.requestprocessor.controller;

import com.kamlesh.requestprocessor.dto.ProcessingJobRequestDto;
import com.kamlesh.requestprocessor.dto.ProcessingJobResponseDto;
import com.kamlesh.requestprocessor.mapper.ProcessingJobMapper;
import com.kamlesh.requestprocessor.service.ProcessingJobService;
import com.kamlesh.requestprocessor.service.QueueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/", produces = "application/json")
public class ProcessingJobController {
    private final QueueService queueService;
    private final ProcessingJobService processingJobService;
    public ProcessingJobController(QueueService queueService, ProcessingJobService processingJobService){
        this.queueService = queueService;
        this.processingJobService = processingJobService;
    }
    @PostMapping(value = "/submit", consumes = "application/json")
    public ResponseEntity<String> queue(@Valid @RequestBody ProcessingJobRequestDto processingJobRequestDto){
        queueService.add(processingJobRequestDto);
        processingJobService.createProcessingJob(processingJobRequestDto);
        return ResponseEntity.ok().body("Queued For Https and inserted in db!!");
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<ProcessingJobResponseDto>> getAll(){
        List<ProcessingJobResponseDto> jobs = processingJobService.findAll();
        return ResponseEntity.ok().body(jobs);
    }
}
