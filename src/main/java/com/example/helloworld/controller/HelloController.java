package com.example.helloworld.controller;

import com.example.helloworld.service.SSEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HelloController {

    @Autowired
    private SSEService sseService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/hello/{name}")
    public String helloWithName(@org.springframework.web.bind.annotation.PathVariable String name) {
        return "Hello, " + name + "!";
    }

    // POST endpoint to send messages
    @PostMapping("/messages")
    public String sendMessage(@RequestBody MessageRequest request) {
        sseService.sendMessage(request.getMessage());
        return "Message sent successfully";
    }

    // GET endpoint for SSE stream
    @GetMapping(value = "/messages/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, String>> streamMessages() {
        return sseService.getMessageStream();
    }

    // Request DTO for message creation
    public static class MessageRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
