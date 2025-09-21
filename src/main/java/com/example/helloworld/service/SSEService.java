package main.java.com.example.helloworld.service;

import main.java.com.example.helloworld.model.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.UUID;

@Service
public class SSEService {
    
    private final Sinks.Many<Message> messageSink = Sinks.many().multicast().onBackpressureBuffer();
    
    public Flux<Message> getMessageStream() {
        return messageSink.asFlux()
                .doOnNext(message -> System.out.println("Sending message: " + message))
                .doOnError(error -> System.err.println("Error in message stream: " + error));
    }
    
    public void sendMessage(String content, String sender) {
        String messageId = UUID.randomUUID().toString();
        Message message = new Message(messageId, content, sender);
        
        messageSink.tryEmitNext(message);
        System.out.println("Message sent: " + message);
    }
    
    public void sendMessage(Message message) {
        if (message.getId() == null) {
            message.setId(UUID.randomUUID().toString());
        }
        if (message.getTimestamp() == null) {
            message.setTimestamp(java.time.LocalDateTime.now());
        }
        
        messageSink.tryEmitNext(message);
        System.out.println("Message sent: " + message);
    }
}
