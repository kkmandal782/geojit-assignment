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
    private final ProcessingJobRepository processingJobRepository;
    private final Logger log = LoggerFactory.getLogger(ProcessingJobService.class);

    public ProcessingJobService(ProcessingJobRepository processingJobRepository){
        this.processingJobRepository = processingJobRepository;
    }


    @CacheEvict(value = "jobs", allEntries = true)
    public ProcessingJobResponseDto createProcessingJob(ProcessingJobRequestDto processingJobRequestDto){
        if(processingJobRepository.existsByJobName(processingJobRequestDto.getName())){
            throw new JobAlreadyExistException("Process Job Already Exists !"+processingJobRequestDto.getName());
        }

        ProcessingJob processingJob = processingJobRepository.save(ProcessingJobMapper.toModel(processingJobRequestDto));
        return ProcessingJobMapper.toDTO(processingJob);
    }

    @Cacheable(value = "jobs",key = "'all'")
    public List<ProcessingJobResponseDto> findAll(){
        log.info("[REDIS]: Cache miss - fetching from DB");
        List<ProcessingJob> jobs = processingJobRepository.findAll();
        return jobs.stream()
                .map(ProcessingJobMapper::toDTO)
                .toList();
    }
}
