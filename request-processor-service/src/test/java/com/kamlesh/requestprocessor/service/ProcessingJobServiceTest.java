package com.kamlesh.requestprocessor.service;

import com.kamlesh.requestprocessor.dto.ProcessingJobRequestDto;
import com.kamlesh.requestprocessor.dto.ProcessingJobResponseDto;
import com.kamlesh.requestprocessor.exception.JobAlreadyExistException;
import com.kamlesh.requestprocessor.model.ProcessingJob;
import com.kamlesh.requestprocessor.repository.ProcessingJobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessingJobServiceTest {

    @Mock
    private ProcessingJobRepository repository;

    @InjectMocks
    private ProcessingJobService service;

    // -------------------------
    // TEST 1: duplicate job
    // -------------------------
    @Test
    void shouldThrowExceptionWhenJobAlreadyExists() {

        ProcessingJobRequestDto dto = new ProcessingJobRequestDto();
        dto.setName("job1");
        dto.setJob_creation_time("2026-04-15");

        when(repository.existsByJobName("job1")).thenReturn(true);

        assertThrows(JobAlreadyExistException.class,
                () -> service.createProcessingJob(dto));

        verify(repository, never()).save(any());
    }

    // -------------------------
    // TEST 2: success flow
    // -------------------------
    @Test
    void shouldCreateJobSuccessfully() {

        ProcessingJobRequestDto dto = new ProcessingJobRequestDto();
        dto.setName("job1");
        dto.setJob_creation_time("2026-04-15");

        ProcessingJob saved = new ProcessingJob();
        saved.setId(UUID.randomUUID());
        saved.setJob_name("job1");
        saved.setJob_creation_time(java.time.LocalDate.parse("2026-04-15"));

        when(repository.existsByJobName("job1")).thenReturn(false);
        when(repository.save(any(ProcessingJob.class))).thenReturn(saved);

        ProcessingJobResponseDto response =
                service.createProcessingJob(dto);

        assertNotNull(response);
        assertEquals("job1", response.getJob_name());
        assertEquals("2026-04-15", response.getJob_creation_time());

        verify(repository, times(1)).save(any(ProcessingJob.class));
    }

    @Test
    void shouldCallSaveWithCorrectEntity() {

        ProcessingJobRequestDto dto = createRequest("job2");

        when(repository.existsByJobName("job2")).thenReturn(false);

        when(repository.save(any(ProcessingJob.class))).thenAnswer(invocation -> {
            ProcessingJob job = invocation.getArgument(0);

            // simulate DB behavior (IMPORTANT)
            job.setId(UUID.randomUUID());

            return job;
        });

        service.createProcessingJob(dto);

        verify(repository).save(argThat(job ->
                job.getJob_name().equals("job2") &&
                        job.getJob_creation_time() != null
        ));
    }

    // -------------------------------
    // 4. Verify existsByJobName called once
    // -------------------------------
    @Test
    void shouldCheckExistenceBeforeSaving() {

        ProcessingJobRequestDto dto = createRequest("job3");

        when(repository.existsByJobName("job3")).thenReturn(false);

        when(repository.save(any(ProcessingJob.class))).thenAnswer(invocation -> {
            ProcessingJob job = invocation.getArgument(0);

            // simulate DB generated ID
            job.setId(UUID.randomUUID());

            return job;
        });

        service.createProcessingJob(dto);

        verify(repository, times(1)).existsByJobName("job3");

        verify(repository, times(1)).save(any(ProcessingJob.class));
    }

    // -------------------------------
    // Helper method
    // -------------------------------
    private ProcessingJobRequestDto createRequest(String name) {
        ProcessingJobRequestDto dto = new ProcessingJobRequestDto();
        dto.setName(name);
        dto.setJob_creation_time("2026-04-15");
        return dto;
    }
}