package com.kamlesh.requestprocessor.mapper;

import com.kamlesh.requestprocessor.dto.ProcessingJobRequestDto;
import com.kamlesh.requestprocessor.dto.ProcessingJobResponseDto;
import com.kamlesh.requestprocessor.model.ProcessingJob;

import java.time.LocalDate;

public class ProcessingJobMapper {
    public static ProcessingJobResponseDto toDTO(ProcessingJob processingJob){
        ProcessingJobResponseDto processingJobResponseDto = new ProcessingJobResponseDto();
        processingJobResponseDto.setId(processingJob.getId().toString());
        processingJobResponseDto.setJob_name(processingJob.getJob_name());
        processingJobResponseDto.setJob_creation_time(processingJob.getJob_creation_time().toString());
        return processingJobResponseDto;
    }

    public static ProcessingJob toModel(ProcessingJobRequestDto processingJobRequestDto){
        ProcessingJob processingJob = new ProcessingJob();
        processingJob.setJob_name(processingJobRequestDto.getName());
        processingJob.setJob_creation_time(LocalDate.parse(processingJobRequestDto.getJob_creation_time()));
        return processingJob;
    }
}
