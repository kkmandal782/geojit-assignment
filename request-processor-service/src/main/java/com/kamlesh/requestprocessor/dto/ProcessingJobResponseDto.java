package com.kamlesh.requestprocessor.dto;

public class ProcessingJobResponseDto {
    private String id;
    private String job_name;
    private String job_creation_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getJob_creation_time() {
        return job_creation_time;
    }

    public void setJob_creation_time(String job_creation_time) {
        this.job_creation_time = job_creation_time;
    }
}
