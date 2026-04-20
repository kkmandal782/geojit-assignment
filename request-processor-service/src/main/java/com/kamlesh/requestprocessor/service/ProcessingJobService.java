package com.kamlesh.requestprocessor.service;

import com.kamlesh.requestprocessor.dto.ProcessingJobRequestDto;
import com.kamlesh.requestprocessor.dto.ProcessingJobResponseDto;
import com.kamlesh.requestprocessor.exception.JobAlreadyExistException;
import com.kamlesh.requestprocessor.mapper.ProcessingJobMapper;
import com.kamlesh.requestprocessor.model.ProcessingJob;
import com.kamlesh.requestprocessor.repository.ProcessingJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessingJobService {

    private static final Logger log = LoggerFactory.getLogger(ProcessingJobService.class);

    private final ProcessingJobRepository processingJobRepository;
    private final QueueService queueService;

    public ProcessingJobService(ProcessingJobRepository processingJobRepository, QueueService queueService){
        this.processingJobRepository = processingJobRepository;
        this.queueService = queueService;
    }

    @CacheEvict(value = "jobs", allEntries = true)
    public ProcessingJobResponseDto createProcessingJob(ProcessingJobRequestDto processingJobRequestDto){

        log.info("Creating processing job with name={}", processingJobRequestDto.getName());

        if(processingJobRepository.existsByJobName(processingJobRequestDto.getName())){
            log.warn("Duplicate job creation attempt for name={}", processingJobRequestDto.getName());
            throw new JobAlreadyExistException("Process Job Already Exists !" + processingJobRequestDto.getName());
        }
        // Adding in queue for Async processing
        queueService.add(processingJobRequestDto);
        // Inserting in db
        ProcessingJob processingJob = processingJobRepository.save(
                ProcessingJobMapper.toModel(processingJobRequestDto)
        );

        log.info("Processing job created successfully with id={}", processingJob.getId());

        return ProcessingJobMapper.toDTO(processingJob);
    }

    @Cacheable(value = "jobs", key = "'all'")
    public List<ProcessingJobResponseDto> findAll(){

        log.debug("Cache miss for 'jobs', fetching from database");

        List<ProcessingJob> jobs = processingJobRepository.findAll();

        log.debug("Fetched {} processing jobs from database", jobs.size());

        return jobs.stream()
                .map(ProcessingJobMapper::toDTO)
                .toList();
    }
}