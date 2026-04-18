//package com.kamlesh.externapi.model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//
//import java.time.LocalDate;
//import java.util.UUID;
//
//@Entity
//public class ProcessingJob {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//
//    @NotNull
//    @Column(name = "job_name", unique = true)
//    private String jobName;
//
//    @NotNull
//    private LocalDate job_creation_time;
//
//    public UUID getId() {
//        return id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    public String getJob_name() {
//        return jobName;
//    }
//
//    public void setJob_name(String jobName) {
//        this.jobName = jobName;
//    }
//
//    public LocalDate getJob_creation_time() {
//        return job_creation_time;
//    }
//
//    public void setJob_creation_time(LocalDate job_creation_time) {
//        this.job_creation_time = job_creation_time;
//    }
//}
