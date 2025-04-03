package com.example.kafka_spring_cloud_stream.controllers;

import com.example.kafka_spring_cloud_stream.events.PageEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@RestController
public class PageEventController {
    private StreamBridge streamBridge;

    public PageEventController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @GetMapping("/publish")
    public PageEvent publish(String name, String topic){
        PageEvent event = new PageEvent(name, Math.random()>0.5 ? "U1":"U2",new Date(),10 + new Random().nextInt(100000));

        streamBridge.send(topic,event);
        return event;
    }
}
