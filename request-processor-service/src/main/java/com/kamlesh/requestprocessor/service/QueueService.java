package com.kamlesh.requestprocessor.service;

import com.kamlesh.requestprocessor.dto.ProcessingJobRequestDto;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class QueueService {
    private final BlockingQueue<ProcessingJobRequestDto> queue = new LinkedBlockingQueue<>();

    public void add(ProcessingJobRequestDto entity) {
        queue.add(entity);
    }

    public ProcessingJobRequestDto poll() {
        return queue.poll();
    }
}
