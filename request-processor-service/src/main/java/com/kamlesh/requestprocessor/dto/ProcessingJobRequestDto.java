package com.kamlesh.requestprocessor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProcessingJobRequestDto {

    @NotBlank(message = "Processing Name is required")
    @Size(max = 100, message = "Processing Name can't exceed 100 characker")
    private String name;

    @NotBlank(message = "Process Creation date is required")
    private String job_creation_time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob_creation_time() {
        return job_creation_time;
    }

    public void setJob_creation_time(String job_creation_time) {
        this.job_creation_time = job_creation_time;
    }
}
