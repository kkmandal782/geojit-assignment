package com.kamlesh.requestprocessor.service;

import com.kamlesh.requestprocessor.dto.ProcessingJobRequestDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private final RestTemplate restTemplate;

    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public void sendToExternal(ProcessingJobRequestDto job) {
        try {
            System.out.println("Sending job: " + job.getName());

            String response = restTemplate.postForObject(
                    "https://host.docker.internal:9090/external/api",
                    job,
                    String.class
            );

            System.out.println("Response: " + response);

        } catch (Exception e) {
            System.err.println("Failed: " + e.getMessage());
        }
    }
}
