package com.kamlesh.externapi.controller;

import com.kamlesh.externapi.dto.ProcessingJobRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/external")
public class ApiController {

    @PostMapping("/api")
    public ResponseEntity<String> receive(@RequestBody ProcessingJobRequestDto processingJobRequestDto){
        System.out.println("Request Received for "+processingJobRequestDto.getName());
        return ResponseEntity.ok().body("Request Received for Job "+ processingJobRequestDto.getName());
    }

    @GetMapping("/checking")
    public ResponseEntity<String> check(){
        return ResponseEntity.ok().body("External api is working");
    }
}
