package main.java.com.example.helloworld.controller;

import main.java.com.example.helloworld.model.Message;
import main.java.com.example.helloworld.service.SSEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
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
        sseService.sendMessage(request.getContent(), request.getSender());
        return "Message sent successfully";
    }

    // GET endpoint for SSE stream
    @GetMapping(value = "/messages/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> streamMessages() {
        return sseService.getMessageStream();
    }

    // Request DTO for message creation
    public static class MessageRequest {
        private String content;
        private String sender;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }
    }
}
