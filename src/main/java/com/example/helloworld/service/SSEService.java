package com.example.helloworld.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@Service
public class SSEService {
    
    private final Sinks.Many<Map<String, String>> messageSink = Sinks.many().multicast().onBackpressureBuffer();
    
    public Flux<Map<String, String>> getMessageStream() {
        return messageSink.asFlux()
                .doOnNext(message -> System.out.println("Sending message: " + message))
                .doOnError(error -> System.err.println("Error in message stream: " + error));
    }
    
    public void sendMessage(String message) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("message", message);
        
        messageSink.tryEmitNext(messageMap);
        System.out.println("Message sent: " + message);
    }
}
